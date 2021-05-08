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

public class SettingsMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private GameData gameData = Game.getGameData();
    private Paint paint;
    private Context context;

    private static float touchX = -1f, touchY = -1f;

    private MediaPlayer menuClickSound;
    private int soundReleaseTimer = 15, soundLength = 15;

    public SettingsMenu(Context context, GameData gameData, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tex.menuBackground, 0, 0, paint);
        canvas.drawBitmap(tex.menuTitles[3], width / 3, height / 10, paint);
        canvas.drawBitmap(tex.otherButtons[0], getRectX(getBoundsBack()), getRectY(getBoundsBack()), paint);

        int music = gameData.isMusicOn() ? 0 : 1;
        int sound = gameData.isSoundOn() ? 2 : 3;
        canvas.drawBitmap(tex.settingsMenuButtons[music], getRectX(getBoundsMusic()), getRectY(getBoundsMusic()), paint);
        canvas.drawBitmap(tex.settingsMenuButtons[sound], getRectX(getBoundsSound()), getRectY(getBoundsSound()), paint);
    }

    @Override
    public void update() {
        if (touchX != -1 && touchY != -1) {
            if (getBoundsBack().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                Game.state = Game.STATE.MAIN_MENU;
                MainMenu.resetTouch();
                resetTouch();
            }
            if (getBoundsMusic().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                gameData.setIsMusicOn(!gameData.isMusicOn());
                resetTouch();
            }
            if (getBoundsSound().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                gameData.setIsSoundOn(!gameData.isSoundOn());
                resetTouch();
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

    private Rect getBoundsMusic() {
        return createRect((7 * width - 4 * height) / 16, 3 * height / 8 + height / 16, height / 4, height / 4);
    }

    private Rect getBoundsSound() {
        return createRect((7 * width - 4 * height) / 16 + height / 4 + width / 8, 3 * height / 8 + height / 16, height / 4, height / 4);
    }

    private Rect getBoundsBack() {
        return createRect(15, 50, width / 8, height / 8);
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
