package com.example.androidstudio_footballpong;

public class GameData {

    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;

    private int score1 = 0, score2 = 0;
    private int gameTimer = 60;
    private int difficulty = DIFFICULTY_MEDIUM;
    private int selectedGameLength = 1;

    /**
     * Decreases the game timer by the amount of given seconds
     * @param seconds amount of seconds to remove from the game timer
     * @return current value of the game timer
     */
    public int decrementTimer(int seconds) {
        gameTimer -= seconds;
        return gameTimer >= 0 ? gameTimer : (gameTimer = 0);
    }

    /**
     * @return the score of the player on the left.
     */
    public int getScore1() {
        return score1;
    }

    /**
     * @param score1 the score of the player on the left.
     */
    public void setScore1(int score1) {
        this.score1 = score1;
    }

    /**
     * @return the score of the player on the right.
     */
    public int getScore2() {
        return score2;
    }

    /**
     * @param score2 the score of the player on the right.
     */
    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void resetScores() {
        score1 = score2 = 0;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getGameTimer() {
        return gameTimer;
    }

    public void setGameTimer(int seconds) {
        gameTimer = seconds;
    }

    public void setGameTimer(int minutes, int seconds) {
        gameTimer = minutes * 60 + seconds;
    }

    public int getSelectedGameLength() {
        return selectedGameLength;
    }

    public void setSelectedGameLength(int length) {
        selectedGameLength = length;
    }

    public void resetGameTimer() {
        switch (selectedGameLength) {
            case 1:
                setGameTimer(3, 0);
                break;
            case 2:
                setGameTimer(5, 0);
                break;
            case 3:
                setGameTimer(8, 0);
        }
    }

    /**
     * Returns formatted game timer.
     */
    public String getTimerDisplay() {
        return convertTimerToString();
    }

    /**
     * Converts game timer into a [min][min]:[sec][sec] format.
     */
    private String convertTimerToString() {
        int temp = gameTimer;
        int minutes = 0, seconds;
        String timerDisplay;
        while (temp >= 60) {
            temp -= 60;
            minutes++;
        }
        seconds = temp;

        if (minutes < 10)
            timerDisplay = "0" + minutes + ":";
        else
            timerDisplay = minutes + ":";

        if (seconds < 10)
            timerDisplay += "0" + seconds;
        else
            timerDisplay += seconds;

        return timerDisplay;
    }

}
