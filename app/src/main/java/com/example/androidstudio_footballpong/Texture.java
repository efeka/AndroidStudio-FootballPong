package com.example.androidstudio_footballpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * This class is for getting image files as Bitmaps.
 * It prepares the Bitmaps by scaling and cropping so that they can be easily used in other classes.
 */
public class Texture {

    private Bitmap background_sheet = null;
    private Bitmap assets_sheet = null;
    private Bitmap game_menu = null;
    private Bitmap effect_sheet = null;

    public Bitmap[] gameMenu = new Bitmap[5];
    public Bitmap gameBackground;
    public Bitmap[] player1 = new Bitmap[8];
    public Bitmap[] player2 = new Bitmap[8];
    public Bitmap[] touchEffect = new Bitmap[8];
    public Bitmap[] goals = new Bitmap[2];
    public Bitmap ball;

    public Texture(Context context) {
        try {
            background_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

            game_menu = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_menu);
            game_menu = game_menu.createScaledBitmap(game_menu, 500, 500, false);

            assets_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.assets_sheet);
            assets_sheet = assets_sheet.createScaledBitmap(assets_sheet, 800, 600, false);

            effect_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.effect_sheet);
            effect_sheet = effect_sheet.createScaledBitmap(effect_sheet, 800, 600, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        getTextures();
    }

    private void getTextures() {
        //in game background
        gameBackground = background_sheet.createScaledBitmap(background_sheet, MainActivity.screenWidth, MainActivity.screenHeight, false);

        for (int i = 0; i < player1.length; i++) {
            player1[i] = Bitmap.createBitmap(assets_sheet, 1 + 49 * i, 102, 48, 48);
            player1[i] = player1[i].createScaledBitmap(player1[i], MainActivity.screenHeight / 6, MainActivity.screenWidth / 12, false);
        }
        for (int i = 0; i < player2.length; i++) {
            player2[i] = Bitmap.createBitmap(assets_sheet, 1 + 49 * i, 151, 48, 48);
            player2[i] = player2[i].createScaledBitmap(player2[i], MainActivity.screenHeight / 6, MainActivity.screenWidth / 12, false);
        }

        //in game pause button
        gameMenu[0] = Bitmap.createBitmap(game_menu, 1, 1, 100, 100);
        gameMenu[0] = gameMenu[0].createScaledBitmap(gameMenu[0], MainActivity.screenWidth / 14, MainActivity.screenWidth / 14, false);

        for (int i = 0; i < touchEffect.length; i++) {
            touchEffect[i] = Bitmap.createBitmap(effect_sheet, 1 + 65 * i, 1, 64, 64);
            touchEffect[i] = touchEffect[i].createScaledBitmap(touchEffect[i], MainActivity.screenHeight / 10, MainActivity.screenHeight / 10, false);
        }

        goals[0] = Bitmap.createBitmap(assets_sheet, 1, 1, 32, 100);
        goals[0] = goals[0].createScaledBitmap(goals[0], 100, 2 * MainActivity.screenHeight / 7, false);
        goals[1] = Bitmap.createBitmap(assets_sheet, 34, 1, 32, 100);
        goals[1] = goals[1].createScaledBitmap(goals[1], 100, 2 * MainActivity.screenHeight / 7, false);

        ball = Bitmap.createBitmap(assets_sheet, 67, 1, 100, 100);
        ball = ball.createScaledBitmap(ball, MainActivity.screenWidth / 25, MainActivity.screenWidth / 25, false);
    }

}
