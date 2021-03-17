package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;

/**
 * Player1 is the main character which is controlled by the user in both "1 player" and "2 player" modes.
 */
public class Player1 extends GameObject {

    private final int BORDER_LEFT = 0;
    private final int BORDER_RIGHT = MainActivity.screenWidth;
    private final int BORDER_UP = 0;
    private final int BORDER_DOWN = MainActivity.screenHeight;

    private Paint paint;
    private Context context;

    private boolean moving = false;
    private double targetX = 0, targetY = 0;
    private boolean ignoreX = false, ignoreY = false;

    private int defaultMaxSpeed = 10;
    private int maxSpeed = 10;

    private Bitmap player1;

    public Player1(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player1);
        paint.setColor(color);

        //player1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_sheet);
        //player1 = player1.createScaledBitmap(player1, MainActivity.screenWidth / 3, MainActivity.screenHeight / 3, false);
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawBitmap(player1, (float) x, (float) y, paint);
        canvas.drawRect(getBounds(), paint);
    }

    @Override
    public void update() {
        if (moving) {
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
        /*
        if (x + width> BORDER_UP) {
            x = BORDER_UP - width;
            ignoreX = true;
            velX = 0;
        }
        if (x < BORDER_DOWN) {
            x = BORDER_DOWN;
            ignoreX = true;
            velX = 0;
        }
        if (y < BORDER_LEFT) {
            y = BORDER_LEFT;
            ignoreY = true;
            velY = 0;
        }
        if (y + height > BORDER_RIGHT) {
            y = BORDER_RIGHT - height;
            ignoreY = true;
            velY = 0;
        }
         */
    }

    @Override
    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void handleTouchEvent(MotionEvent event) {
        targetX = event.getX() - width / 2;
        targetY = event.getY() - height / 2;
        moving = true;
        ignoreX = ignoreY = false;

        double hypot = Math.hypot(targetX - x, targetY - y);
        velX = (float) (maxSpeed * (targetX - x) / hypot);
        velY = (float) (maxSpeed * (targetY - y) / hypot);
    }
}
