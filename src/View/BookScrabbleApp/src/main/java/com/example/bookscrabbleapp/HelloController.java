package com.example.bookscrabbleapp;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label bookScrabbleLabel;

    @FXML
    private Canvas gameBoard;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView boardImage;
    boolean isGameStarted = false;


    ////Setting Start square - Star

    //yellow - #FFFF97
//        gameBoard[7][7].setColor(5);
//
//                //Setting Red squares - color 4 - Triple Word Score
//             red - #FE3032
//

    //yellow - #FFFF97
//                //Setting Yellow squares - color 3 - Double Word Score
//                gameBoard[1][1].setColor(3);
//                gameBoard[2][2].setColor(3);
//                gameBoard[3][3].setColor(3);
//                gameBoard[4][4].setColor(3);
//                gameBoard[1][13].setColor(3);
//                gameBoard[2][12].setColor(3);
//                gameBoard[3][11].setColor(3);
//                gameBoard[4][10].setColor(3);
//                gameBoard[13][13].setColor(3);
//                gameBoard[12][12].setColor(3);
//                gameBoard[11][11].setColor(3);
//                gameBoard[10][10].setColor(3);
//                gameBoard[10][4].setColor(3);
//                gameBoard[11][3].setColor(3);
//                gameBoard[12][2].setColor(3);
//                gameBoard[13][1].setColor(3);
//
    // BLUE - #0B8EFA
//                //Setting Blue squares - color 2 - Triple Letter Score
//                gameBoard[1][5].setColor(2);
//                gameBoard[1][9].setColor(2);
//                gameBoard[5][1].setColor(2);
//                gameBoard[5][5].setColor(2);
//                gameBoard[5][9].setColor(2);
//                gameBoard[5][13].setColor(2);
//                gameBoard[9][1].setColor(2);
//                gameBoard[9][5].setColor(2);
//                gameBoard[9][9].setColor(2);
//                gameBoard[9][13].setColor(2);
//                gameBoard[13][5].setColor(2);
//                gameBoard[13][9].setColor(2);
//
    // CYAN = #5DC5FC
