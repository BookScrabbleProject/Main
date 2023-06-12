package com.example.bookscrabbleapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class HelloController {
    @FXML
    private Label welcomeText;


    @FXML
    private Label bookScrabbleLabel;

    @FXML
    private Canvas gameBoard;

    @FXML
    private GridPane gridPane;

    boolean isGameStarted = false;
    public void init(){

        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        //fill the center square of the gridPane with color black (of grid 15x15)



        gc.strokeRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
        gc.setFill(Paint.valueOf("#43B14F"));
        gc.fillRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
        final double width = gameBoard.getWidth();
        final double height = gameBoard.getHeight();
        final double cellWidth = width / 15 ;
        final double cellHeight = height / 15 ;
        //draw a black square in the middle
        gc.setFill(Color.BLACK);
        gc.fillRect(7*cellWidth, 7*cellHeight, cellWidth, cellHeight);
//        gc.setFill(Paint.valueOf("#43B14F"));
//        for( int i=0; i<15; i++) {
//            for (int j = 0; j < 15; j++) {
//                if(i==7 && j==7) continue;
//                gc.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
//            }
//        }
        //put a label on every square
//        gc.setFill(Color.BLACK);
//        for( int i=0; i<15; i++) {
//            for (int j = 0; j < 15; j++) {
//                if(i==7 && j==7) continue;
//                gc.fillText(i + "," + j, i * cellWidth+15, j * cellHeight + 15);
//            }
//        }
        if(!isGameStarted) {
            //add stackpane to the gridpane with label of i and j
            for(int i=0;i<15;i++){
                for(int j=0;j<15;j++){
                    StackPane sp = new StackPane();
                    Label text = new Label(i + "," + j);
                    sp.getChildren().add(text);
                    gridPane.add(sp, j, i);
                    System.out.println(i+","+j);
                    System.out.println("Added stackpane to gridpane");
                }
            }
//            for(int i=0; i<15;i++){
//                for(int j=0; j<15;j++){
//                    StackPane sp = new StackPane();
//                    Label text = new Label(i + "," + j);
//                    sp.getChildren().add(text);
//                    gridPane.add(sp, i, j);
//                    System.out.println(i+","+j);
//                    System.out.println("Added stackpane to gridpane");
//                }
//            }
            gameBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                double mouseX = event.getX();
                double mouseY = event.getY();

                double squareSize = gameBoard.getWidth() / 15;

                int clickedRow = (int) Math.floor (mouseY / squareSize);
                int clickedCol = (int) Math.floor (mouseX / squareSize);
                StackPane sp = (StackPane) gridPane.getChildren().get(clickedRow*15+clickedCol+1);
                sp.setStyle("-fx-background-color: #ff0000");
                ((Label)sp.getChildren().get(0)).setTextFill(Color.WHITE);
                System.out.println(GridPane.getColumnIndex(sp));



            });
            isGameStarted = true;
        }

    }

}