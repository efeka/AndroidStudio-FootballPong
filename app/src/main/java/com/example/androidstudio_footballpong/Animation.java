package com.example.androidstudio_footballpong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This class if for drawing sprite animations on the screen.
 */
public class Animation {

    private int speed;
    private int frames;

    private int index;
    private int frameCount;

    private Bitmap[] sprites;
    private Bitmap currentSprite;

    private boolean playedOnce = false;
    private boolean stopped = false;

    private float x = -1, y = -1;

    /**
     * @param speed Number of frames to skip before drawing the next sprite.
     * @param args Each sprite of the animation in order.
     */
    public Animation(int speed, Bitmap... args) {
        this.speed = speed;

        sprites = new Bitmap[args.length];
        for (int i = 0; i < args.length; i++)
            sprites[i] = args[i];
        frames = args.length;
    }

    public void runAnimation() {
        if (!stopped) {
            index++;
            if (index > speed) {
                index = 0;
                nextFrame();
            }
        }
    }

    private void nextFrame() {
        playedOnce = false;
        for (int i = 0; i < frames; i++) {
            if (frameCount == i)
                currentSprite = sprites[i];
        }
        if (!playedOnce)
            frameCount++;
        if (frameCount >= frames) {
            frameCount = 0;
            playedOnce = true;
        }
    }

    public void drawAnimation(Canvas canvas, Paint paint, float x, float y) {
        if (!stopped) {
            try {
                canvas.drawBitmap(currentSprite, x, y, paint);
            } catch (NullPointerException e) {
            }
        }
    }

    public void drawAnimation(Canvas canvas, Paint paint) {
        if (!stopped && x != -1 && y != -1) {
            try {
                canvas.drawBitmap(currentSprite, x, y, paint);
            } catch (NullPointerException e) {
            }
        }
    }

    public void resetAnimation() {
        index = 0;
        frameCount = 0;
        nextFrame();
        playedOnce = false;
    }

    public void stopAnimation() {
        stopped = true;
    }

    public void resumeAnimation() {
        stopped = false;
    }

    public boolean getPlayedOnce() {
        return playedOnce;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return currentSprite.getWidth();
    }

    public int getHeight() {
        return currentSprite.getHeight();
    }

}
