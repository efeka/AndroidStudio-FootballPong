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

    /*
    Data for angle & coordinate relations
    downwards shots
    top left = 485.96375 334.0393 1295.9033 925.02686
    top mid = 794.96704 363.0432 1240.9589 789.0381
    top right = 1287.9089 308.03467 1622.9279 848.03467
    mid = 827.9608 653.01636 1309.9274 984.02344
    mid right = 1258.9124 686.04126 1611.8848 1079.0112

    upwards shots
    mid = 566.9238 550.01953 1052.9553 249.03809
    mid right = 1129.9182 580.04517 1677.9401 0.032958984
    bot left = 29.945068 1028.0237 1394.9524 21.060791
    bot mid = 518.9575 1025.0244 1549.8944 21.060791
    bot right = 1041.9122 1021.0034 1637.9004 25.048828
    */

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
