package com.example.androidstudio_footballpong;

import com.example.androidstudio_footballpong.objects.Ball;
import com.example.androidstudio_footballpong.objects.Player1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BallUnitTest {

    int BORDER_LEFT = 10;
    int BORDER_RIGHT = MainActivity.screenWidth - 10;
    int BORDER_UP = 10;
    int BORDER_DOWN = MainActivity.screenHeight - 10;

    Player1 player1;
    Texture tex = Game.getTexture();

    @Before
    public void setup() {
        player1 = new Player1(null, MainActivity.screenWidth / 4, MainActivity.screenHeight / 2, MainActivity.screenHeight / 10, MainActivity.screenWidth / 10);
    }

    @Test
    public void testBallCollisionWithLeftBorder() {
        Ball ball = new Ball(null, BORDER_LEFT - 300, 100, 100, 100, player1);
        ball.collision();
        assertEquals(Math.abs(BORDER_LEFT + ball.getWidth() / 2), Math.abs((int) ball.getX()));
    }

    @Test
    public void testBallCollisionWithRightBorder() {
        Ball ball = new Ball(null, BORDER_RIGHT + 300, 100, 100, 100, player1);
        ball.collision();
        assertEquals(Math.abs(BORDER_RIGHT - ball.getWidth() / 2), Math.abs((int) ball.getX()));
    }

    @Test
    public void testBallCollisionWithTopBorder() {
        Ball ball = new Ball(null, 100, BORDER_UP - 300, 100, 100, player1);
        ball.collision();
        assertEquals(Math.abs(BORDER_UP + ball.getWidth()), Math.abs((int) ball.getY()));
    }

    @Test
    public void testBallCollisionWithBottomBorder() {
        Ball ball = new Ball(null, 100, BORDER_DOWN + 300, 100, 100, player1);
        ball.collision();
        assertEquals(Math.abs(BORDER_DOWN - ball.getWidth()), Math.abs((int) ball.getY()));
    }

}
