package com.example.androidstudio_footballpong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {

    private int speed;
    private int frames;

    private int index;
    private int frameCount;

    private Bitmap[] sprites;
    private Bitmap currentSprite;

    private boolean playedOnce = false;

    public Animation(int speed, Bitmap ... args) {
        this.speed = speed;

        sprites = new Bitmap[args.length];
        for (int i = 0; i < args.length; i++)
            sprites[i] = args[i];
        frames = args.length;
    }

    public void runAnimation() {
        index++;
        if (index > speed) {
            index = 0;
            nextFrame();
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
        if (frameCount > frames) {
            frameCount = 0;
            playedOnce = true;
        }
    }

    public void drawAnimation(Canvas canvas, Paint paint, float x, float y) {
        canvas.drawBitmap(currentSprite, x, y, paint);
    }

    public boolean getPlayedOnce() {
        return playedOnce;
    }

}