//                //Setting Cyan squares - color 1 - Double Letter Score
//                gameBoard[0][3].setColor(1);
//                gameBoard[0][11].setColor(1);
//                gameBoard[2][6].setColor(1);
//                gameBoard[2][8].setColor(1);
//                gameBoard[3][0].setColor(1);
//                gameBoard[3][7].setColor(1);
//                gameBoard[3][14].setColor(1);
//                gameBoard[6][2].setColor(1);
//                gameBoard[6][6].setColor(1);
//                gameBoard[6][8].setColor(1);
//                gameBoard[6][12].setColor(1);
//                gameBoard[7][3].setColor(1);
//                gameBoard[7][11].setColor(1);
//                gameBoard[8][2].setColor(1);
//                gameBoard[8][6].setColor(1);
//                gameBoard[8][8].setColor(1);
//                gameBoard[8][12].setColor(1);
//                gameBoard[11][0].setColor(1);
//                gameBoard[11][7].setColor(1);
//                gameBoard[11][14].setColor(1);
//                gameBoard[12][6].setColor(1);
//                gameBoard[12][8].setColor(1);
//                gameBoard[14][3].setColor(1);
//                gameBoard[14][11].setColor(1);


   private void paintTheSquares(){
       String yellow = "-fx-background-color: #FFFF97";
       String cyan = "-fx-background-color: #5DC5FC";
       String red = "-fx-background-color: #FE3032";
       String blue = "-fx-background-color: #0B8EFA";
       String yellowLabel ="Double\nWord\nScore";
       String cyanLabel = "Double\nLetter\nScore";
       String redLabel = "Triple\nWord\nScore";
       String blueLabel = "Triple\nLetter\nScore";

       //red
       StackPane sp = (StackPane) gridPane.getChildren().get(0*15+0);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp = (StackPane) gridPane.getChildren().get(0*15+7);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp = (StackPane) gridPane.getChildren().get(0*15+14);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp = (StackPane) gridPane.getChildren().get(7*15+0);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp = (StackPane) gridPane.getChildren().get(7*15+14);
       sp.setStyle(red);
       sp.getChildren().add(new Label(redLabel));
       sp = (StackPane) gridPane.getChildren().get(14*15+0);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp = (StackPane) gridPane.getChildren().get(14*15+7);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp = (StackPane) gridPane.getChildren().get(14*15+14);
       sp.setStyle(red);
       sp.getChildren().add(new Label (redLabel));
       sp.setAlignment(Pos.CENTER);

       //yellow
       sp = (StackPane) gridPane.getChildren().get(7*15+7);
       sp.setStyle(yellow);
       sp = (StackPane) gridPane.getChildren().get(1*15+1);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(2*15+2);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(3*15+3);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(4*15+4);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(1*15+13);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(2*15+12);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(3*15+11);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(4*15+10);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(13*15+13);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(12*15+12);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(11*15+11);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(10*15+10);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(10*15+4);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(11*15+3);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(12*15+2);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));
       sp = (StackPane) gridPane.getChildren().get(13*15+1);
       sp.setStyle(yellow);
       sp.getChildren().add(new Label (yellowLabel));



       //blue
       sp = (StackPane) gridPane.getChildren().get(1*15+5);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(1*15+9);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(5*15+1);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(5*15+5);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(5*15+9);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(5*15+13);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(9*15+1);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(9*15+5);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(9*15+9);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(9*15+13);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(13*15+5);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));
       sp = (StackPane) gridPane.getChildren().get(13*15+9);
       sp.setStyle(blue);
       sp.getChildren().add(new Label (blueLabel));



       //cyan
       sp = (StackPane) gridPane.getChildren().get(0*15+3);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(0*15+11);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(2*15+6);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(2*15+8);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(3*15+0);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp.setAlignment(Pos.CENTER);
       sp = (StackPane) gridPane.getChildren().get(3*15+7);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(3*15+14);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(6*15+2);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(6*15+6);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(6*15+8);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(6*15+12);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(7*15+3);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(7*15+11);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(8*15+2);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(8*15+6);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(8*15+8);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(8*15+12);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(11*15+0);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(11*15+7);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(11*15+14);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(12*15+6);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(12*15+8);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(14*15+3);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));
       sp = (StackPane) gridPane.getChildren().get(14*15+11);
       sp.setStyle(cyan);
       sp.getChildren().add(new Label (cyanLabel));

   }
    final int HGAP = 11;
   final int VGAP = 4;
    public void init(MouseEvent e){

        double x = e.getX(); //the y of the mouse click relative to the scene
        double y = e.getY(); //the x of the mouse click relative to the scene

        System.out.println("x: " + x + " y: " + y);
        double sw = (gridPane.getWidth()-HGAP*14) / 15; //the width of each square
        double sh = (gridPane.getHeight()-VGAP*14) / 15; //the height of each square
        int cr = (int) Math.floor (y / (sh+VGAP)); //the row of the square clicked
        int cc = (int) Math.floor (x / (sw+HGAP)); //the column of the square clicked
        boolean clickedInGap = (x - (cc * (sw + HGAP))) > sw;
//                sp.setStyle("-fx-background-color: #ff0000");
        //((Label)sp.getChildren().get(0)).setTextFill(Color.WHITE);
        System.out.println("row: " + cr + " col: " + cc);
        System.out.println("clicked");
        System.out.println("clicked in gap: " + clickedInGap);
        System.out.println("square width: " + sw + " square height: " + sh + " grid width: " + gridPane.getWidth() + " grid height: " + gridPane.getHeight() + " "+ (x-cc*(sw+8)));
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        if(!boardImage.isVisible())
            boardImage.setVisible(true);
        //fill the center square of the gridPane with color black (of grid 15x15)

//        gc.strokeRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
//        gc.setFill(Paint.valueOf("#43B14F"));
//        gc.fillRect(0, 0, gameBoard.getWidth(), gameBoard.getHeight());
//        final double width = gameBoard.getWidth();
//        final double height = gameBoard.getHeight();
//        final double cellWidth = width / 15 ;
//        final double cellHeight = height / 15 ;

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
//            for(int i=0;i<15;i++){
//                for(int j=0;j<15;j++){
//                    StackPane sp = new StackPane();
//                    sp.setScaleX(0.78);
//                    sp.setScaleY(0.92);
//                    //Label text = new Label(i + "," + j);
//                    //sp.getChildren().add(text);
//                    sp.setStyle("-fx-background-color:#43B14F;");
//                    gridPane.add(sp, j, i);
//                    System.out.println(i+","+j);
//                    System.out.println("Added stackpane to gridpane");
//                }
//            }
            //paintTheSquares();
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
//            boardImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//                double mouseX = event.getX();
//                double mouseY = event.getY();
//                System.out.println("image eventListener: x: " + mouseX + " y: " + mouseY);
//
//                double squareWidth = (gridPane.getWidth()+8*14) / 15;
//                double squareHeight = (gridPane.getHeight()+4*14) / 15;
//
//                int clickedRow = (int) Math.floor (mouseY / squareHeight);
//                int clickedCol = (int) Math.floor (mouseX / squareWidth);
////                sp.setStyle("-fx-background-color: #ff0000");
//                //((Label)sp.getChildren().get(0)).setTextFill(Color.WHITE);
//                System.out.println("row: " + clickedRow + " col: " + clickedCol);
//            });
            isGameStarted = true;
        }
    }

}




