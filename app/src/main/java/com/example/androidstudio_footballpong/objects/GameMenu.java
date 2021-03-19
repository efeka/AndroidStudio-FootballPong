package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.Texture;

public class GameMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Paint paint;

    public GameMenu(Context context, double x, double y) {
        super(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tex.gameMenu[0], (float) x, (float) y, paint);
    }

    @Override
    public void update() {

    }

    @Override
    public Rect getBounds() {
        return null;
    }

}
