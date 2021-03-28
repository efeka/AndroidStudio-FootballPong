package com.example.androidstudio_footballpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.objects.Ball;
import com.example.androidstudio_footballpong.objects.GameMenu;
import com.example.androidstudio_footballpong.objects.Goal;
import com.example.androidstudio_footballpong.objects.Player1;

/*
 * TODO: add goals on each side
 * TODO: make the game menu functional
 * TODO: add kicking system for players
 * TODO: add 1 player and 2 player modes
 * TODO: make a main menu
 * TODO: add random power ups
 * TODO: graphics
 */

/**
 * Manages all objects in the game by updating and rendering them.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private static Texture tex;

    private GameLoop gameLoop;

    private Bitmap background;

    private final GameMenu gameMenu;
    private final Player1 player1;
    private final Ball ball;
    private final Goal leftGoal, rightGoal;

    public Animation touchEffect;

    private int touchPauseTimer = 0, touchPauseCooldown = 0;
    private boolean touchPaused = false;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        tex = new Texture(context);
        gameLoop = new GameLoop(this, surfaceHolder);

        touchEffect = new Animation(1, tex.touchEffect[7], tex.touchEffect[6], tex.touchEffect[5], tex.touchEffect[4], tex.touchEffect[3], tex.touchEffect[2], tex.touchEffect[1], tex.touchEffect[0]);

        player1 = new Player1(getContext(), MainActivity.screenWidth / 4, MainActivity.screenHeight / 2, MainActivity.screenHeight / 10, MainActivity.screenWidth / 10);
        gameMenu = new GameMenu(getContext(), MainActivity.screenWidth / 2 - MainActivity.screenWidth / 28, 3, player1);
        ball = new Ball(getContext(), MainActivity.screenWidth / 2 - MainActivity.screenWidth / 80, MainActivity.screenHeight / 2 - MainActivity.screenWidth / 80, MainActivity.screenWidth / 40, MainActivity.screenWidth / 40, player1);
        int goalWidth = MainActivity.screenWidth / 25, goalHeight = 2 * MainActivity.screenHeight / 7;
        leftGoal = new Goal(getContext(), ball, 0, MainActivity.screenHeight / 2 - goalHeight / 2, goalWidth, goalHeight);
        rightGoal = new Goal(getContext(), ball,MainActivity.screenWidth - goalWidth, MainActivity.screenHeight / 2 - goalHeight / 2, goalWidth, goalHeight);

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

    public static Texture getTexture() {
        return tex;
    }

    boolean pressed = false;
    float releaseX = 0, releaseY = 0;
    float touchStartX = 1;
    float touchStartY = 1;
    boolean swiped = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!pressed) {
                    touchStartX = event.getX();
                    touchStartY = event.getY();
                    releaseX = releaseY = 0;
                }
                pressed = true;
                return true;
            case MotionEvent.ACTION_UP:
                pressed = false;
                releaseX = event.getX();
                releaseY = event.getY();

                //if the user did not swipe far enough, the swipe will be assumed to be accidental and it will count as a tap instead
                int minimumSwipeLimit = 50;
                if (Math.abs(touchStartX - releaseX) < minimumSwipeLimit && Math.abs(touchStartY - releaseY) < minimumSwipeLimit) {
                    player1.handleTap(touchStartX, touchStartY);

                    touchEffect.resetAnimation();
                    touchEffect.setX(touchStartX - touchEffect.getWidth() / 2);
                    touchEffect.setY(touchStartY - touchEffect.getHeight() / 2);
                    touchEffect.resumeAnimation();
                }
                else {
                    player1.handleSwipe(touchStartX, touchStartY, releaseX, releaseY);
                    ball.handleSwipe(1, touchStartX, touchStartY, releaseX, releaseY);
                }
                return true;
        }
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
        //background (in game)
        canvas.drawBitmap(tex.gameBackground, 0f, 0f, paint);

        //drawUPS(canvas);
        //drawFPS(canvas);

        player1.draw(canvas);
        ball.draw(canvas);
        gameMenu.draw(canvas);
        touchEffect.drawAnimation(canvas, paint);
        leftGoal.draw(canvas);
        rightGoal.draw(canvas);
    }

    //displays the number of updates
    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    //displays the number of frames
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
        ball.update();
        gameMenu.update();
        leftGoal.update();
        rightGoal.update();

        touchEffect.runAnimation();
        if (touchEffect.getPlayedOnce())
            touchEffect.stopAnimation();
    }

    public void pauseGame() {
        gameLoop.pauseLoop();
    }

    public Context getGameContext() {
        return getContext();
    }

}
