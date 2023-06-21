package View.bookscrabbleapp;

import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import General.MethodsNames;


import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class InGameController implements Observer {
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
    @FXML
    private Label player1Name;
    @FXML
    private Label player2Name;
    @FXML
    private Label player3Name;
    @FXML
    private Label myPlayerName;
    @FXML
    private Label player1Score;
    @FXML
    private Label player2Score;
    @FXML
    private Label player3Score;
    @FXML
    private Label myPlayerScore;
    @FXML
    private GridPane tilesGrid;
    @FXML
    private Label numOfTilesInBag;
    @FXML
    private Label numOfTilesInHand;
    @FXML
    private Button finishTurnBtn;
    @FXML
    private Button undoBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private ImageView bagImage;
    @FXML
    private ImageView player1Image;
    @FXML
    private ImageView player2Image;
    @FXML
    private ImageView player3Image;
    @FXML
    private ImageView myPlayerImage;
    @FXML
    private Circle player1ImageCircle;
    @FXML
    private Circle player2ImageCircle;
    @FXML
    private Circle player3ImageCircle;
    @FXML
    private Circle myPlayerImageCircle;

    boolean isGameStarted = false;



    final int HGAP = 11;
    final int VGAP = 4;
    public void init(MouseEvent e){

        ViewModel.getViewModel().addObserver(this);
        double MOUSE_CLICKED_X = e.getX(); //the y of the mouse click relative to the scene
        double MOUSE_CLICKED_Y = e.getY(); //the x of the mouse click relative to the scene

        System.out.println("x: " + MOUSE_CLICKED_X + " y: " + MOUSE_CLICKED_Y);
        double SQUARE_WIDTH = (gridPane.getWidth()-HGAP*14) / 15; //the width of each square
        double SQUARE_HEIGHT = (gridPane.getHeight()-VGAP*14) / 15; //the height of each square
        int cr = (int) Math.floor (MOUSE_CLICKED_Y / (SQUARE_HEIGHT+VGAP)); //the row of the square clicked
        int cc = (int) Math.floor (MOUSE_CLICKED_X / (SQUARE_WIDTH+HGAP)); //the column of the square clicked
        boolean clickedInGap = (MOUSE_CLICKED_X - (cc * (SQUARE_WIDTH + HGAP))) > SQUARE_WIDTH;
//                sp.setStyle("-fx-background-color: #ff0000");
        //((Label)sp.getChildren().get(0)).setTextFill(Color.WHITE);
        if(clickedInGap) return;
        System.out.println("row: " + cr + " col: " + cc);
        System.out.println("clicked");
        System.out.println("clicked in gap: " + clickedInGap);
        System.out.println("square width: " + SQUARE_WIDTH + " square height: " + SQUARE_HEIGHT + " grid width: " + gridPane.getWidth() + " grid height: " + gridPane.getHeight() + " "+ (MOUSE_CLICKED_X-cc*(SQUARE_WIDTH+8)));
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        if(!boardImage.isVisible())
            boardImage.setVisible(true);

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

            char myC = 'a';
            //paintTheSquares();
            for(int i=0; i<15;i++){
                for(int j=0; j<15;j++){

                    Image tile = new Image(getClass().getResource("/Images/Tiles/"+myC+"Letter.png").toExternalForm());
                    myC++;
                    if(myC>'z') myC = 'a';
                    ImageView iv = new ImageView(tile);
                    iv.setFitWidth(SQUARE_WIDTH-3);
                    iv.setFitHeight(SQUARE_HEIGHT-2);
                    StackPane sp = new StackPane(iv);
                    sp.setAlignment(Pos.CENTER);
                    gridPane.add(sp, j, i);
                    System.out.println(i+","+j);
                    System.out.println("Added stackpane to gridpane");
                }
            }
            isGameStarted = true;
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println("View update "+arg);
        switch ((String) arg){
            case MethodsNames.DISCONNECT_FROM_SERVER:
                System.out.println("In Case Disconnect");
                Platform.runLater(()->showBackAlert());
                System.out.println("Alert Created");
                break;
        }
    }

    public void showBackAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Host Has Disconnected");
        alert.setHeaderText("Host Has Disconnected");
        alert.setContentText("The Host Has Disconnected, You Will Be Redirected To The Main Menu");
        //add a button of go back
        ButtonType buttonTypeOne = new ButtonType("Go Back");

        alert.getButtonTypes().setAll(buttonTypeOne);
        alert.setOnCloseRequest(event -> {
            System.out.println("Alert Closed");
            ((Stage) alert.getDialogPane().getScene().getWindow()).close();
            Platform.runLater(this::moveToLoginScene);

        });
        alert.showAndWait();
    }

    public void moveToLoginScene(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
            //close the current window and change to the root
            Scene scene = new Scene(root, 650, 650);
            Stage stage = (Stage) gameBoard.getScene().getWindow();
            stage.close();
            stage = new Stage();
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




