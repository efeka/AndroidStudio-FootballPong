package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;

import java.util.ArrayList;

/**
 * Ball is an object that bounces from goals and screen borders and is used for scoring goals.
 * Players can kick the ball by swiping the screen to give it speed and direction.
 */
public class Ball extends GameObject {

    private final int BORDER_LEFT = 10;
    private final int BORDER_RIGHT = MainActivity.screenWidth - 10;
    private final int BORDER_UP = 10;
    private final int BORDER_DOWN = MainActivity.screenHeight - 10;

    private Paint paint;
    private Context context;

    private Player1 player1;
    private Goal leftGoal, rightGoal;

    private double initialX, initialY;

    private final int MIN_SPEED = 10;
    private final int MAX_SPEED = 60;
    private final float DECELERATION = 0.01f;
    private float currentSpeedX, currentSpeedY;

    public ArrayList<GameObject> trailList = new ArrayList<>();

    public Ball(Context context, Goal leftGoal, Goal rightGoal, Player1 player1, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;
        this.leftGoal = leftGoal;
        this.rightGoal = rightGoal;
        this.player1 = player1;

        initialX = x;
        initialY = y;

        velX = -MIN_SPEED;
        velY = MIN_SPEED;
        currentSpeedX = (float) velX;
        currentSpeedY = (float) velY;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.purple_200);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((int) x, (int) y, width, paint);
        /*
        int color = ContextCompat.getColor(context, R.color.black);
        paint.setColor(color);
        canvas.drawRect(getBoundsLeft(), paint);
        canvas.drawRect(getBoundsTop(), paint);
        canvas.drawRect(getBoundsBottom(), paint);
        canvas.drawRect(getBoundsRight(), paint);
         */
    }

    @Override
    public void update() {
        x += velX;
        y += velY;

        if (Math.abs(velX) > MIN_SPEED)
            velX -= velX * DECELERATION;
        if (Math.abs(velY) > MIN_SPEED)
            velY -= velY * DECELERATION;

        collision();
    }

    public void collision() {
        //collision with screen borders
        if (x - width / 2 < BORDER_LEFT) {
            velX *= -1;
            x = BORDER_LEFT + width / 2;
        }
        if (x + width / 2 > BORDER_RIGHT) {
            velX *= -1;
            x = BORDER_RIGHT - width / 2;
        }
        if (y - width < BORDER_UP) {
            velY *= -1;
            y = BORDER_UP + width;
        }
        if (y + width > BORDER_DOWN) {
            velY *= -1;
            y = BORDER_DOWN - width;
        }

        //collision with the left goal
        if (getBoundsTop().intersect(leftGoal.getBoundsTop())) {
            velY *= -1;
            y = leftGoal.getY() + leftGoal.getBoundsTop().height() + width;
        }
        if (getBoundsTop().intersect(leftGoal.getBoundsBottom())) {
            velY *= -1;
            y = leftGoal.getY() + leftGoal.getHeight() + width;
        }
        if (getBoundsBottom().intersect(leftGoal.getBoundsTop())) {
            velY *= -1;
            y = leftGoal.getY() - width;
        }
        if (getBoundsBottom().intersect(leftGoal.getBoundsBottom())) {
            velY *= -1;
            y = leftGoal.getY() + leftGoal.getHeight() - leftGoal.getHeight() / 8 - width;
        }
        if (getBoundsLeft().intersect(leftGoal.getBoundsTop()) || getBoundsLeft().intersect(leftGoal.getBoundsBottom())) {
            velX *= -1;
            x = leftGoal.getX() + leftGoal.getWidth() + width;
        }
        if (getBounds().intersect(leftGoal.getBoundsScore())) {
            //TODO: increase player2's score
            //TODO: pause the game for a moment
            resetPosition();
        }

        //collision with the right goal
        if (getBoundsTop().intersect(rightGoal.getBoundsTop())) {
            velY *= -1;
            y = rightGoal.getY() + rightGoal.getBoundsTop().height() + width;
        }
        if (getBoundsTop().intersect(rightGoal.getBoundsBottom())) {
            velY *= -1;
            y = rightGoal.getY() + rightGoal.getHeight() + width;
        }
        if (getBoundsBottom().intersect(rightGoal.getBoundsTop())) {
            velY *= -1;
            y = rightGoal.getY() - width;
        }
        if (getBoundsBottom().intersect(rightGoal.getBoundsBottom())) {
            velY *= -1;
            y = rightGoal.getY() + rightGoal.getHeight() - rightGoal.getHeight() / 8 - width;
        }
        if (getBoundsRight().intersect(rightGoal.getBoundsTop()) || getBoundsRight().intersect(rightGoal.getBoundsBottom())) {
            velX *= -1;
            x = rightGoal.getX() - width;
        }
        if (getBounds().intersect(rightGoal.getBoundsScore())) {
            //TODO: increase player1's score
            //TODO: pause the game for a moment
            resetPosition();
        }

    }

    /**
     * @param playerId indicates which player did the swipe, set this to 0 for AIPlayer, 1 for Player1 and 2 for Player2
     */
    public void handleSwipe(int playerId, float touchStartX, float touchStartY, float releaseX, float releaseY) {
        if (playerId == 0) {

        }
        else if (playerId == 1) {
            if (getBounds().intersect(player1.getBounds())) {
                double hypot = Math.hypot(releaseX - touchStartX, releaseY - touchStartY);
                velX = (float) (MAX_SPEED * (releaseX - touchStartX) / hypot);
                velY = (float) (MAX_SPEED * (releaseY - touchStartY) / hypot);
            }
        }
        else if (playerId == 2) {

        }
    }

    public void resetPosition() {
        x = initialX;
        y = initialY;
        velX = -MIN_SPEED;
        velY = MIN_SPEED;
    }

    @Override
    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + width);
    }

    public Rect getBoundsTop() {
        return createRect((int) x - width + 15, (int) y - width, width * 2 - 30, width / 4);
    }

    public Rect getBoundsBottom() {
        return createRect((int) x - width + 15, (int) y + width - 10, width * 2 - 30, width / 4);
    }

    public Rect getBoundsLeft() {
        return createRect((int) x - width, (int) y - width + 15, width / 4, width * 2 - 30);
    }

    public Rect getBoundsRight() {
        return createRect((int) x + width - 10, (int) y - width + 15, width / 4, width * 2 - 30);
    }

}
