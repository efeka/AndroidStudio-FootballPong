package com.example.androidstudio_footballpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.androidstudio_footballpong.objects.MainMenu;

/**
 * This class is for getting image files as Bitmaps.
 * It prepares the Bitmaps by scaling and cropping so that they can be easily used in other classes.
 */
public class Texture {

    private Bitmap background_sheet = null;
    private Bitmap menu_sheet = null;
    private Bitmap assets_sheet = null;

    public Bitmap pauseButton;
    public Bitmap gameBackground;
    public Bitmap[] player1 = new Bitmap[8];
    public Bitmap[] player2 = new Bitmap[8];
    public Bitmap[] aiPlayer = new Bitmap[8];
    public Bitmap[] touchEffect = new Bitmap[8];
    public Bitmap[] goals = new Bitmap[2];
    public Bitmap ball;

    public Bitmap menuBackground;
    public Bitmap[] mainMenuButtons = new Bitmap[3];
    public Bitmap[] onePlayerMenuButtons = new Bitmap[12];
    public Bitmap[] pauseMenuButtons = new Bitmap[3];
    public Bitmap[] settingsMenuButtons = new Bitmap[4];
    public Bitmap[] otherButtons = new Bitmap[2];
    public Bitmap[] menuTitles = new Bitmap[4];

    public Bitmap[] backgroundFiller = new Bitmap[2];

    public Texture(Context context) {
        try {
            background_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

            menu_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_assets);
            menu_sheet = Bitmap.createScaledBitmap(menu_sheet, 555, 769, false);

            assets_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.assets_sheet);
            assets_sheet = Bitmap.createScaledBitmap(assets_sheet, 800, 600, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getTextures();
    }

    private void getTextures() {
        //in game background
        gameBackground = Bitmap.createScaledBitmap(background_sheet, MainActivity.screenWidth, MainActivity.screenHeight, false);

        menuBackground = Bitmap.createBitmap(menu_sheet, 1, 1, 400, 200);
        menuBackground = Bitmap.createScaledBitmap(menuBackground, MainActivity.screenWidth, MainActivity.screenHeight, false);

        for (int i = 0; i < mainMenuButtons.length; i++) {
            mainMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 1 + 181 * i, 202, 180, 60);
            mainMenuButtons[i] = Bitmap.createScaledBitmap(mainMenuButtons[i], MainActivity.screenWidth / 3, MainActivity.screenHeight / 5, false);
        }

        menuTitles[0] = Bitmap.createBitmap(menu_sheet, 1, 263, 452, 66);
        menuTitles[0] = Bitmap.createScaledBitmap(menuTitles[0], 2 * MainActivity.screenWidth / 3, MainActivity.screenHeight / 4, false);
        menuTitles[1] = Bitmap.createBitmap(menu_sheet, 1, 330, 255, 66);
        menuTitles[1] = Bitmap.createScaledBitmap(menuTitles[1], MainActivity.screenWidth / 3, MainActivity.screenHeight / 7, false);
        menuTitles[2] = Bitmap.createBitmap(menu_sheet, 257, 330, 292, 66);
        menuTitles[2] = Bitmap.createScaledBitmap(menuTitles[2], MainActivity.screenWidth / 3, MainActivity.screenHeight / 7, false);
        menuTitles[3] = Bitmap.createBitmap(menu_sheet, 1, 703, 266, 65);
        menuTitles[3] = Bitmap.createScaledBitmap(menuTitles[3], MainActivity.screenWidth / 3, MainActivity.screenHeight / 7, false);


