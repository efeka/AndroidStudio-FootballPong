package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Animation;
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
    private final int SECOND_BORDER_LEFT = 3 * MainActivity.screenWidth / 4;
    private int currentLeftBorder = BORDER_LEFT;

    private Texture tex = Game.getTexture();
    private GameData gameData = Game.getGameData();

    private Paint paint;

    private Ball ball;
    private Goal leftGoal;

    public static final int DEFAULT_MAX_SPEED_EASY = 7;
    public static final int DEFAULT_MAX_SPEED_MEDIUM = 11;
    public static final int DEFAULT_MAX_SPEED_HARD = 13;
    private int maxSpeed = 0;

    private double initialX, initialY;

    private boolean moving = false;
    private boolean repositioningX = false, repositioningY = false;
    private double targetX = 0, targetY = 0;
    private boolean ignoreX = false, ignoreY = false;

    private static int maxEnergy = 1000;
    private static int energy = 1000;

    private int shootTimer = 0, shootCooldown = 60;

    private Animation aiPlayerIdle, aiPlayerWalk;

    public AIPlayer(Context context, Ball ball, Goal leftGoal, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.ball = ball;
        this.leftGoal = leftGoal;

        initialX = x;
        initialY = y;

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        aiPlayerWalk = new Animation(2, tex.aiPlayer[4], tex.aiPlayer[3], tex.aiPlayer[2], tex.aiPlayer[1], tex.aiPlayer[0]);
        aiPlayerIdle = new Animation(2, tex.aiPlayer[7], tex.aiPlayer[6], tex.aiPlayer[5], tex.aiPlayer[6]);
    }

    @Override
    public void draw(Canvas canvas) {
        if (moving)
            aiPlayerWalk.drawAnimation(canvas, paint, (float) x, (float) y);
        else
            aiPlayerIdle.drawAnimation(canvas, paint, (float) x, (float) y);
    }

    @Override
    public void update() {
        if (energy <= 0) {
            switch (gameData.getDifficulty()) {
                case GameData.DIFFICULTY_EASY:
                    if (maxSpeed > DEFAULT_MAX_SPEED_EASY / 4)
                        maxSpeed = DEFAULT_MAX_SPEED_EASY / 4;
                    break;
                case GameData.DIFFICULTY_MEDIUM:
                    if (maxSpeed > DEFAULT_MAX_SPEED_MEDIUM / 4)
                        maxSpeed = DEFAULT_MAX_SPEED_MEDIUM / 4;
                    break;
                case GameData.DIFFICULTY_HARD:
                    if (maxSpeed > DEFAULT_MAX_SPEED_HARD / 4)
                        maxSpeed = DEFAULT_MAX_SPEED_HARD / 4;
                    break;
            }
        }

        int winningMovementThreshold = height;
        int losingMovementThreshold = height / 2;
        boolean winning = (gameData.getScore1() - gameData.getScore2() < 0);
        int currentMovementThreshold = winning ? winningMovementThreshold : losingMovementThreshold;

        repositioningY = ball.getResetting() && Math.abs(y + height / 2 - MainActivity.screenHeight / 2) > height / 2;
        repositioningX = energy <= maxEnergy / 4 && x < SECOND_BORDER_LEFT;
        if (repositioningX || repositioningY) {
            if (repositioningY) {
                if (y + height / 2 < MainActivity.screenHeight / 2)
                    velY = maxSpeed;
                else
                    velY = -maxSpeed;
                moving = true;
            }
            if (repositioningX) {
                velX = maxSpeed;
                moving = true;
            }
        } else {
            if (energy <= maxEnergy / 2 && x >= SECOND_BORDER_LEFT) {
                currentLeftBorder = SECOND_BORDER_LEFT;
            }

            if (ball.getVelX() <= 0 || (Math.abs(x - ball.getX()) < currentMovementThreshold && Math.abs(y - ball.getY()) < currentMovementThreshold)) {
                velX = velY = 0;
                moving = false;
            } else {
                if (Math.abs(y - ball.getY()) < currentMovementThreshold) {
                    velY = 0;
                } else if (y < ball.getY()) {
                    velY = maxSpeed;
                    moving = true;
                } else if (y > ball.getY()) {
                    velY = -maxSpeed;
                    moving = true;
                }

                if (Math.abs(x - ball.getX()) < currentMovementThreshold) {
                    velX = 0;
                } else if (Math.abs(y - ball.getY()) < MainActivity.screenHeight / 3) {
                    if (x < ball.getX()) {
                        velX = maxSpeed;
                        moving = true;
                    } else {
                        velX = -maxSpeed;
                        moving = true;
                    }
                }
            }
        }

        if (moving) {
            if (energy > 0)
                energy -= 1;
            x += velX;
            y += velY;
        }

        collision();
        aiPlayerWalk.runAnimation();
        aiPlayerIdle.runAnimation();
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
        if (x < currentLeftBorder) {
            x = currentLeftBorder;
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

    public static void setMaxEnergy(int maxEng) {
        maxEnergy = energy = maxEng;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void reset() {
        x = initialX;
        y = initialY;
        velX = velY = 0;
    }

}
