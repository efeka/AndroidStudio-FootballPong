package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;

/**
 * Player1 is the main character which is controlled by the user in both "1 player" and "2 player" modes.
 */
public class Player1 extends GameObject {

    private Paint paint;
    private Context context;

    private boolean moving = false;
    private double targetX = 0, targetY = 0;

    private Bitmap player1;

    public Player1(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.black);
        paint.setColor(color);

        player1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_sheet);
        player1 = player1.createScaledBitmap(player1, MainActivity.screenWidth / 3, MainActivity.screenHeight / 3, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(player1, (float) x, (float) y, paint);
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