        for (int i = 0; i < 3; i++) {
            onePlayerMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 1 + 121 * i, 397, 120, 60);
            onePlayerMenuButtons[i] = Bitmap.createScaledBitmap(onePlayerMenuButtons[i], 2 * MainActivity.screenWidth / 9, MainActivity.screenHeight / 5, false);
        }
        for (int i = 3; i < 6; i++) {
            onePlayerMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 1 + 121 * (i - 3), 458, 120, 60);
            onePlayerMenuButtons[i] = Bitmap.createScaledBitmap(onePlayerMenuButtons[i], 2 * MainActivity.screenWidth / 9, MainActivity.screenHeight / 5, false);
        }
        for (int i = 6; i < 9; i++) {
            onePlayerMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 1 + 121 * (i - 6), 519, 120, 60);
            onePlayerMenuButtons[i] = Bitmap.createScaledBitmap(onePlayerMenuButtons[i], 2 * MainActivity.screenWidth / 9, MainActivity.screenHeight / 5, false);
        }
        for (int i = 9; i < 12; i++) {
            onePlayerMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 1 + 121 * (i - 9), 580, 120, 60);
            onePlayerMenuButtons[i] = Bitmap.createScaledBitmap(onePlayerMenuButtons[i], 2 * MainActivity.screenWidth / 9, MainActivity.screenHeight / 5, false);
        }

        for (int i = 0; i < pauseMenuButtons.length; i++) {
            pauseMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 1 + 181 * i, 641, 180, 60);
            pauseMenuButtons[i] = Bitmap.createScaledBitmap(pauseMenuButtons[i], MainActivity.screenWidth / 3, MainActivity.screenHeight / 5, false);
        }

        for (int i = 0; i < settingsMenuButtons.length; i++) {
            settingsMenuButtons[i] = Bitmap.createBitmap(menu_sheet, 268 + 67 * i, 702, 66, 66);
            settingsMenuButtons[i] = Bitmap.createScaledBitmap(settingsMenuButtons[i], MainActivity.screenHeight / 4, MainActivity.screenHeight / 4, false);
        }

        otherButtons[0] = Bitmap.createBitmap(menu_sheet, 364, 397, 160, 80);
        otherButtons[0] = Bitmap.createScaledBitmap(otherButtons[0], MainActivity.screenWidth / 8, MainActivity.screenHeight / 8, false);
        otherButtons[1] = Bitmap.createBitmap(menu_sheet, 364, 478, 180, 60);
        otherButtons[1] = Bitmap.createScaledBitmap(otherButtons[1], MainActivity.screenWidth / 4, MainActivity.screenHeight / 6, false);

        for (int i = 0; i < player1.length; i++) {
            player1[i] = Bitmap.createBitmap(assets_sheet, 1 + 49 * i, 102, 48, 48);
            player1[i] = Bitmap.createScaledBitmap(player1[i], MainActivity.screenHeight / 6, MainActivity.screenWidth / 12, false);
        }
        for (int i = 0; i < player2.length; i++) {
            player2[i] = Bitmap.createBitmap(assets_sheet, 1 + 49 * i, 151, 48, 48);
            player2[i] = Bitmap.createScaledBitmap(player2[i], MainActivity.screenHeight / 6, MainActivity.screenWidth / 12, false);
        }
        for (int i = 0; i < aiPlayer.length; i++) {
            aiPlayer[i] = Bitmap.createBitmap(assets_sheet, 1 + 49 * i, 265, 48, 48);
            aiPlayer[i] = Bitmap.createScaledBitmap(aiPlayer[i], MainActivity.screenHeight / 6, MainActivity.screenWidth / 12, false);
        }

        //in game pause button
        pauseButton = Bitmap.createBitmap(menu_sheet, 402, 1, 100, 100);
        pauseButton = Bitmap.createScaledBitmap(pauseButton, MainActivity.screenWidth / 14, MainActivity.screenWidth / 14, false);

        for (int i = 0; i < touchEffect.length; i++) {
            touchEffect[i] = Bitmap.createBitmap(assets_sheet, 1 + 65 * i, 200, 64, 64);
            touchEffect[i] = Bitmap.createScaledBitmap(touchEffect[i], MainActivity.screenHeight / 10, MainActivity.screenHeight / 10, false);
        }

        goals[0] = Bitmap.createBitmap(assets_sheet, 1, 1, 32, 100);
        goals[0] = Bitmap.createScaledBitmap(goals[0], 100, 2 * MainActivity.screenHeight / 5, false);
        goals[1] = Bitmap.createBitmap(assets_sheet, 34, 1, 32, 100);
        goals[1] = Bitmap.createScaledBitmap(goals[1], 100, 2 * MainActivity.screenHeight / 5, false);

        ball = Bitmap.createBitmap(assets_sheet, 67, 1, 100, 100);
        ball = Bitmap.createScaledBitmap(ball, MainActivity.screenWidth / 25, MainActivity.screenWidth / 25, false);

        for (int i = 0; i < backgroundFiller.length; i++) {
            backgroundFiller[i] = Bitmap.createBitmap(assets_sheet, 269 + 26 * i, 1, 25, 76);
            backgroundFiller[i] = Bitmap.createScaledBitmap(backgroundFiller[i], 100, 2 * MainActivity.screenHeight / 5, false);
        }
    }

}
