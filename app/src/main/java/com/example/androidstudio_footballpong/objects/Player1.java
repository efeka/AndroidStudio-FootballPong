package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Animation;
import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

/**
 * Player1 is the main character which is controlled by the user in both "1 player" and "2 player" modes.
 */
public class Player1 extends GameObject {

    private Texture tex = Game.getTexture();

    private final int BORDER_LEFT = 0;
    private final int BORDER_RIGHT = MainActivity.screenWidth / 2;
    private final int BORDER_UP = 0;
    private final int BORDER_DOWN = MainActivity.screenHeight;

    private Paint paint;
    private Context context;

    private int defaultMaxSpeed = 10;
    private int maxSpeed = 10;

    private boolean moving = false;
    private double targetX = 0, targetY = 0;
    private boolean ignoreX = false, ignoreY = false;

    private float swipeStartX = 0, swipeStartY = 0;
    private float swipeEndX = 0, swipeEndY = 0;

    private int maxEnergy = 150;
    private int energy = 100;

    private Animation player1Walk;

    public Player1(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player1);
        paint.setColor(color);

        player1Walk = new Animation(1, tex.player1[0], tex.player1[1], tex.player1[2], tex.player1[3], tex.player1[2], tex.player1[1]);
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawRect(getBounds(), paint);
        //canvas.drawLine(swipeStartX, swipeStartY, swipeEndX, swipeEndY, paint);
        player1Walk.drawAnimation(canvas, paint, (float) x, (float) y);
    }

    @Override
    public void update() {
        if (!moving && energy < maxEnergy)
            energy++;
        if (energy >= maxEnergy)
            energy = maxEnergy;

        if (moving) {
            energy -= 2;
            if (energy <= 0)
                moving = false;

            x += velX;
            y += velY;

            if ((Math.abs(x - targetX) <= 10 || ignoreX) && (Math.abs(y - targetY) <= 10 || ignoreY)) {
                moving = false;
                velX = velY = 0;
            }
        }

        collision();
        player1Walk.runAnimation();
    }

    private void collision() {
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

    @Override
    public Rect getBounds() {
        return createRect((int) x - width / 2, (int) y - height / 4, width + width, height + height / 2);
    }

    public void handleTap(float touchStartX, float touchStartY) {
        targetX = touchStartX;
        targetY = touchStartY;
        moving = true;
        ignoreX = ignoreY = false;

        double hypot = Math.hypot(targetX - x, targetY - y);
        velX = (float) (maxSpeed * (targetX - x) / hypot);
        velY = (float) (maxSpeed * (targetY - y) / hypot);
    }

    public void handleSwipe(float touchStartX, float touchStartY, float releaseX, float releaseY) {
        swipeStartX = touchStartX;
        swipeStartY = touchStartY;
        swipeEndX = releaseX;
        swipeEndY = releaseY;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

}
