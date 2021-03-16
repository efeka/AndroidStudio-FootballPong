package com.example.androidstudio_pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.androidstudio_footballpong.Game;

//Entry point to the application

public class MainActivity extends Activity {

    public static int screenWidth, screenHeight;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        game = new Game(this);
        game.state = Game.STATE.MAIN_MENU;
        setContentView(game);
    }

    @Override
    protected void onPause() {
        game.pauseGame();
        super.onPause();
    }

}