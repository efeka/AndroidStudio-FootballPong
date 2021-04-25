package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.Game;
import com.example.androidstudio_footballpong.GameData;
import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;
import com.example.androidstudio_footballpong.Texture;

public class GameMenu extends GameObject {

    private Texture tex = Game.getTexture();
    private Paint paint;

    private Player1 player1;
    private Player2 player2;
    private AIPlayer aiPlayer;
    private GameData gameData;

    private int updateCount = 0;

    private int[] energyColors = new int[3];
    private int textColor;

    public GameMenu(Context context, Player1 player1, Player2 player2, AIPlayer aiPlayer, GameData gameData, double x, double y) {
        super(x, y);
        this.player1 = player1;
        this.player2 = player2;
        this.aiPlayer = aiPlayer;
        this.gameData = gameData;

        paint = new Paint();
        energyColors[0] = ContextCompat.getColor(context, R.color.energy_border);
        energyColors[1] = ContextCompat.getColor(context, R.color.energy_background);
        energyColors[2] = ContextCompat.getColor(context, R.color.energy_bar);
        textColor = ContextCompat.getColor(context, R.color.white);
    }

    @Override
    public void draw(Canvas canvas) {
        //pause button
        canvas.drawBitmap(tex.gameMenu[0], (float) x, (float) y, paint);

        //Player1 energy
        int rectRadius = 10;
        paint.setColor(energyColors[0]);
        canvas.drawRoundRect(getEnergy1Border(), rectRadius, rectRadius, paint);
        paint.setColor(energyColors[1]);
        canvas.drawRoundRect(getEnergy1Background(), rectRadius, rectRadius, paint);
        paint.setColor(energyColors[2]);
        canvas.drawRoundRect(getEnergy1Bar(), rectRadius, rectRadius, paint);

        //Scores & Game timer
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(textColor);
        paint.setTextSize(100);
        RectF button = getPauseButtonBorder();
        canvas.drawText(gameData.getScore1() + "", button.centerX() - button.width() / 2 - 15, button.centerY() + 25, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(gameData.getScore2() + "", button.centerX() + button.width() / 2 + 10, button.centerY() + 25, paint);
        paint.setTextSize(70);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(gameData.getTimerDisplay(), button.centerX(), button.centerY() + button.height(), paint);


        if (Game.state == Game.STATE.ONE_PLAYER) {
            //AIPlayer energy
            rectRadius = 10;
            paint.setColor(energyColors[0]);
            canvas.drawRoundRect(getEnergyAIBorder(), rectRadius, rectRadius, paint);
            paint.setColor(energyColors[1]);
            canvas.drawRoundRect(getEnergyAIBackground(), rectRadius, rectRadius, paint);
            paint.setColor(energyColors[2]);
            canvas.drawRoundRect(getEnergyAIBar(), rectRadius, rectRadius, paint);
        }
        else if (Game.state == Game.STATE.TWO_PLAYERS) {
            //Player2 energy
            rectRadius = 10;
            paint.setColor(energyColors[0]);
            canvas.drawRoundRect(getEnergy2Border(), rectRadius, rectRadius, paint);
            paint.setColor(energyColors[1]);
            canvas.drawRoundRect(getEnergy2Background(), rectRadius, rectRadius, paint);
            paint.setColor(energyColors[2]);
            canvas.drawRoundRect(getEnergy2Bar(), rectRadius, rectRadius, paint);
        }
    }

    @Override
    public void update() {
        if (updateCount < 30)
            updateCount++;
        else {
            updateCount = 0;
            gameData.decrementTimer(1);
        }
    }

    @Override
    public Rect getBounds() {
        return null;
    }

    public RectF getPauseButtonBorder() {
        int width = MainActivity.screenWidth / 14;
        return createRectF(MainActivity.screenWidth / 2 - width / 2, (int) y, width, width);
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

    public RectF getEnergy2Border() {
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        return createRectF(MainActivity.screenWidth - MainActivity.screenWidth / 40 - width, (int) y + height / 2, width, height);
    }

    public RectF getEnergy2Background() {
        int offset = 8;
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        return createRectF(MainActivity.screenWidth - MainActivity.screenWidth / 40 - width + offset / 2, (int) y + height / 2 + offset / 2, width - offset, height - offset);
    }

    public RectF getEnergy2Bar() {
        int offset = 8;
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        double normalize = (double) (width - offset) / player2.getMaxEnergy();
        return createRectF(MainActivity.screenWidth - MainActivity.screenWidth / 40 - width + offset / 2 + (int) (normalize * player2.getMaxEnergy() - normalize * player2.getEnergy()), (int) y + height / 2 + offset / 2, (int) (normalize * player2.getEnergy()), height - offset);
    }

    public RectF getEnergyAIBorder() {
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        return createRectF(MainActivity.screenWidth - MainActivity.screenWidth / 40 - width, (int) y + height / 2, width, height);
    }

    public RectF getEnergyAIBackground() {
        int offset = 8;
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        return createRectF(MainActivity.screenWidth - MainActivity.screenWidth / 40 - width + offset / 2, (int) y + height / 2 + offset / 2, width - offset, height - offset);
    }

    public RectF getEnergyAIBar() {
        int offset = 8;
        int width = MainActivity.screenWidth / 6;
        int height = MainActivity.screenHeight / 16;
        double normalize = (double) (width - offset) / aiPlayer.getMaxEnergy();
        return createRectF(MainActivity.screenWidth - MainActivity.screenWidth / 40 - width + offset / 2 + (int) (normalize * aiPlayer.getMaxEnergy() - normalize * aiPlayer.getEnergy()), (int) y + height / 2 + offset / 2, (int) (normalize * aiPlayer.getEnergy()), height - offset);
    }

    public RectF createRectF(int x, int y, int width, int height) {
        return new RectF(x, y, x + width, y + height);
    }

}
