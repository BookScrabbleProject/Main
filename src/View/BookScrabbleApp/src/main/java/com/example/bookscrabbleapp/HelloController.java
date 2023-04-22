package com.example.bookscrabbleapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class HelloController {
    @FXML
    private Label welcomeText;


    @FXML
    private Label bookScrabbleLabel;

    @FXML
    private Canvas gameBoard;

    public void init(){
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        gc.strokeRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
        gc.setFill(Paint.valueOf("#43B14F"));
        gc.fillRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());




    }

}