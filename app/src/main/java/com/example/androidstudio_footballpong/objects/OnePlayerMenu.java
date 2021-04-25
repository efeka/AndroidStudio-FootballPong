package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.GameData;
import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

public class OnePlayerMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private GameData gameData = Game.getGameData();
    private Context context;
    private Paint paint;

    private static float touchX = -1f, touchY = -1f;

    private int selectedDifficulty = GameData.DIFFICULTY_MEDIUM;
    private int selectedTime = 2;

    //temporary
    int[] colors = new int[3];

    public OnePlayerMenu(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;

        paint = new Paint();
        colors[0] = ContextCompat.getColor(context, R.color.teal_200);
        colors[1] = ContextCompat.getColor(context, R.color.white);
        colors[2] = ContextCompat.getColor(context, R.color.black);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(tex.menuBackground, 0, 0, paint);

        paint.setColor(colors[2]);
        canvas.drawRect(getBoundsBack(), paint);
        canvas.drawRect(getBoundsStart(), paint);

        if (selectedDifficulty == GameData.DIFFICULTY_EASY) {
            paint.setColor(colors[1]);
            canvas.drawRect(getBoundsEasy(), paint);
            paint.setColor(colors[2]);
            canvas.drawRect(getBoundsMedium(), paint);
            canvas.drawRect(getBoundsHard(), paint);
        } else if (selectedDifficulty == GameData.DIFFICULTY_MEDIUM) {
            paint.setColor(colors[1]);
            canvas.drawRect(getBoundsMedium(), paint);
            paint.setColor(colors[2]);
            canvas.drawRect(getBoundsEasy(), paint);
            canvas.drawRect(getBoundsHard(), paint);
        } else if (selectedDifficulty == GameData.DIFFICULTY_HARD) {
            paint.setColor(colors[1]);
            canvas.drawRect(getBoundsHard(), paint);
            paint.setColor(colors[2]);
            canvas.drawRect(getBoundsMedium(), paint);
            canvas.drawRect(getBoundsEasy(), paint);
        }

        if (selectedTime == 1) {
            paint.setColor(colors[1]);
            canvas.drawRect(getBoundsLength1(), paint);
            paint.setColor(colors[2]);
            canvas.drawRect(getBoundsLength2(), paint);
            canvas.drawRect(getBoundsLength3(), paint);
        } else if (selectedTime == 2) {
            paint.setColor(colors[1]);
            canvas.drawRect(getBoundsLength2(), paint);
            paint.setColor(colors[2]);
            canvas.drawRect(getBoundsLength1(), paint);
            canvas.drawRect(getBoundsLength3(), paint);
        } else {
            paint.setColor(colors[1]);
            canvas.drawRect(getBoundsLength3(), paint);
            paint.setColor(colors[2]);
            canvas.drawRect(getBoundsLength2(), paint);
            canvas.drawRect(getBoundsLength1(), paint);
        }

        canvas.drawBitmap(tex.menuTitles[1], width / 3, height / 10, paint);

        paint.setColor(colors[0]);
        paint.setTextSize(108);
        canvas.drawText("Back", 10, getBoundsBack().centerY(), paint);
        canvas.drawText("Easy", getBoundsEasy().centerX() - getBoundsEasy().width() / 2, getBoundsEasy().centerY(), paint);
        canvas.drawText("Medium", getBoundsMedium().centerX() - getBoundsMedium().width() / 2, getBoundsMedium().centerY(), paint);
        canvas.drawText("Hard", getBoundsHard().centerX() - getBoundsHard().width() / 2, getBoundsHard().centerY(), paint);
        canvas.drawText("3 min.", getBoundsLength1().centerX() - getBoundsLength1().width() / 2, getBoundsLength1().centerY(), paint);
        canvas.drawText("8 min.", getBoundsLength2().centerX() - getBoundsLength2().width() / 2, getBoundsLength2().centerY(), paint);
        canvas.drawText("10 min.", getBoundsLength3().centerX() - getBoundsLength3().width() / 2, getBoundsLength3().centerY(), paint);
    }

    @Override
    public void update() {
        if (touchX != -1 && touchY != -1) {
            if (getBoundsBack().contains((int) touchX, (int) touchY)) {
                Game.state = Game.STATE.MAIN_MENU;
                MainMenu.resetTouch();
            }
            if (getBoundsStart().contains((int) touchX, (int) touchY)) {
                Game.state = Game.STATE.ONE_PLAYER;
                gameData.setDifficulty(selectedDifficulty);
                if (selectedTime == 1)
                    gameData.setGameTimer(3, 0);
                else if (selectedTime == 2)
                    gameData.setGameTimer(8, 0);
                else
                    gameData.setGameTimer(10, 0);
            }
            if (getBoundsEasy().contains((int) touchX, (int) touchY))
                selectedDifficulty = GameData.DIFFICULTY_EASY;
            if (getBoundsMedium().contains((int) touchX, (int) touchY))
                selectedDifficulty = GameData.DIFFICULTY_MEDIUM;
            if (getBoundsHard().contains((int) touchX, (int) touchY))
                selectedDifficulty = GameData.DIFFICULTY_HARD;
            if (getBoundsLength1().contains((int) touchX, (int) touchY))
                selectedTime = 1;
            if (getBoundsLength2().contains((int) touchX, (int) touchY))
                selectedTime = 2;
            if (getBoundsLength3().contains((int) touchX, (int) touchY))
                selectedTime = 3;
        }
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

    private Rect getBoundsBack() {
        return createRect(10, 10, width / 8, height / 8);
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

    @Override
    public Rect getBounds() {
        return null;
    }
}
