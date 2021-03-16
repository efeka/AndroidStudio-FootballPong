package com.example.androidstudio_footballpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.objects.Player1;

/**
 * Manages all objects in the game by updating and rendering them.
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;

    private final Player1 player1;

    private int touchPauseTimer = 0, touchPauseCooldown = 0;
    private boolean touchPaused = false;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        player1 = new Player1(getContext(), MainActivity.screenWidth / 2, MainActivity.screenHeight / 2, 32, 32);

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
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        canvas.drawRect(new Rect(0, 0, MainActivity.screenWidth, MainActivity.screenHeight), paint);

        player1.draw(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void update() {
        player1.update();
    }

    public void pauseGame() {
        gameLoop.pauseLoop();
    }

}
