package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.GameData;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

public class PauseMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Paint paint;
    private Context context;

    private GameData gameData;
    private Player1 player1;
    private Player2 player2;
    private AIPlayer aiPlayer;
    private Ball ball;

    private static float touchX = -1f, touchY = -1f;

    private MediaPlayer menuClickSound;
    private int soundReleaseTimer = 15, soundLength = 15;

    public PauseMenu(Context context, GameData gameData, Player1 player1, Player2 player2, AIPlayer aiPlayer, Ball ball, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.gameData = gameData;
        this.player1 = player1;
        this.player2 = player2;
        this.aiPlayer = aiPlayer;
        this.ball = ball;
        this.context = context;

        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        if (Game.state == Game.STATE.PAUSED_1P || Game.state == Game.STATE.PAUSED_2P) {
            if (gameData.getGameTimer() > 0)
                canvas.drawBitmap(tex.pauseMenuButtons[0], getRectX(getBoundsResume()), getRectY(getBoundsResume()), paint);
            else
                canvas.drawBitmap(tex.pauseMenuButtons[2], getRectX(getBoundsResume()), getRectY(getBoundsResume()), paint);
            canvas.drawBitmap(tex.pauseMenuButtons[1], getRectX(getBoundsMainMenu()), getRectY(getBoundsMainMenu()), paint);
        }
    }

    @Override
    public void update() {
        if (touchX != -1 && touchY != -1) {
            if (gameData.getGameTimer() > 0 && getBoundsResume().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                resetTouch();
                if (Game.state == Game.STATE.PAUSED_1P)
                    Game.state = Game.STATE.ONE_PLAYER;
                else if (Game.state == Game.STATE.PAUSED_2P)
                    Game.state = Game.STATE.TWO_PLAYERS;
            }
            if (getBoundsMainMenu().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                resetGame();
                MainMenu.resetTouch();
                Game.state = Game.STATE.MAIN_MENU;
            }
            if (gameData.getGameTimer() <= 0 && getBoundsRestart().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                resetGame();
                if (Game.state == Game.STATE.PAUSED_1P)
                    Game.state = Game.STATE.ONE_PLAYER;
                else if (Game.state == Game.STATE.PAUSED_2P)
                    Game.state = Game.STATE.TWO_PLAYERS;
            }
        }

        if (soundReleaseTimer < soundLength)
            soundReleaseTimer++;
        else
            releaseMediaPlayer();
    }

    private void resetGame() {
        player1.reset();
        player2.reset();
        aiPlayer.reset();
        gameData.resetScores();
        gameData.resetGameTimer();
        int randomDirection = (int) (Math.random() * 2);
        ball.resetBall(randomDirection);
        resetTouch();
    }

    public void handleTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchX = touchY = -1;
                break;
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                break;
        }
    }

    /**
     * To stop the user from accidentally clicking this menu's buttons during menu transitions.
     */
    public static void resetTouch() {
        touchX = touchY = -1;
    }

    private void releaseMediaPlayer() {
        try {
            if (menuClickSound != null) {
                if (menuClickSound.isPlaying())
                    menuClickSound.stop();
                menuClickSound.release();
                menuClickSound = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Rect getBounds() {
        return null;
    }

    private Rect getBoundsResume() {
        return createRect(width / 3, 3 * height / 10 - 10, width / 3, height / 5);
    }

    private Rect getBoundsRestart() {
        return createRect(width / 3, 3 * height / 10 - 10, width / 3, height / 5);
    }

    private Rect getBoundsMainMenu() {
        return createRect(width / 3, 5 * height / 10 + 10, width / 3, height / 5);
    }

    private int getRectX(Rect rect) {
        return rect.centerX() - rect.width() / 2;
    }

    private int getRectY(Rect rect) {
        return rect.centerY() - rect.height() / 2;
    }

}
