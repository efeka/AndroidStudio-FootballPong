package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

public class GameMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Paint paint;

    private Player1 player1;

    private int[] energyColors = new int[3];

    public GameMenu(Context context, double x, double y, Player1 player1) {
        super(x, y);
        this.player1 = player1;

        paint = new Paint();
        energyColors[0] = ContextCompat.getColor(context, R.color.energy_border);
        energyColors[1] = ContextCompat.getColor(context, R.color.energy_background);
        energyColors[2] = ContextCompat.getColor(context, R.color.energy_bar);
    }

    @Override
    public void draw(Canvas canvas) {
        //pause button
        canvas.drawBitmap(tex.gameMenu[0], (float) x, (float) y, paint);

        //player1 energy
        int rectRadius = 10;
        paint.setColor(energyColors[0]);
        canvas.drawRoundRect(getEnergy1Border(), rectRadius, rectRadius, paint);
        paint.setColor(energyColors[1]);
        canvas.drawRoundRect(getEnergy1Background(), rectRadius, rectRadius, paint);
        paint.setColor(energyColors[2]);
        canvas.drawRoundRect(getEnergy1Bar(), rectRadius, rectRadius, paint);
    }

    @Override
    public void update() {

    }

    @Override
    public Rect getBounds() {
        return null;
    }

    public RectF getEnergy1Border() {
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        return createRectF(MainActivity.screenWidth / 40, (int) y + height / 2, width, height);
    }

    public RectF getEnergy1Background() {
        int offset = 8;
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        return createRectF(MainActivity.screenWidth / 40 + offset / 2, (int) y + height / 2 + offset / 2, width - offset, height - offset);
    }

    public RectF getEnergy1Bar() {
        int offset = 8;
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        double normalize = (double) (width - offset) / player1.getMaxEnergy();
        return createRectF(MainActivity.screenWidth / 40 + offset / 2, (int) y + height / 2 + offset / 2, (int) (normalize * player1.getEnergy()), height - offset);
    }

    public Rect createRect(int x, int y, int width, int height) {
        return new Rect(x, y, x + width, y + height);
    }

    public RectF createRectF(int x, int y, int width, int height) {
        return new RectF(x, y, x + width, y + height);
    }

}
