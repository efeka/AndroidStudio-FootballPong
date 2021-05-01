package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Animation;
import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

/**
 * Player1 is the main character which is controlled by a user in both "1 player" and "2 player" modes.
 */
public class Player1 extends GameObject {

    private final int BORDER_LEFT = 0;
    private final int BORDER_RIGHT = MainActivity.screenWidth / 2;
    private final int BORDER_UP = 0;
    private final int BORDER_DOWN = MainActivity.screenHeight;

    private Texture tex = Game.getTexture();
    private Paint paint;

    private Goal leftGoal;

    private double initialX, initialY;

    private final int DEFAULT_MAX_SPEED = 10;
    private int maxSpeed = 10;

    private boolean moving = false;
    private double targetX = 0, targetY = 0;
    private boolean ignoreX = false, ignoreY = false;

    private static int maxEnergy = 1000;
    private static int energy = 1000;

    private Animation player1Idle, player1Walk;

    public Player1(Goal leftGoal, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.leftGoal = leftGoal;

        initialX = x;
        initialY = y;

        paint = new Paint();
        player1Walk = new Animation(2, tex.player1[3], tex.player1[4], tex.player1[5], tex.player1[6], tex.player1[7]);
        player1Idle = new Animation(2, tex.player1[0], tex.player1[1], tex.player1[2], tex.player1[1]);
    }

    @Override
    public void draw(Canvas canvas) {
        if (moving)
            player1Walk.drawAnimation(canvas, paint, (float) x, (float) y);
        else
            player1Idle.drawAnimation(canvas, paint, (float) x, (float) y);
    }

    @Override
    public void update() {
        /*
        if (!moving && energy < maxEnergy)
            energy++;
        if (energy >= maxEnergy)
            energy = maxEnergy;
         */
        if (energy <= 0 && maxSpeed > DEFAULT_MAX_SPEED / 4)
            maxSpeed = DEFAULT_MAX_SPEED / 4;

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

        player1Walk.runAnimation();
        player1Idle.runAnimation();
    }

    private void collision() {
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

        if (getBoundsTop().intersect(leftGoal.getBounds())) {
            y = leftGoal.getBounds().centerY() + leftGoal.getBounds().height() / 2;
            ignoreY = true;
            velY = 0;
        } else if (getBoundsBot().intersect(leftGoal.getBounds())) {
            y = leftGoal.getBounds().centerY() - leftGoal.getBounds().height() / 2 - height;
            ignoreY = true;
            velY = 0;
        } else if (getBoundsLeft().intersect(leftGoal.getBounds())) {
            x = leftGoal.getBounds().centerX() + leftGoal.getBounds().width() / 2;
            ignoreX = true;
            velX = 0;
        }

    }

    public Rect getBoundsTop() {
        return createRect((int) x + 15, (int) y, width - 30, height / 5);
    }

    public Rect getBoundsBot() {
        return createRect((int) x + 15, (int) y + height - 10, width - 30, width / 5);
    }

    public Rect getBoundsLeft() {
        return createRect((int) x, (int) y + 15, height / 5, height - 30);
    }

    public Rect getBoundsRight() {
        return createRect((int) x + width - height / 5, (int) y + 15, height / 5, height - 30);
    }

    /**
     * Returns a bigger rectangle than the player's model.
     * This method is only used to check if the player is close enough to interact with the ball.
     */
    @Override
    public Rect getBounds() {
        return createRect((int) x - width / 2, (int) y - height / 4, width + width, height + height / 2);
    }

    public void handleTap(float touchStartX, float touchStartY) {
        targetX = touchStartX - width / 2;
        targetY = touchStartY - height;
        moving = true;
        ignoreX = ignoreY = false;

        double hypot = Math.hypot(targetX - x, targetY - y);
        velX = (float) (maxSpeed * (targetX - x) / hypot);
        velY = (float) (maxSpeed * (targetY - y) / hypot);
    }

    public static int getEnergy() {
        return energy;
    }

    public static void setEnergy(int eng) {
        energy = eng;
    }

    public static int getMaxEnergy() {
        return maxEnergy;
    }

    public static void setMaxEnergy(int maxEng) {
        maxEnergy = energy = maxEng;
    }

    public void reset() {
        x = initialX;
        y = initialY;
        velX = velY = 0;
    }

}
