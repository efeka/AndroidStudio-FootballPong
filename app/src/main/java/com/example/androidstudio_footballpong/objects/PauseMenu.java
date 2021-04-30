package com.example.androidstudio_footballpong.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.GameData;
import com.example.androidstudio_footballpong.Texture;

public class PauseMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Paint paint;

    private static float touchX = -1f, touchY = -1f;

    public PauseMenu(double x, double y, int width, int height) {
        super(x, y, width, height);
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

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

    @Override
    public Rect getBounds() {
        return null;
    }
}
