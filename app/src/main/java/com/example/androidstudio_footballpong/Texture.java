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
    private Bitmap player_sheet = null;
    private Bitmap game_menu = null;
    private Bitmap effect_sheet = null;

    public Bitmap[] gameMenu = new Bitmap[5];
    public Bitmap gameBackground;
    public Bitmap[] player1 = new Bitmap[5];
    public Bitmap[] touchEffect = new Bitmap[8];


    public Texture(Context context) {
        try {
            background_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

            game_menu = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_menu);
            game_menu = game_menu.createScaledBitmap(game_menu, 500, 500, false);

            player_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_sheet);
            player_sheet = player_sheet.createScaledBitmap(player_sheet, 397, 331, false);

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
            player1[i] = Bitmap.createBitmap(player_sheet, 1 + 33 * i, 1, 32, 32);
            player1[i] = player1[i].createScaledBitmap(player1[i], MainActivity.screenHeight / 10, MainActivity.screenWidth / 10, false);
        }

        //in game pause button
        gameMenu[0] = Bitmap.createBitmap(game_menu, 1, 1, 100, 100);
        gameMenu[0] = gameMenu[0].createScaledBitmap(gameMenu[0], MainActivity.screenWidth / 14, MainActivity.screenWidth / 14, false);

        for (int i = 0; i < touchEffect.length; i++) {
            touchEffect[i] = Bitmap.createBitmap(effect_sheet, 1 + 65 * i, 1, 64, 64);
            touchEffect[i] = touchEffect[i].createScaledBitmap(touchEffect[i], MainActivity.screenHeight / 10, MainActivity.screenHeight / 10, false);
        }

    }

}
