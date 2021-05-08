package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

/**
 * This menu is displayed when the app is opened.
 * Any other menu in the game can be navigated to from this menu.
 */
public class MainMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Paint paint;
    private Context context;

    private static float touchX = -1f, touchY = -1f;

    private MediaPlayer menuClickSound;
    private int soundReleaseTimer = 15, soundLength = 15;

    public MainMenu(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tex.menuBackground, 0, 0, paint);

        canvas.drawBitmap(tex.mainMenuButtons[0], getRectX(getBounds1P()), getRectY(getBounds1P()), paint);
        canvas.drawBitmap(tex.mainMenuButtons[1], getRectX(getBounds2P()), getRectY(getBounds2P()), paint);
        canvas.drawBitmap(tex.mainMenuButtons[2], getRectX(getBoundsSettings()), getRectY(getBoundsSettings()), paint);

        canvas.drawBitmap(tex.menuTitles[0], width / 6, height / 7, paint);
    }

    @Override
    public void update() {
        if (touchX != -1 && touchY != -1) {
            if (getBounds1P().contains((int) touchX, (int) touchY)) {
                menuClickSound = MediaPlayer.create(context, R.raw.click);
                soundReleaseTimer = 0;
                menuClickSound.start();

                Game.state = Game.STATE.ONE_PLAYER_MENU;
                OnePlayerMenu.resetTouch();
            }
            if (getBounds2P().contains((int) touchX, (int) touchY)) {
                menuClickSound = MediaPlayer.create(context, R.raw.click);
                soundReleaseTimer = 0;
                menuClickSound.start();

                Game.state = Game.STATE.TWO_PLAYERS_MENU;
                TwoPlayersMenu.resetTouch();
            }
            if (getBoundsSettings().contains((int) touchX, (int) touchY)) {
                menuClickSound = MediaPlayer.create(context, R.raw.click);
                soundReleaseTimer = 0;
                menuClickSound.start();

                Game.state = Game.STATE.SETTINGS;
            }
        }

        if (soundReleaseTimer < soundLength)
            soundReleaseTimer++;
        else
            releaseMediaPlayer();
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

    private Rect getBounds1P() {
        return createRect(width / 9, height / 2, width / 3, height / 5);
    }

    private Rect getBounds2P() {
        return createRect(5 * width / 9, height / 2, width / 3, height / 5);
    }

    private Rect getBoundsSettings() {
        return createRect(width / 3, 3 * height / 4, width / 3, height / 5);
    }

    @Override
    public Rect getBounds() {
        return null;
    }

    private int getRectX(Rect rect) {
        return rect.centerX() - rect.width() / 2;
    }

    private int getRectY(Rect rect) {
        return rect.centerY() - rect.height() / 2;
    }
}
