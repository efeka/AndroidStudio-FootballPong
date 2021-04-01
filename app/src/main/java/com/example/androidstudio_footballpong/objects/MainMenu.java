package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

/**
 * This menu is displayed when the app is opened.
 * Any other menu in the game can be navigated to from this menu.
 */
public class MainMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Context context;
    private Paint paint;

    private static float touchX = -1f, touchY = -1f;

    //placeholder graphics
    int[] colors = new int[3];

    public MainMenu(Context context, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.context = context;
        paint = new Paint();

        colors[0] = ContextCompat.getColor(context, R.color.teal_200);
        colors[1] = ContextCompat.getColor(context, R.color.white);
        colors[2] = ContextCompat.getColor(context, R.color.black);
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(colors[0]);
        canvas.drawRect(createRect(0, 0, width, height), paint);

        paint.setColor(colors[2]);
        canvas.drawRect(getBounds1P(), paint);
        canvas.drawRect(getBounds2P(), paint);
        canvas.drawRect(getBoundsSettings(), paint);

        paint.setColor(colors[0]);
        paint.setTextSize(108);
        canvas.drawText("1 Player",width / 9, getBounds1P().centerY(), paint);
        canvas.drawText("2 Players", 5 * width / 9, getBounds2P().centerY(), paint);
        canvas.drawText("Settings", width / 3, getBoundsSettings().centerY(), paint);

        paint.setColor(colors[1]);
        paint.setTextSize(216);
        canvas.drawText("Football Pong", 0, height / 4, paint);
    }

    @Override
    public void update() {
        if (touchX != -1 && touchY != -1) {
            if (getBounds1P().contains((int) touchX, (int) touchY)) {
                Game.state = Game.STATE.ONE_PLAYER;
            }
            if (getBounds2P().contains((int) touchX, (int) touchY)) {
                Game.state = Game.STATE.TWO_PLAYERS;
            }
            if (getBoundsSettings().contains((int) touchX, (int) touchY)) {
                Game.state = Game.STATE.SETTINGS;
            }
        }
    }

    public void handleTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchX = touchY = 0;
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
}
