package View.bookscrabbleapp;

import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import General.MethodsNames;


import java.io.IOException;
import java.net.URL;
import java.util.*;


public class InGameController implements Observer, Initializable {
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
    private GridPane tilesInHandGrid;
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


    Label[] playersNames = new Label[4];
    Label[] playersScores = new Label[4];
    ImageView[] playersImages = new ImageView[4];
    Circle[] playersImagesCircles = new Circle[4];


    boolean isGameStarted = false;
    Character pickedTile = null;
    int[] playersPosition = new int[4];



    final int BOARD_HGAP = 11;
    final int BOARD_VGAP = 4;
    final int HAND_HGAP = 20;
    public void boardClickHandler(MouseEvent e){
        if(ViewModel.getViewModel().getMyPlayer().getId()!=ViewModel.getViewModel().getCurrentPlayerId())
            return;
        double MOUSE_CLICKED_X = e.getX(); //the y of the mouse click relative to the scene
        double MOUSE_CLICKED_Y = e.getY(); //the x of the mouse click relative to the scene

        System.out.println("x: " + MOUSE_CLICKED_X + " y: " + MOUSE_CLICKED_Y);
        double SQUARE_WIDTH = (gridPane.getWidth()-BOARD_HGAP*14) / 15; //the width of each square
        double SQUARE_HEIGHT = (gridPane.getHeight()-BOARD_VGAP*14) / 15; //the height of each square
        int cr = (int) Math.floor (MOUSE_CLICKED_Y / (SQUARE_HEIGHT+BOARD_VGAP)); //the row of the square clicked
        int cc = (int) Math.floor (MOUSE_CLICKED_X / (SQUARE_WIDTH+BOARD_HGAP)); //the column of the square clicked
        boolean clickedInGap = (MOUSE_CLICKED_X - (cc * (SQUARE_WIDTH + BOARD_HGAP))) > SQUARE_WIDTH;
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

        if(!isGameStarted) {
            char myC = 'a';
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
        }
        ViewModel.getViewModel().takeTileFromBag();
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println("View update "+arg);
        switch ((String) arg){
            case MethodsNames.DISCONNECT_FROM_SERVER:
                System.out.println("In Case Disconnect");
                Platform.runLater(this::showBackAlert);
                System.out.println("Alert Created");
                break;

            case MethodsNames.SET_HAND:
                Platform.runLater(this::setHand);
                break;
            case MethodsNames.NEW_PLAYER_TURN:
                Platform.runLater(this::newPlayerTurn);
                break;
        }

    }

    private void newPlayerTurn() {
        for(int i=0; i<4; i++){
            if(ViewModel.getViewModel().getPlayers().get(i)==null) continue;
            if(playersNames[i]==null) continue;
            if(ViewModel.getViewModel().getPlayers().get(i).getId()==ViewModel.getViewModel().getCurrentPlayerId()){
                playersImagesCircles[i].setStroke(Color.RED);
            }
            else{
                playersImagesCircles[i].setStroke(Color.BLACK);
            }
        }

        if (ViewModel.getViewModel().getMyPlayer().getId() == ViewModel.getViewModel().getCurrentPlayerId()) {
            resetBtn.setDisable(false);
            undoBtn.setDisable(false);
            finishTurnBtn.setDisable(false);
        }
        else{
            resetBtn.setDisable(true);
            undoBtn.setDisable(true);
            finishTurnBtn.setDisable(true);
        }

    }

    private void setHand() {
        double HAND_SQUARE_WIDTH = (tilesInHandGrid.getWidth()-HAND_HGAP*6) / 7; //the width of each square
        double HAND_SQUARE_HEIGHT = (tilesInHandGrid.getHeight()); //the height of each square
        if(tilesInHandGrid.getChildren().size() > 0)
            tilesInHandGrid.getChildren().clear();
        if(ViewModel.getViewModel().getMyPlayer().getHand() == null || ViewModel.getViewModel().getMyPlayer().getHand().size()==0 ) return;
        for(int i = 0; i< ViewModel.getViewModel().getMyPlayer().getHand().size(); i++) {
            Character myC = ViewModel.getViewModel().getMyPlayer().getHand().get(i);
            Image tile = new Image(getClass().getResource("/Images/Tiles/" + myC + "Letter.png").toExternalForm());
            ImageView iv = new ImageView(tile);
            iv.setFitWidth(HAND_SQUARE_WIDTH);
            iv.setFitHeight(HAND_SQUARE_HEIGHT);
            StackPane sp = new StackPane(iv);
            sp.setAlignment(Pos.CENTER);
            tilesInHandGrid.add(sp, i, 0);

            System.out.println("Added stackpane to hand gridpane");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewModel.getViewModel().addObserver(this);
        for(int i=0;i<3;i++){
            playersPosition[i] = -1;
        }
        isGameStarted = true;
        setHand();
        fillPlayersDetails();
        numOfTilesInHand.textProperty().bind(ViewModel.getViewModel().getMyPlayer().numberOfTilesProperty);
        numOfTilesInBag.textProperty().bind(ViewModel.getViewModel().numberOfTilesInBagProperty);

        newPlayerTurn();

    }

    private void fillPlayersDetails() {
        int myID = ViewModel.getViewModel().getMyPlayer().getId();
        int myScore = ViewModel.getViewModel().getMyPlayer().getScore();
        for(int i=0;i<3;i++){
            if(ViewModel.getViewModel().getPlayers().get(i) == null) continue;
            if(ViewModel.getViewModel().getPlayers().get(i).getId() == myID) {
                playersPosition[i]=4;
                playersNames[i] = myPlayerName;
                playersScores[i] = myPlayerScore;
                playersImagesCircles[i] = myPlayerImageCircle;
                playersImages[i] = myPlayerImage;
                continue;
            }
            String name = ViewModel.getViewModel().getPlayers().get(i).getName();
            insertPlayerDetails(name, i);
        }
    }

    public void insertPlayerDetails(String name, int id){
        if(player1Name.getText().equals("")){
            player1Name.setText(name);
            player1Score.setText(0+" pts");
            player1Name.setVisible(true);
            player1Score.setVisible(true);
            //player1Image.setVisible(true);
            player1ImageCircle.setVisible(true);
            playersPosition[id] = 1;
            playersNames[id] = player1Name;
            playersScores[id] = player1Score;
            playersImagesCircles[id] = player1ImageCircle;
            playersImages[id] = player1Image;
        }
        else if(player2Name.getText().equals("")){
            player2Name.setText(name);
            player2Score.setText(0+" pts");
            player2Name.setVisible(true);
            player2Score.setVisible(true);
            //player2Image.setVisible(true);
            player2ImageCircle.setVisible(true);
            playersPosition[id] = 2;
            playersNames[id] = player2Name;
            playersScores[id] = player2Score;
            playersImagesCircles[id] = player2ImageCircle;
            playersImages[id] = player2Image;


        }
        else if(player3Name.getText().equals("")){
            player3Name.setText(name);
            player3Score.setText(0+" pts");
            player3Name.setVisible(true);
            player3Score.setVisible(true);
            //player3Image.setVisible(true);
            player3ImageCircle.setVisible(true);
            playersPosition[id] = 3;
            playersNames[id] = player3Name;
            playersScores[id] = player3Score;
            playersImagesCircles[id] = player3ImageCircle;
            playersImages[id] = player3Image;
        }
    }
}




