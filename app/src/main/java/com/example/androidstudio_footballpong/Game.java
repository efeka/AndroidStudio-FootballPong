package com.example.androidstudio_footballpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.androidstudio_footballpong.objects.Ball;
import com.example.androidstudio_footballpong.objects.Player1;

/**
 * Manages all objects in the game by updating and rendering them.
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;

    private Bitmap background;

    private final Player1 player1;
    private final Ball ball;

    private int touchPauseTimer = 0, touchPauseCooldown = 0;
    private boolean touchPaused = false;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        background = background.createScaledBitmap(background, MainActivity.screenWidth, MainActivity.screenHeight, false);

        gameLoop = new GameLoop(this, surfaceHolder);

        player1 = new Player1(getContext(), MainActivity.screenWidth / 2, MainActivity.screenHeight / 2, MainActivity.screenHeight / 10, MainActivity.screenWidth / 10);
        ball = new Ball(getContext(), MainActivity.screenWidth / 2, MainActivity.screenHeight / 2, MainActivity.screenWidth / 30, MainActivity.screenHeight / 30, player1);

        setFocusable(true);
    }

    public static enum STATE{
        MAIN_MENU,
        PAUSED_1P,
        PAUSED_2P,
        ONE_PLAYER,
        TWO_PLAYERS
    };
    public static STATE state;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        player1.handleTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        //background
        /*
        int color = ContextCompat.getColor(getContext(), R.color.background);
        paint.setColor(color);
        canvas.drawRect(new Rect(0, 0, MainActivity.screenWidth, MainActivity.screenHeight), paint);
        */
        canvas.drawBitmap(background, 0f, 0f, paint);

        //drawUPS(canvas);
        //drawFPS(canvas);
        player1.draw(canvas);
        ball.draw(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.teal_700);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.teal_700);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void update() {
        player1.update();
        ball.update();
    }

    public void pauseGame() {
        gameLoop.pauseLoop();
    }

}
