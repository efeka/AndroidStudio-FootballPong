package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.R;

public class Goal extends GameObject {

    public static final int LEFT_GOAL = 0;
    public static final int RIGHT_GOAL = 1;
    private int type;

    private Paint paint;
    private Context context;

    private int initialHeight;
    private float initialX;

    public Goal(Context context, double x, double y, int width, int height, int type) {
        super(x, y, width, height);
        this.context = context;
        this.type = type;

        initialHeight = height;
        initialX = (float) x;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player1);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(ContextCompat.getColor(context, R.color.player1));
        canvas.drawRect(getBounds(), paint);

        /* boundaries
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        canvas.drawRect(getBottomBounds(), paint);
        canvas.drawRect(getTopBounds(), paint);
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawRect(getScoringBounds(), paint);
         */
    }

    @Override
    public void update() {

    }

    @Override
    public Rect getBounds() {
        return createRect((int) x, (int) y, width, height);
    }

    public Rect getBoundsTop() {
        return createRect((int) x, (int) y, width, height / 8);
    }

    public Rect getBoundsBottom() {
        return createRect((int) x, (int) y + height - height / 8, width, height / 8);
    }

    public Rect getBoundsScore() {
        if (type == LEFT_GOAL)
            return createRect((int) x, (int) y + height / 8, width / 2, height - height / 4);
        else
            return createRect((int) x + width / 2, (int) y + height / 8, width / 2, height - height / 4);
    }

}
