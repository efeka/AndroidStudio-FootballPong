package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.Texture;

public class AIPlayer extends GameObject {

    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;
    public static int selectedDifficulty = MEDIUM;

    private Context context;
    private Paint paint;
    private Texture tex = Game.getTexture();

    private Ball ball;

    public AIPlayer(Context context, Ball ball, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;
        this.ball = ball;

        paint = new Paint();
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
