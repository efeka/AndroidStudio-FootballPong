package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.R;

public class Goal extends GameObject {

    private Paint paint;

    private Ball ball;

    private int initialHeight;
    private float initialX;

    public Goal(Context context, Ball ball, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.ball = ball;
        initialHeight = height;
        initialX = (float) x;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player1);
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
        return createRect((int) x, (int) y, width, height);
    }

}
