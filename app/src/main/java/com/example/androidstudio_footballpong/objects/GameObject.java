package com.example.androidstudio_footballpong.objects;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * GameObject is the parent of each class in the game which needs updating and rendering.
 */
public abstract class GameObject {

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double velX = 0;
    protected double velY = 0;

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();
    public abstract Rect getBounds();

    public Rect getBoundsExact() {
        return createRect((int) x, (int) y, width, height);
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return  velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * For creating Rects in a simpler way, similar to java.awt style.
     * @param x x coordinate of the top left corner
     * @param y y coordinate of the top left corner
     * @param width width of the Rect, originating from the top left corner
     * @param height height of the Rect, originating from the top left corner
     */
    public Rect createRect(int x, int y, int width, int height) {
        return new Rect(x, y, x + width, y + height);
    }

}
