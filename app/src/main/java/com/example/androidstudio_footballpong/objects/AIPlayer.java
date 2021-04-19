package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.GameData;
import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

/**
 * AIPlayer is controlled by the computer when the user selects the "1 Player" mode.
 * It has easy, medium and hard modes which will change how it shoots and how it manages it's energy.
 */
public class AIPlayer extends GameObject {

    private final int BORDER_LEFT = MainActivity.screenWidth / 2;
    private final int BORDER_RIGHT = MainActivity.screenWidth;
    private final int BORDER_UP = 0;
    private final int BORDER_DOWN = MainActivity.screenHeight;

    private Texture tex = Game.getTexture();
    private GameData gameData = Game.getGameData();

    private Paint paint;

    private Ball ball;
    private Goal leftGoal;

    private final int DEFAULT_MAX_SPEED = 15;
    private int maxSpeed = 15;

    private boolean moving = false;
    private double targetX = 0, targetY = 0;
    private boolean ignoreX = false, ignoreY = false;

    public int maxEnergy = 1000;
    public static int energy = 1000;

    private int shootTimer = 0, shootCooldown = 60;

    /*
    Scoring with a wall bounce from the top border formula
    ----------------------------------
    double h = MainActivity.screenHeight;
    double w = MainActivity.screenWidth;
    double t = w - x;
    double z = y;
    int releaseX = (int) ((h * w - h * t) / (2 * z + h));
    int releaseY = 0;
    */

    public AIPlayer(Context context, Ball ball, Goal leftGoal, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.ball = ball;
        this.leftGoal = leftGoal;

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.black));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getBoundsExact(), paint);
    }

    @Override
    public void update() {
        if (energy <= 0 && maxSpeed > DEFAULT_MAX_SPEED / 4)
            maxSpeed = DEFAULT_MAX_SPEED / 4;

        if (ball.getX() < MainActivity.screenWidth / 2) {
            if (ball.getVelX() <= 0) {
                velX = velY = 0;
                moving = false;
            } else {
                if (Math.abs(y - ball.getY()) < 30) {
                    velY = 0;
                    moving = false;
                } else if (y < ball.getY()) {
                    velY = maxSpeed;
                    moving = true;
                } else if (y > ball.getY()) {
                    velY = -maxSpeed;
                    moving = true;
                }
            }
        }

        if (moving) {
            if (energy > 0)
                energy -= 1;

            x += velX;
            y += velY;

            if ((Math.abs(x - targetX) <= 10 || ignoreX) && (Math.abs(y - targetY) <= 10 || ignoreY)) {
                moving = false;
                velX = velY = 0;
            }
        }

        collision();
    }

    private void collision() {
        if (shootTimer < shootCooldown)
            shootTimer++;
        else {
            if (getBounds().intersect(ball.getBounds())) {
                shootTimer = 0;
                shoot();
            }
        }

        //collisions with screen borders
        if (x < BORDER_LEFT) {
            x = BORDER_LEFT;
            ignoreX = true;
            velX = 0;
        }
        if (x + width > BORDER_RIGHT) {
            x = BORDER_RIGHT - width;
            ignoreX = true;
            velX = 0;
        }
        if (y < BORDER_UP) {
            y = BORDER_UP;
            ignoreY = true;
            velY = 0;
        }
        if (y + height > BORDER_DOWN) {
            y = BORDER_DOWN - height;
            ignoreY = true;
            velY = 0;
        }
    }

    /**
     * This method makes the AI take a shot and it is only called if the ball is close enough to the AIPlayer.
     * The AI will attempt to do different and more difficult shots depending on the selected difficulty.
     */
    private void shoot() {
        int shotType = (int) (Math.random() * 2);

        if (shotType == 0) { //direct shot
            if (gameData.getDifficulty() == GameData.DIFFICULTY_EASY) {
                int accuracy = (int) (Math.random() * 5);
                if (accuracy < 2) { //40% chance for a purposefully flawed shot
                    int mistakeType = (int) (Math.random() * 2);
                    float targetX = leftGoal.getBounds().centerX();
                    float targetY;
                    if (mistakeType == 0) { //shoot too high
                        targetY = leftGoal.getBounds().centerY() - leftGoal.getBounds().height() / 2;
                    } else { //shoot too low
                        targetY = leftGoal.getBounds().centerY() + leftGoal.getBounds().height() / 2;
                    }
                    ball.handleSwipe(0, (float) ball.getX() + ball.getWidth() / 2, (float) ball.getY() + ball.getWidth() / 2, targetX, targetY);
                } else { //60% chance for an accurate shot
                    float targetX = leftGoal.getBounds().centerX();
                    float targetY = leftGoal.getBounds().centerY();
                    ball.handleSwipe(0, (float) ball.getX() + ball.getWidth() / 2, (float) ball.getY() + ball.getWidth() / 2, targetX, targetY);
                }
            } else if (gameData.getDifficulty() == GameData.DIFFICULTY_MEDIUM || gameData.getDifficulty() == GameData.DIFFICULTY_HARD) {
                float targetX = leftGoal.getBounds().centerX();
                float targetY = leftGoal.getBounds().centerY();
                ball.handleSwipe(0, (float) ball.getX() + ball.getWidth() / 2, (float) ball.getY() + ball.getWidth() / 2, targetX, targetY);
            }
        } else if (shotType == 1) { //bounce from top/bottom borders
            if (gameData.getDifficulty() == GameData.DIFFICULTY_MEDIUM || gameData.getDifficulty() == GameData.DIFFICULTY_HARD) {
                //Calculating a shot where the ball will bounce from the top or bottom border and go into the center of the goal.
                float h = MainActivity.screenHeight;
                float w = MainActivity.screenWidth;
                float distRight = w - (float) x;
                float distUp = (float) y;
                float targetX = (h * w - h * distRight) / (2 * distUp + h) - ball.getWidth() / 2;
                float targetY = 0;

                int randomBorder = (int) (Math.random() * 2);
                if (randomBorder == 0)
                    targetY = MainActivity.screenHeight;

                ball.handleSwipe(0, (float) ball.getX() + ball.getWidth() / 2, (float) ball.getY() + ball.getWidth() / 2, targetX, targetY);
            }
        }
    }

    @Override
    public Rect getBounds() {
        return createRect((int) x - width / 2, (int) y - height / 4, width + width, height + height / 2);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
}
