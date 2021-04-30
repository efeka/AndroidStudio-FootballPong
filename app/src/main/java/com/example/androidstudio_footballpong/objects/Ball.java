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
    private Texture tex = Game.getTexture();

    private Player1 player1;
    private Player2 player2;
    private Goal leftGoal, rightGoal;
    private GameData gameData;

    private int resetTimer = 0, resetCooldown = 45;
    private int nextDirection;
    private boolean resetting = false;
    private double initialX, initialY;

    private final int MIN_SPEED = 10;
    private final int MAX_SPEED = 60;
    private final float DECELERATION = 0.01f;

    public Ball(Goal leftGoal, Goal rightGoal, Player1 player1, Player2 player2, GameData gamedata, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.leftGoal = leftGoal;
        this.rightGoal = rightGoal;
        this.player1 = player1;
        this.player2 = player2;
        this.gameData = gamedata;

        initialX = x;
        initialY = y;

        velX = -MIN_SPEED;
        velY = MIN_SPEED;

        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tex.ball, (int) x, (int) y, paint);
    }

    @Override
    public void update() {
        if (resetting) {
            if (resetTimer < resetCooldown) {
                resetTimer++;
                resetPosition();
                return;
            }
            else {
                resetting = false;
                int randomY = (int) (Math.random() * 2);
                if (nextDirection == 0)
                    velX = -MIN_SPEED;
                else
                    velX = MIN_SPEED;

                if (randomY == 0)
                    velY = MIN_SPEED;
                else
                    velY = -MIN_SPEED;
            }
        }

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
        if (x < BORDER_LEFT) {
            velX *= -1;
            x = BORDER_LEFT;
        }
        if (x + width > BORDER_RIGHT) {
            velX *= -1;
            x = BORDER_RIGHT - width;
        }
        if (y < BORDER_UP) {
            velY *= -1;
            y = BORDER_UP;
        }
        if (y + width > BORDER_DOWN) {
            velY *= -1;
            y = BORDER_DOWN - width;
        }

        //collision with the left goal
        if (getBoundsTop().intersect(leftGoal.getBoundsTop())) {
            velY *= -1;
            y = leftGoal.getBoundsTop().centerY() + leftGoal.getBoundsTop().height() / 2;
        }
        if (getBoundsTop().intersect(leftGoal.getBoundsBottom())) {
            velY *= -1;
            y = leftGoal.getBoundsBottom().centerY() + leftGoal.getBoundsBottom().height() / 2;
        }
        if (getBoundsBottom().intersect(leftGoal.getBoundsTop())) {
            velY *= -1;
            y = leftGoal.getBoundsTop().centerY() - leftGoal.getBoundsTop().height() / 2 - width;
        }
        if (getBoundsBottom().intersect(leftGoal.getBoundsBottom())) {
            velY *= -1;
            y = leftGoal.getBoundsBottom().centerY() + leftGoal.getBoundsBottom().height() / 2 - width;
        }
        if (getBoundsLeft().intersect(leftGoal.getBoundsTop()) || getBoundsLeft().intersect(leftGoal.getBoundsBottom())) {
            velX *= -1;
            x = leftGoal.getX() + leftGoal.getWidth() + width;
        }
        if (getBounds().intersect(leftGoal.getBoundsScore())) {
            gameData.setScore2(gameData.getScore2() + 1);
            resetBall(0);
        }

        //collision with the right goal
        if (getBoundsTop().intersect(rightGoal.getBoundsTop())) {
            velY *= -1;
            y = rightGoal.getBoundsTop().centerY() + rightGoal.getBoundsTop().height() / 2;
        }
        else if (getBoundsTop().intersect(rightGoal.getBoundsBottom())) {
            velY *= -1;
            y = rightGoal.getBoundsBottom().centerY() + rightGoal.getBoundsBottom().height() / 2;
        }
        else if (getBoundsBottom().intersect(rightGoal.getBoundsTop())) {
            velY *= -1;
            y = rightGoal.getBoundsTop().centerY() - rightGoal.getBoundsTop().height() / 2 - width;
        }
        else if (getBoundsBottom().intersect(rightGoal.getBoundsBottom())) {
            velY *= -1;
            y = rightGoal.getBoundsBottom().centerY() + rightGoal.getBoundsBottom().height() / 2 - width;
        }
        else if (getBoundsRight().intersect(rightGoal.getBoundsTop()) || getBoundsRight().intersect(rightGoal.getBoundsBottom())) {
            velX *= -1;
            x = rightGoal.getX() - width;
        }
        if (getBounds().intersect(rightGoal.getBoundsScore())) {
            gameData.setScore1(gameData.getScore1() + 1);
            resetBall(1);
        }

    }

    /**
     * @param playerId indicates which player did the swipe, set this to 0 for AIPlayer, 1 for Player1 and 2 for Player2
     */
    public void handleSwipe(int playerId, float touchStartX, float touchStartY, float releaseX, float releaseY) {
        if (resetting)
            return;

        if (playerId == 0) {
            double hypot = Math.hypot(releaseX - touchStartX, releaseY - touchStartY);
            velX = (float) (MAX_SPEED * (releaseX - touchStartX) / hypot);
            velY = (float) (MAX_SPEED * (releaseY - touchStartY) / hypot);
        }
        else if (playerId == 1) {
            if (getBounds().intersect(player1.getBounds())) {
                double hypot = Math.hypot(releaseX - touchStartX, releaseY - touchStartY);
                velX = (float) (MAX_SPEED * (releaseX - touchStartX) / hypot);
                velY = (float) (MAX_SPEED * (releaseY - touchStartY) / hypot);
            }
        }
        else if (playerId == 2) {
            if (getBounds().intersect(player2.getBounds())) {
                double hypot = Math.hypot(releaseX - touchStartX, releaseY - touchStartY);
                velX = (float) (MAX_SPEED * (releaseX - touchStartX) / hypot);
                velY = (float) (MAX_SPEED * (releaseY - touchStartY) / hypot);
            }
        }
    }

    /**
     * @param nextDirection indicates if the ball will go left (=0) or right (=1) after a player scores
     */
    public void resetBall(int nextDirection) {
        velX = velY = 0;
        resetting = true;
        this.nextDirection = nextDirection;
        resetTimer = 0;
    }

    public void resetPosition() {
        x = initialX - width / 2;
        y = initialY - width / 2;
    }

    @Override
    public Rect getBounds() {
        return createRect((int) x, (int) y, width, width);
    }

    public Rect getBoundsTop() {
        return createRect((int) x + 15, (int) y, width - 30, width / 4);
    }

    public Rect getBoundsBottom() {
        return createRect((int) x + 15, (int) y + width - width / 4, width - 30, width / 4);
    }

    public Rect getBoundsLeft() {
        return createRect((int) x, (int) y + 15, width / 4, width - 30);
    }

    public Rect getBoundsRight() {
        return createRect((int) x + width - width / 4, (int) y + 15, width / 4, width - 30);
    }

}
