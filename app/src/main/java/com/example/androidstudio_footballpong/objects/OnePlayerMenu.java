package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.GameData;
import com.example.androidstudio_footballpong.GameLoop;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

public class OnePlayerMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private GameData gameData = Game.getGameData();
    private Paint paint;
    private Context context;

    private AIPlayer aiPlayer;

    private static float touchX = -1f, touchY = -1f;

    private int selectedDifficulty = GameData.DIFFICULTY_MEDIUM;
    private int selectedTime = 2;

    private MediaPlayer menuClickSound;
    private int soundReleaseTimer = 15, soundLength = 15;

    public OnePlayerMenu(Context context, AIPlayer aiPlayer, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.aiPlayer = aiPlayer;
        this.context = context;
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tex.menuBackground, 0, 0, paint);
        canvas.drawBitmap(tex.menuTitles[1], width / 3, height / 10, paint);
        canvas.drawBitmap(tex.otherButtons[0], getRectX(getBoundsBack()), getRectY(getBoundsBack()), paint);
        canvas.drawBitmap(tex.otherButtons[1], getRectX(getBoundsStart()), getRectY(getBoundsStart()), paint);

        if (selectedDifficulty == GameData.DIFFICULTY_EASY) {
            canvas.drawBitmap(tex.onePlayerMenuButtons[3], getRectX(getBoundsEasy()), getRectY(getBoundsEasy()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[1], getRectX(getBoundsMedium()), getRectY(getBoundsMedium()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[2], getRectX(getBoundsHard()), getRectY(getBoundsHard()), paint);
        } else if (selectedDifficulty == GameData.DIFFICULTY_MEDIUM) {
            canvas.drawBitmap(tex.onePlayerMenuButtons[0], getRectX(getBoundsEasy()), getRectY(getBoundsEasy()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[4], getRectX(getBoundsMedium()), getRectY(getBoundsMedium()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[2], getRectX(getBoundsHard()), getRectY(getBoundsHard()), paint);
        } else if (selectedDifficulty == GameData.DIFFICULTY_HARD) {
            canvas.drawBitmap(tex.onePlayerMenuButtons[0], getRectX(getBoundsEasy()), getRectY(getBoundsEasy()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[1], getRectX(getBoundsMedium()), getRectY(getBoundsMedium()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[5], getRectX(getBoundsHard()), getRectY(getBoundsHard()), paint);
        }

        if (selectedTime == 1) {
            canvas.drawBitmap(tex.onePlayerMenuButtons[9], getRectX(getBoundsLength1()), getRectY(getBoundsLength1()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[7], getRectX(getBoundsLength2()), getRectY(getBoundsLength2()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[8], getRectX(getBoundsLength3()), getRectY(getBoundsLength3()), paint);
        } else if (selectedTime == 2) {
            canvas.drawBitmap(tex.onePlayerMenuButtons[6], getRectX(getBoundsLength1()), getRectY(getBoundsLength1()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[10], getRectX(getBoundsLength2()), getRectY(getBoundsLength2()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[8], getRectX(getBoundsLength3()), getRectY(getBoundsLength3()), paint);
        } else {
            canvas.drawBitmap(tex.onePlayerMenuButtons[6], getRectX(getBoundsLength1()), getRectY(getBoundsLength1()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[7], getRectX(getBoundsLength2()), getRectY(getBoundsLength2()), paint);
            canvas.drawBitmap(tex.onePlayerMenuButtons[11], getRectX(getBoundsLength3()), getRectY(getBoundsLength3()), paint);
        }

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
            }

            if (getBoundsStart().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                gameData.setDifficulty(selectedDifficulty);

                if (selectedDifficulty == GameData.DIFFICULTY_EASY)
                    aiPlayer.setMaxSpeed(AIPlayer.DEFAULT_MAX_SPEED_EASY);
                else if (selectedDifficulty == GameData.DIFFICULTY_MEDIUM)
                    aiPlayer.setMaxSpeed(AIPlayer.DEFAULT_MAX_SPEED_MEDIUM);
                else
                    aiPlayer.setMaxSpeed(AIPlayer.DEFAULT_MAX_SPEED_HARD);

                gameData.setSelectedGameLength(selectedTime);
                if (selectedTime == 1) {
                    gameData.setGameTimer(0, 10);
                    Player1.setMaxEnergy(calculateMaxEnergy(3, 0));
                    AIPlayer.setMaxEnergy(calculateMaxEnergy(3, 0));
                } else if (selectedTime == 2) {
                    gameData.setGameTimer(5, 0);
                    Player1.setMaxEnergy(calculateMaxEnergy(5, 0));
                    AIPlayer.setMaxEnergy(calculateMaxEnergy(5, 0));
                } else {
                    gameData.setGameTimer(8, 0);
                    Player1.setMaxEnergy(calculateMaxEnergy(8, 0));
                    AIPlayer.setMaxEnergy(calculateMaxEnergy(8, 0));
                }

                Game.state = Game.STATE.ONE_PLAYER;
            }

            if (getBoundsEasy().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                selectedDifficulty = GameData.DIFFICULTY_EASY;
            }
            if (getBoundsMedium().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                selectedDifficulty = GameData.DIFFICULTY_MEDIUM;
            }
            if (getBoundsHard().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                selectedDifficulty = GameData.DIFFICULTY_HARD;
            }

            if (getBoundsLength1().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                selectedTime = 1;
            }
            if (getBoundsLength2().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                selectedTime = 2;
            }
            if (getBoundsLength3().contains((int) touchX, (int) touchY)) {
                if (menuClickSound == null) {
                    menuClickSound = MediaPlayer.create(context, R.raw.click);
                    soundReleaseTimer = 0;
                    menuClickSound.start();
                }

                selectedTime = 3;
            }
        }

        if (soundReleaseTimer < soundLength)
            soundReleaseTimer++;
        else
            releaseMediaPlayer();
    }

    private int calculateMaxEnergy(int minutes, int seconds) {
        int time = minutes * 60 + seconds;
        return (int) (time * GameLoop.MAX_UPS / 5);
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

    private Rect getBoundsBack() {
        return createRect(15, 50, width / 8, height / 8);
    }

    private Rect getBoundsEasy() {
        return createRect(width / 6, 23 * height / 80, 2 * width / 9, height / 5);
    }

    private Rect getBoundsMedium() {
        return createRect(width / 6 + 2 * width / 9, 23 * height / 80, 2 * width / 9, height / 5);
    }

    private Rect getBoundsHard() {
        return createRect(width / 6 + 4 * width / 9, 23 * height / 80, 2 * width / 9, height / 5);
    }

    private Rect getBoundsLength1() {
        return createRect(width / 6, 42 * height / 80, 2 * width / 9, height / 5);
    }

    private Rect getBoundsLength2() {
        return createRect(width / 6 + 2 * width / 9, 42 * height / 80, 2 * width / 9, height / 5);
    }

    private Rect getBoundsLength3() {
        return createRect(width / 6 + 4 * width / 9, 42 * height / 80, 2 * width / 9, height / 5);
    }

    private Rect getBoundsStart() {
        return createRect(3 * width / 8, 42 * height / 80 + 9 * height / 156 + height / 5, width / 4, height / 6);
    }

    private int getRectX(Rect rect) {
        return rect.centerX() - rect.width() / 2;
    }

    private int getRectY(Rect rect) {
        return rect.centerY() - rect.height() / 2;
    }

    @Override
    public Rect getBounds() {
        return null;
    }
}
