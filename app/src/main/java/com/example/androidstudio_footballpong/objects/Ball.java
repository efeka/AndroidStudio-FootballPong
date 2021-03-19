package com.example.androidstudio_footballpong.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.androidstudio_footballpong.MainActivity;
import com.example.androidstudio_footballpong.R;

import java.util.ArrayList;

/**
 * Ball is an object that bounces from players and screen borders and is used for scoring goals.
 */
public class Ball extends GameObject {

    private final int BORDER_LEFT = 0;
    private final int BORDER_RIGHT = MainActivity.screenWidth;
    private final int BORDER_UP = 0;
    private final int BORDER_DOWN = MainActivity.screenHeight;

    private Paint paint;
    private Context context;

    public int velX, velY;
    private final int MAX_SPEED = 10;

    private Player1 player1;

    private double initialX, initialY;

    public ArrayList<GameObject> trailList = new ArrayList<>();

    public Ball(Context context, double x, double y, int width, int height, Player1 player1) {
        super(x, y, width, height);
        this.context = context;
        this.player1 = player1;

        initialX = x;
        initialY = y;

        velX = -MAX_SPEED;
        velY = MAX_SPEED;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.white);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(ContextCompat.getColor(context, R.color.purple_200));
        canvas.drawCircle((int) x, (int) y, width, paint);
    }

    @Override
    public void update() {
        x += velX;
        y += velY;

        collision();
    }

    private void collision() {
        //collision with screen borders
        if (x < BORDER_LEFT) {
            velX *= -1;
            x = BORDER_LEFT;
        }
        if (x > BORDER_RIGHT) {
            velX *= -1;
            x = BORDER_RIGHT;
        }
        if (y < BORDER_UP) {
            velY *= -1;
            y = BORDER_UP;
        }
        if (y > BORDER_DOWN) {
            velY *= -1;
            y = BORDER_DOWN;
        }

        //collision with Player1
        if (getBoundsLeft().intersect(player1.getBounds())) {
            velX *= -1;
            x = player1.getX() + player1.getWidth() + width;
        }
        if (getBoundsRight().intersect(player1.getBounds())) {
            velX *= -1;
            x = player1.getX() - width;
        }
        if (getBoundsTop().intersect(player1.getBounds())) {
            if (velY < 0)
                velY *= -1;
            y = player1.getY() + player1.getHeight() + height;
        }
        if (getBoundsBottom().intersect(player1.getBounds())) {
            if (velY > 0)
                velY *= -1;
            y = player1.getY() - height;
        }


    }

    public void resetPosition() {
        x = initialX;
        y = initialY;
    }

    @Override
    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public Rect getBoundsTop() {
        return new Rect((int) x - 2 * width / 4, (int) y - height, (int) x + 2 * width / 4, (int) y - height / 3);
    }

    public Rect getBoundsBottom() {
        return new Rect((int) x - 2 * width / 4, (int) y + height / 3, (int) x + 2 * width / 4, (int) y + height);
    }

    public Rect getBoundsLeft() {
        return new Rect((int) x - width, (int) y - 2 * height / 4, (int) x - width / 3, (int) y + 2 * height / 4);
    }

    public Rect getBoundsRight() {
        return new Rect((int) x + width / 3, (int) y - 2 * height / 4, (int) x + width, (int) y + 2 * height / 4);
    }

}
