package com.example.androidstudio_footballpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Texture {

    private Bitmap background_sheet = null;
    private Bitmap player_sheet = null;
    private Bitmap game_menu = null;

    public Bitmap[] gameMenu = new Bitmap[5];
    public Bitmap gameBackground;
    public Bitmap[] player1 = new Bitmap[5];

    public Texture(Context context) {
        try {
            background_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

            game_menu = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_menu);
            game_menu = game_menu.createScaledBitmap(game_menu, 500, 500, false);

            player_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_sheet);
            player_sheet = player_sheet.createScaledBitmap(player_sheet, 397, 331, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        getTextures();
    }

    private void getTextures() {
        gameBackground = background_sheet.createScaledBitmap(background_sheet, MainActivity.screenWidth, MainActivity.screenHeight, false);

        for (int i = 0; i < player1.length; i++) {
            player1[i] = Bitmap.createBitmap(player_sheet, 1 + 33 * i, 1, 32, 32);
            player1[i] = player1[i].createScaledBitmap(player1[i], MainActivity.screenHeight / 10, MainActivity.screenWidth / 10, false);
        }

        gameMenu[0] = Bitmap.createBitmap(game_menu, 1, 1, 100, 100);
        gameMenu[0] = gameMenu[0].createScaledBitmap(gameMenu[0], MainActivity.screenWidth / 14, MainActivity.screenWidth / 14, false);

    }

}
