package com.sudoku.gameService;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class timerService {
    @FXML
    private Label timeLabel;
    private static Timeline timeline;
    private boolean isRunning=false;

    public void setTimeLabel(Label timeLabel){this.timeLabel = timeLabel;}

    public void startTimer(){
        if(isRunning){
            timeline.stop();
        }
        DateFormat timeFormat = new SimpleDateFormat( "mm:ss" );
        AtomicInteger count = new AtomicInteger();
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 500 ),
                        event -> timeLabel.setText( timeFormat.format(count.getAndAdd(500)) )
                )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
        isRunning = true;
    }
    public void stopTimer(){
        isRunning=false;
        timeline.stop();
        timeLabel.setText("GG:WP");
    }

    public void toggleTimer(){
        if(isRunning) pauseTimer();
        else resumeTimer();
    }

    private void pauseTimer(){
        timeline.stop();
        timeLabel.setText("||");
        isRunning=false;
    }
    private void resumeTimer(){
        timeline.play();
        isRunning=true;
    }
}
