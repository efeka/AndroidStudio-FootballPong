package com.example.androidstudio_footballpong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.objects.AIPlayer;
import com.example.androidstudio_footballpong.objects.Ball;
import com.example.androidstudio_footballpong.objects.GameMenu;
import com.example.androidstudio_footballpong.objects.Goal;
import com.example.androidstudio_footballpong.objects.MainMenu;
import com.example.androidstudio_footballpong.objects.OnePlayerMenu;
import com.example.androidstudio_footballpong.objects.PauseMenu;
import com.example.androidstudio_footballpong.objects.Player1;
import com.example.androidstudio_footballpong.objects.Player2;
import com.example.androidstudio_footballpong.objects.SettingsMenu;
import com.example.androidstudio_footballpong.objects.TwoPlayersMenu;

/*
 * TODO: Better movement for AIPlayer
 * TODO: Graphics (shooting animations)
 * TODO: Add sound effects
 * TODO: Add a settings menu which has music volume settings (on/off), effects volume settings (on/off) and credits
 */

/**
 * Manages all objects in the game by updating and rendering them.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private static Texture tex;
    private static GameData gameData;

    private Paint paint;

    private GameLoop gameLoop;

    private final MainMenu mainMenu;
    private final OnePlayerMenu onePlayerMenu;
    private final TwoPlayersMenu twoPlayersMenu;
    private final GameMenu gameMenu;
    private final PauseMenu pauseMenu;
    private final SettingsMenu settingsMenu;
    private final Player1 player1;
    private final Player2 player2;
    private final AIPlayer aiPlayer;
    private final Ball ball;
    private final Goal leftGoal, rightGoal;

    public Animation touchEffect;

    boolean pressed = false;
    float releaseX = 0, releaseY = 0;
    float touchStartX = 1;
    float touchStartY = 1;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        tex = new Texture(context);
        paint = new Paint();
        gameLoop = new GameLoop(this, surfaceHolder);

        touchEffect = new Animation(1, tex.touchEffect[7], tex.touchEffect[6], tex.touchEffect[5], tex.touchEffect[4], tex.touchEffect[3], tex.touchEffect[2], tex.touchEffect[1], tex.touchEffect[0]);

        gameData = new GameData();
        mainMenu = new MainMenu(0, 0, MainActivity.screenWidth, MainActivity.screenHeight);
        twoPlayersMenu = new TwoPlayersMenu(getContext(), 0, 0, MainActivity.screenWidth, MainActivity.screenHeight);
        int goalWidth = 100, goalHeight = 2 * MainActivity.screenHeight / 5;
        leftGoal = new Goal(getContext(), 0, (float) MainActivity.screenHeight / 2 - (float) goalHeight / 2, goalWidth, goalHeight, Goal.LEFT_GOAL);
        rightGoal = new Goal(getContext(), MainActivity.screenWidth - goalWidth, (float) MainActivity.screenHeight / 2 - (float) goalHeight / 2, goalWidth, goalHeight, Goal.RIGHT_GOAL);
        player1 = new Player1(leftGoal, (float) MainActivity.screenWidth / 4 - MainActivity.screenWidth / 20, (float) MainActivity.screenHeight / 2 - MainActivity.screenWidth / 20, MainActivity.screenHeight / 6, MainActivity.screenWidth / 12);
        player2 = new Player2(rightGoal, (float) 3 * MainActivity.screenWidth / 4 - MainActivity.screenWidth / 20, (float) MainActivity.screenHeight / 2 - MainActivity.screenWidth / 20, MainActivity.screenHeight / 6, MainActivity.screenWidth / 12);
        ball = new Ball(leftGoal, rightGoal, player1, player2, gameData, (float) MainActivity.screenWidth / 2, (float) MainActivity.screenHeight / 2, MainActivity.screenWidth / 25, MainActivity.screenWidth / 25);
        aiPlayer = new AIPlayer(getContext(), ball, leftGoal, (float) 3 * MainActivity.screenWidth / 4 - MainActivity.screenWidth / 20, (float) MainActivity.screenHeight / 2 - MainActivity.screenWidth / 20, MainActivity.screenHeight / 6, MainActivity.screenWidth / 12);
        onePlayerMenu = new OnePlayerMenu(aiPlayer, 0, 0, MainActivity.screenWidth, MainActivity.screenHeight);
        gameMenu = new GameMenu(getContext(), player1, player2, aiPlayer, gameData, (float) MainActivity.screenWidth / 2 - (float) MainActivity.screenWidth / 28, 3);
        pauseMenu = new PauseMenu(gameData, player1, player2, aiPlayer, ball, 0, 0, MainActivity.screenWidth, MainActivity.screenHeight);
        settingsMenu = new SettingsMenu(gameData, 0, 0, MainActivity.screenWidth, MainActivity.screenHeight);

        setFocusable(true);
    }

    public enum STATE {
        MAIN_MENU,
        ONE_PLAYER_MENU,
        TWO_PLAYERS_MENU,
        SETTINGS,
        PAUSED_1P,
        PAUSED_2P,
        ONE_PLAYER,
        TWO_PLAYERS
    }

    public static STATE state = STATE.MAIN_MENU;

    public static Texture getTexture() {
        return tex;
    }

    public static GameData getGameData() {
        return gameData;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (state == STATE.ONE_PLAYER || state == STATE.TWO_PLAYERS) {
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
                    if ((Math.abs(touchStartX - releaseX) < minimumSwipeLimit) && (Math.abs(touchStartY - releaseY) < minimumSwipeLimit)) {
                        if (gameMenu.getPauseButtonBorder().contains(touchStartX, touchStartY)) {
                            if (state == STATE.ONE_PLAYER)
                                state = STATE.PAUSED_1P;
                            else if (state == STATE.TWO_PLAYERS)
                                state = STATE.PAUSED_2P;
                        } else {
                            if (touchStartX < MainActivity.screenWidth / 2)
                                player1.handleTap(touchStartX, touchStartY);
                            else
                                player2.handleTap(touchStartX, touchStartY);

                            touchEffect.resetAnimation();
                            touchEffect.setX(touchStartX - (float) touchEffect.getWidth() / 2);
                            touchEffect.setY(touchStartY - (float) touchEffect.getHeight() / 2);
                            touchEffect.resumeAnimation();
                        }
                    } else {
                        if (state == STATE.ONE_PLAYER)
                            ball.handleSwipe(1, touchStartX, touchStartY, releaseX, releaseY);
                        else if (state == STATE.TWO_PLAYERS) {
                            if (touchStartX < MainActivity.screenWidth / 2)
                                ball.handleSwipe(1, touchStartX, touchStartY, releaseX, releaseY);
                            else
                                ball.handleSwipe(2, touchStartX, touchStartY, releaseX, releaseY);
                        }
                    }
                    return true;
            }
        } else if (state == STATE.MAIN_MENU) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mainMenu.handleTouchEvent(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    MainMenu.resetTouch();
                    return true;
            }
        } else if (state == STATE.ONE_PLAYER_MENU) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onePlayerMenu.handleTouchEvent(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    OnePlayerMenu.resetTouch();
                    return true;
            }
        } else if (state == STATE.TWO_PLAYERS_MENU) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    twoPlayersMenu.handleTouchEvent(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    TwoPlayersMenu.resetTouch();
                    return true;
            }
        } else if (state == STATE.PAUSED_1P || state == STATE.PAUSED_2P) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pauseMenu.handleTouchEvent(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    PauseMenu.resetTouch();
                    return true;
            }
        } else if (state == STATE.SETTINGS) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    settingsMenu.handleTouchEvent(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    SettingsMenu.resetTouch();
                    return true;
            }
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
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //drawUPS(canvas);
        //drawFPS(canvas);

        switch (state) {
            case ONE_PLAYER:
                canvas.drawBitmap(tex.gameBackground, 0f, 0f, paint);
                player1.draw(canvas);
                aiPlayer.draw(canvas);
                canvas.drawBitmap(tex.backgroundFiller[0], leftGoal.getBounds().centerX() - leftGoal.getBounds().width() / 2, leftGoal.getBounds().centerY() - leftGoal.getBounds().height() / 2, paint);
                canvas.drawBitmap(tex.backgroundFiller[1], rightGoal.getBounds().centerX() - rightGoal.getBounds().width() / 2, rightGoal.getBounds().centerY() - rightGoal.getBounds().height() / 2, paint);
                ball.draw(canvas);
                leftGoal.draw(canvas);
                rightGoal.draw(canvas);
                gameMenu.draw(canvas);
                touchEffect.drawAnimation(canvas, paint);
                break;
            case TWO_PLAYERS:
                canvas.drawBitmap(tex.gameBackground, 0f, 0f, paint);
                player1.draw(canvas);
                player2.draw(canvas);
                canvas.drawBitmap(tex.backgroundFiller[0], leftGoal.getBounds().centerX() - leftGoal.getBounds().width() / 2, leftGoal.getBounds().centerY() - leftGoal.getBounds().height() / 2, paint);
                canvas.drawBitmap(tex.backgroundFiller[1], rightGoal.getBounds().centerX() - rightGoal.getBounds().width() / 2, rightGoal.getBounds().centerY() - rightGoal.getBounds().height() / 2, paint);
                ball.draw(canvas);
                leftGoal.draw(canvas);
                rightGoal.draw(canvas);
                gameMenu.draw(canvas);
                touchEffect.drawAnimation(canvas, paint);
                break;
            case MAIN_MENU:
                mainMenu.draw(canvas);
                break;
            case ONE_PLAYER_MENU:
                onePlayerMenu.draw(canvas);
                break;
            case TWO_PLAYERS_MENU:
                twoPlayersMenu.draw(canvas);
                break;
            case SETTINGS:
                settingsMenu.draw(canvas);
                break;
            case PAUSED_1P:
                canvas.drawBitmap(tex.gameBackground, 0f, 0f, paint);
                player1.draw(canvas);
                aiPlayer.draw(canvas);
                canvas.drawBitmap(tex.backgroundFiller[0], leftGoal.getBounds().centerX() - leftGoal.getBounds().width() / 2, leftGoal.getBounds().centerY() - leftGoal.getBounds().height() / 2, paint);
                canvas.drawBitmap(tex.backgroundFiller[1], rightGoal.getBounds().centerX() - rightGoal.getBounds().width() / 2, rightGoal.getBounds().centerY() - rightGoal.getBounds().height() / 2, paint);
                ball.draw(canvas);
                leftGoal.draw(canvas);
                rightGoal.draw(canvas);
                gameMenu.draw(canvas);
                touchEffect.drawAnimation(canvas, paint);
                pauseMenu.draw(canvas);
                break;
            case PAUSED_2P:
                canvas.drawBitmap(tex.gameBackground, 0f, 0f, paint);
                player1.draw(canvas);
                player2.draw(canvas);
                canvas.drawBitmap(tex.backgroundFiller[0], leftGoal.getBounds().centerX() - leftGoal.getBounds().width() / 2, leftGoal.getBounds().centerY() - leftGoal.getBounds().height() / 2, paint);
                canvas.drawBitmap(tex.backgroundFiller[1], rightGoal.getBounds().centerX() - rightGoal.getBounds().width() / 2, rightGoal.getBounds().centerY() - rightGoal.getBounds().height() / 2, paint);
                ball.draw(canvas);
                leftGoal.draw(canvas);
                rightGoal.draw(canvas);
                gameMenu.draw(canvas);
                touchEffect.drawAnimation(canvas, paint);
                pauseMenu.draw(canvas);
                break;
        }
    }

    public void update() {
        switch (state) {
            case ONE_PLAYER:
                player1.update();
                aiPlayer.update();
                ball.update();
                gameMenu.update();
                leftGoal.update();
                rightGoal.update();

                touchEffect.runAnimation();
                if (touchEffect.getPlayedOnce())
                    touchEffect.stopAnimation();
                break;
            case TWO_PLAYERS:
                player1.update();
                player2.update();
                ball.update();
                gameMenu.update();
                leftGoal.update();
                rightGoal.update();

                touchEffect.runAnimation();
                if (touchEffect.getPlayedOnce())
                    touchEffect.stopAnimation();
                break;
            case MAIN_MENU:
                mainMenu.update();
                break;
            case ONE_PLAYER_MENU:
                onePlayerMenu.update();
                break;
            case TWO_PLAYERS_MENU:
                twoPlayersMenu.update();
                break;
            case SETTINGS:
                settingsMenu.update();
                break;
            case PAUSED_1P:
            case PAUSED_2P:
                pauseMenu.update();
                break;
        }
    }

    /**
     * displays the number of updates
     */
    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    /**
     * displays the number of frames
     */
    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(36);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void pauseGame() {
        gameLoop.pauseLoop();
    }

}
