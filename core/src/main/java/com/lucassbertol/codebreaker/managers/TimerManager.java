package com.lucassbertol.codebreaker.managers;

import com.lucassbertol.codebreaker.config.Constants;

public class TimerManager {
    private float timeRemaining;
    private boolean timeUp;

    public TimerManager(String difficulty) {
        if (Constants.DIFFICULTY_EASY.equals(difficulty)) {
            this.timeRemaining = 80; // 1 minuto e 15 segundos
        } else {
            this.timeRemaining = 155; // 2 minutos e 30 segundos
        }
        this.timeUp = false;
    }

    public void update(float delta) {
        if (timeRemaining > 0) {
            timeRemaining -= delta;
            if (timeRemaining <= 0) {
                timeRemaining = 0;
                timeUp = true;
            }
        }
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public String getFormattedTime() {
        int minutes = (int) timeRemaining / 60;
        int seconds = (int) timeRemaining % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}

