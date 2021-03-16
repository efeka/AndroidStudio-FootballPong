package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.R;

/**
 * Player1 is the main character which is controlled by the user in both "1 player" and "2 player" modes.
 */
public class Player1 extends GameObject {

    private Paint paint;
    private Context context;

    private boolean moving = false;
    private double targetX = 0, targetY = 0;

    public Player1(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.black);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), paint);
    }

    @Override
    public void update() {
        if (moving) {
            x += velX;
            y += velY;
        }

        if (moving) {
            if (Math.abs(x - targetX) <= 10 && Math.abs(y - targetY) <= 10) {
                moving = false;
                velX = velY = 0;
            }
        }

    }

    @Override
    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public void handleTouchEvent(MotionEvent event) {
        targetX = event.getX() - width / 2;
        targetY = event.getY() - height / 2;
        moving = true;

        double hypot = Math.hypot(targetX - x, targetY - y);
        velX = (float) (7 * (targetX - x) / hypot);
        velY = (float) (7 * (targetY - y) / hypot);
    }
}
