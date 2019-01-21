package controllers;

import eu.hansolo.enzo.lcd.Lcd;
import eu.hansolo.enzo.lcd.LcdBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;


public class mainViewController implements Initializable {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox menuBox;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button copyButton;

    @FXML
    private Button exitButton;

    private Lcd control;
    private Timeline timeline;
    private boolean started;
    private int minutes;
    private int seconds;
    private int milliseconds;
    StringBuilder stringBuilder;
    private double xOffset;
    private double yOffset;

    public void initialize(URL location, ResourceBundle resources) {
        started = false;
        xOffset = 0.0;
        yOffset = 0.0;
        control = LcdBuilder.create()
                .prefHeight(mainPane.getPrefHeight())
                .prefWidth(mainPane.getPrefWidth() - menuBox.getPrefWidth())
                .lcdDesign(Lcd.LcdDesign.DARKBLUE)
                .title("StopWatch")
                .decimals(3)
                .valueFont(Lcd.LcdFont.DIGITAL)
                .noFrame(true)
                .text("00:00:000")
                .textMode(true)
                .build();

        mainPane.getChildren().addAll(control);
        stringBuilder = new StringBuilder();
        prepareTimeline();
        setListeners();
    }

    private void calculateTime() {
        if (milliseconds == 1000) {
            seconds++;
            milliseconds = 0;
            if (seconds == 60) {
                minutes++;
                seconds = 0;
                if (minutes == 60) {
                    minutes = 0;
                }
            }
        }
    }

    private void setListeners() {
        control.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        control.setOnMouseDragged(event -> {
            mainPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            mainPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });

        startButton.setOnAction(event->{
            if(started == false){
                timeline.play();
                startButton.setStyle("-fx-background-image: url('/images/stop.png');");
                started = true;
            }else{
                timeline.stop();
                startButton.setStyle("-fx-background-image: url('/images/start.png');");
                started = false;
            }
        });

        resetButton.setOnAction(event-> {
            started = false;
            minutes = 0;
            seconds = 0;
            milliseconds = 0;
            control.setText("00:00:000");
        });

        exitButton.setOnAction(event -> Platform.exit());

        copyButton.setOnAction(event ->{
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(control.getText());
            clipboard.setContents(stringSelection, null);
        });
    }

    private void prepareTimeline() {
        timeline = new Timeline(new KeyFrame[]{new KeyFrame(Duration.millis(1), event -> {
            milliseconds++;
            calculateTime();
            stringBuilder.append((minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + ":" +
                    (milliseconds < 10 ? "00" + milliseconds : milliseconds < 100 ? "0" + milliseconds : milliseconds));

            control.setText(stringBuilder.toString());
            stringBuilder.setLength(0);
        }, new KeyValue[0])});

        timeline.setCycleCount(-1);
        timeline.setAutoReverse(false);
    }
}
