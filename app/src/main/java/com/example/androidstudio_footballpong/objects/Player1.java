package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player1 extends GameObject {

    private Paint paint;
    private Context context;

    public Player1(Context context, double x, double y) {
        super(x, y);
        this.context = context;
        width = height = 32;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }

    @Override
    public Rect getBounds() {
        return null;
    }
}
