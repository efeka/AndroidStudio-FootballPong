package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.R;

/**
 * Player1 is the main character which is controlled by the user in both "1 player" and "2 player" modes.
 */
public class Player1 extends GameObject {

    private Paint paint;
    private Context context;

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

    }

    @Override
    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }
}
