package View.bookscrabbleapp;

import ViewModel.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.application.Preloader;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import General.MethodsNames;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

//testing
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
    @FXML
    private Text waitingChallengeText;

    boolean isGameStarted = false;
    private Alert challengeAlert;
    private Image userIcon = new Image(getClass().getResource("/Images/userIcon.jpg").toExternalForm());

    //in every index we can find the player's id. the first player that we see in the game is the player in index 0 with id X
    int[] playersPosition = new int[4];
    Label[] playersNames = new Label[4];
    Label[] playersScores = new Label[4];
    ImageView[] playersImages = new ImageView[4];
    Circle[] playersImagesCircles = new Circle[4];
    final int BOARD_HGAP = 11;
    final int BOARD_VGAP = 4;
    final int HAND_HGAP = 20;
    boolean isBoardEmpty = true;

    double SQUARE_WIDTH; //the width of each square in the board
    double SQUARE_HEIGHT; //the height of each square in the board


    Character lastPickedTile = null;
    int lastPickedTileIndex = -1;
    Character[][] boardStatus = new Character[15][15];


    ArrayList<DataChanges> dataChangesList = new ArrayList<>();
    ArrayList<Integer> indexChangesList = new ArrayList<>();

    /**
     * this method is called when the user clicks on the board. it checks if the click was on a square in the board
     * it checks if the user has the turn to play.
     * it checks if the user has already picked a tile from his hand and if so, it puts the tile on the board
     * @param e the mouse event
     */
    public void boardClickHandler(MouseEvent e){

        if(ViewModel.getViewModel().getMyPlayer().getId()!=ViewModel.getViewModel().getCurrentPlayerId())
            return;
        if(lastPickedTile== null || lastPickedTileIndex == -1)
            return;
        double MOUSE_CLICKED_X = e.getX(); //the y of the mouse click relative to the scene
        double MOUSE_CLICKED_Y = e.getY(); //the x of the mouse click relative to the scene

        System.out.println("x: " + MOUSE_CLICKED_X + " y: " + MOUSE_CLICKED_Y);
        SQUARE_WIDTH = (gridPane.getWidth()-BOARD_HGAP*14) / 15;
        SQUARE_HEIGHT= (gridPane.getHeight()-BOARD_VGAP*14) / 15; //the height of each square in the board

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
        addTileToBoard(lastPickedTile, lastPickedTileIndex, cr, cc);
        if(!boardImage.isVisible())
            boardImage.setVisible(true);
        if(resetBtn.isDisable() || undoBtn.isDisable() || finishTurnBtn.isDisable()) {
            setDisableButtons(false);

        }

//        if(!isGameStarted) {
//            char myC = 'a';
//            for(int i=0; i<15;i++){
//                for(int j=0; j<15;j++){
//                    Image tile = new Image(getClass().getResource("/Images/Tiles/"+myC+"Letter.png").toExternalForm());
//                    myC++;
//                    if(myC>'z') myC = 'a';
//                    ImageView iv = new ImageView(tile);
//                    iv.setFitWidth(SQUARE_WIDTH-3);
//                    iv.setFitHeight(SQUARE_HEIGHT-2);
//                    StackPane sp = new StackPane(iv);
//                    sp.setAlignment(Pos.CENTER);
//                    gridPane.add(sp, j, i);
//                    System.out.println(i+","+j);
//                    System.out.println("Added stackpane to gridpane");
//                }
//            }
//        }


    }

    private void newPlayerTurn() {
        waitingChallengeText.setVisible(false);
        tilesInHandGrid.setDisable(false);
        dataChangesList.clear();
        indexChangesList.clear();
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

        }
        else{
            setDisableButtons(true);
        }
        setHand();

    }

    /**
     * this method is called when the view model notifies the view that the current player's hand has changed
     * it updates the hand of the current player from the view model
     */
    private void setHand() {
        double HAND_SQUARE_WIDTH = (tilesInHandGrid.getWidth()-HAND_HGAP*6) / 7; //the width of each square
        double HAND_SQUARE_HEIGHT = (tilesInHandGrid.getHeight()); //the height of each square
        if(tilesInHandGrid.getChildren().size() > 0)
            tilesInHandGrid.getChildren().clear();
        if(ViewModel.getViewModel().getMyPlayer().getHand() == null || ViewModel.getViewModel().getMyPlayer().getHand().size()==0 ) return;
        System.out.println(String.join(",", ViewModel.getViewModel().getMyPlayer().getHand().stream().map(c->c.toString()).collect(Collectors.toList())));
        for(int i = 0; i< ViewModel.getViewModel().getMyPlayer().getHand().size(); i++) {
            Character myC = ViewModel.getViewModel().getMyPlayer().getHand().get(i);
            Image tile = new Image(getClass().getResource("/Images/Tiles/" + myC + "Letter.png").toExternalForm());
            ImageView iv = new ImageView(tile);
            iv.setFitWidth(HAND_SQUARE_WIDTH);
            iv.setFitHeight(HAND_SQUARE_HEIGHT);
            StackPane sp = new StackPane(iv);
            sp.setAlignment(Pos.CENTER);
            int finalI = i;
            sp.setOnMouseClicked((event)-> {
                if(sp.isVisible())
                    tileInHandClickHandler(event, finalI, myC);
            });
            sp.setVisible(true);
            tilesInHandGrid.add(sp, i, 0);

            System.out.println("Added stackpane to hand gridpane");
        }
    }


    /**
     * this method is called when the user clicks on a tile in his hand - it selects the tile and deselects the rest (and keep the character and the index in a variable);
     * @param event the mouse click
     * @param index the index of the triggered tile
     * @param myC the character of the triggered tile
     */
    private void tileInHandClickHandler(MouseEvent event, int index, Character myC) {
        if(ViewModel.getViewModel().getMyPlayer().getId()!=ViewModel.getViewModel().getCurrentPlayerId())
            return;
        if(event.getButton() == MouseButton.PRIMARY) {
            for(int i=0; i<7; i++){
                if(i==index && tilesInHandGrid.getChildren().get(i).getStyle().equals("")){ //if the tile is not selected
                    tilesInHandGrid.getChildren().get(i).setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 20, 0, 0, 0);");
                }
                else{
                    tilesInHandGrid.getChildren().get(i).setStyle("");
                }
            }
            if(lastPickedTileIndex == index){
                lastPickedTileIndex = -1;
                lastPickedTile = null;
            }
            else{
                lastPickedTileIndex = index;
                lastPickedTile = myC;
            }

        }
    }

    /**
     * this method is called when a user wants to add a tile to the board. it updated the board status and the hand status
     * @param letter the letter of the tile
     * @param indexInHand the index of the tile in the hand
     * @param row the row of the desire placement in the board
     * @param col the column of the desire placement in the board
     */
    private void addTileToBoard(Character letter, int indexInHand, int row, int col ){
        if(letter==null || indexInHand<0 || indexInHand >=7 || row>14 || row<0 || col>14 || col<0) return;
        if(boardStatus[row][col]!=null && boardStatus[row][col].charValue()>='A' && boardStatus[row][col].charValue()<='Z') return;
        if(boardStatus[row][col]!=null && boardStatus[row][col].charValue()>='a' && boardStatus[row][col].charValue()<='z') return;
        tilesInHandGrid.getChildren().get(indexInHand).setVisible(false);
        tilesInHandGrid.getChildren().get(indexInHand).setStyle("");
        boardStatus[row][col] = letter;
//        Image tile = new Image(getClass().getResource("/Images/Tiles/" + letter + "Letter.png").toExternalForm());
//        ImageView iv = new ImageView(tile);
//        iv.setFitWidth(SQUARE_WIDTH-3);
//        iv.setFitHeight(SQUARE_HEIGHT-2);
//        StackPane sp = new StackPane(iv);
//        sp.setAlignment(Pos.CENTER);
//        gridPane.add(sp, col, row);
        drawBoard();
        dataChangesList.add(new DataChanges(letter, row, col));
        indexChangesList.add(indexInHand);
        lastPickedTile = null;
        lastPickedTileIndex = -1;


    }

    /**
     * this method is called when a user got a positive answer from the server about his move, and it lets the other players to challenge the server decision.
     * @param words the words that were created during the turn
     */
    public void showChallengeAlert(String... words){
        challengeAlert = new Alert(Alert.AlertType.INFORMATION);
        challengeAlert.setTitle("Challenge");
        challengeAlert.setHeaderText("Would You Like To Challenge The Player?");
        challengeAlert.setContentText("The List Of Words That Were Created During This Turn:");
        VBox wordsCreated=new VBox();
        for(String word : words)
        {
            Button b=new Button(word);
            b.setOnMouseClicked((event)-> {
                ViewModel.getViewModel().challenge(word);
                ((Stage) challengeAlert.getDialogPane().getScene().getWindow()).close();
            });
            wordsCreated.getChildren().add(b);
        }
        wordsCreated.setSpacing(10);
        challengeAlert.getDialogPane().setContent(wordsCreated);
        ButtonType buttonTypeOne = new ButtonType("Close");
        challengeAlert.getButtonTypes().setAll(buttonTypeOne);
        challengeAlert.setOnCloseRequest(event -> {
            System.out.println("Alert Closed");
            ((Stage) challengeAlert.getDialogPane().getScene().getWindow()).close();
        });
        challengeAlert.showAndWait();
        //todo show to all the players the score that the player will get
    }

    /**
     * This alert is triggered when the host disconnects from the game, it redirects the guest to the login page
     */

    public void showDisconnectionAlert(){
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

    /**
     * This method redirects the user to the login page
     */
    public void moveToLoginScene(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
            //close the current window and change to the root
            Scene scene = new Scene(root, 650, 500);
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

    /**
     * This method is called when the InGame scene is initialized. in this method - all the required parameters are initialized before starting the game.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewModel.getViewModel().addObserver(this);
        challengeAlert = new Alert(Alert.AlertType.INFORMATION);
        for(int i=0;i<3;i++){
            playersPosition[i] = -1;
        }
        isGameStarted = true;
        setHand();
        fillPlayersDetails();
        myPlayerImageCircle.setFill(new ImagePattern(userIcon));

        numOfTilesInHand.textProperty().bind(ViewModel.getViewModel().getMyPlayer().numberOfTilesProperty);
        numOfTilesInBag.textProperty().bind(ViewModel.getViewModel().numberOfTilesInBagProperty);
        myPlayerScore.textProperty().bind(ViewModel.getViewModel().getMyPlayer().scoreProperty);
        newPlayerTurn();
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                boardStatus[i][j] = '_';
            }
        }

    }

    /**
     * This method is called when the game starts, and it fills the players details in the window.
     */
    private void fillPlayersDetails() {
        int myID = ViewModel.getViewModel().getMyPlayer().getId();
        for(int i=0;i<=3;i++){
            if(ViewModel.getViewModel().getPlayers().get(i) == null) continue;
            if(i == myID) {
                playersPosition[i]=4;
                playersNames[i] = myPlayerName;
                playersImagesCircles[i] = myPlayerImageCircle;
                playersImages[i] = myPlayerImage;
                continue;
            }
            String name = ViewModel.getViewModel().getPlayers().get(i).getName();
            insertPlayerDetails(name, i);
        }
    }

    /**
     * This method place the user details on the left side of the screen and binds the details to the player's details in the ViewModel.
     * @param name the name of the player
     * @param id the id of the player
     */
    public void insertPlayerDetails(String name, int id){
        if(player1Name.getText().equals("")){
            player1Name.setText(name);
            player1Score.textProperty().bind(ViewModel.getViewModel().getPlayers().get(id).scoreProperty);
            player1Name.setVisible(true);
            player1Score.setVisible(true);
            player1ImageCircle.setFill(new ImagePattern(userIcon));
            player1ImageCircle.setVisible(true);
            playersPosition[id] = 1;
            playersNames[id] = player1Name;
            playersScores[id] = player1Score;
            playersImagesCircles[id] = player1ImageCircle;
            playersImages[id] = player1Image;
        }
        else if(player2Name.getText().equals("")){
            player2Name.setText(name);
            ViewModel.getViewModel().getPlayers().get(id).scoreProperty.bindBidirectional(player2Score.textProperty());
            player2Name.setVisible(true);
            player2Score.setVisible(true);
            player2ImageCircle.setFill(new ImagePattern(userIcon));
            player2ImageCircle.setVisible(true);
            playersPosition[id] = 2;
            playersNames[id] = player2Name;
            playersScores[id] = player2Score;
            playersImagesCircles[id] = player2ImageCircle;
            playersImages[id] = player2Image;


        }
        else if(player3Name.getText().equals("")){
            player3Name.setText(name);
            ViewModel.getViewModel().getPlayers().get(id).scoreProperty.bindBidirectional(player3Score.textProperty());
            player3Name.setVisible(true);
            player3Score.setVisible(true);
            player3ImageCircle.setFill(new ImagePattern(userIcon));
            player3ImageCircle.setVisible(true);
            playersPosition[id] = 3;
            playersNames[id] = player3Name;
            playersScores[id] = player3Score;
            playersImagesCircles[id] = player3ImageCircle;
            playersImages[id] = player3Image;
        }
    }

    /**
     * this function is called when the user clicks on undo button
     * it will undo the last move that the user did
     * it will remove the last tile that the user put on the board and will return it to the hand
     */
    public void undoBtnClickHandler(){
        if(dataChangesList.size()==0 || indexChangesList.size()==0) return;
        DataChanges toRemove = dataChangesList.get(dataChangesList.size()-1);
        boardStatus[toRemove.getNewRow()][toRemove.getNewCol()]='_';
        drawBoard();
        tilesInHandGrid.getChildren().get(indexChangesList.get(indexChangesList.size()-1)).setVisible(true);
        dataChangesList.remove(dataChangesList.size()-1);
        indexChangesList.remove(indexChangesList.size()-1);
        if(dataChangesList.size()==0){
            setDisableButtons(true);
        }
    }

    /**
     * this function is called when the user clicks on reset button
     * it will undo all the moves that the user did
     * it will remove all the tiles that the user put on the board and will return them to the hand
     */
    public void resetBtnClickHandler(){
        while(dataChangesList.size()!=0){
            undoBtnClickHandler();
        }
    }

    /**
     * this function iterates through the boardStatus and draws the board
     */
    public void drawBoard(){
        gridPane.getChildren().clear();
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                if(boardStatus[i][j]!='_'){
                    System.out.println("boardStatus["+i+"]["+j+"] = "+boardStatus[i][j]);
                    System.out.println("boardStatus["+i+"]["+j+"] lowerCase = "+Character.toLowerCase(boardStatus[i][j]));
                    Image tile = new Image(getClass().getResource("/Images/Tiles/" + Character.toLowerCase(boardStatus[i][j]) + "Letter.png").toExternalForm());
                    ImageView iv = new ImageView(tile);
                    iv.setPreserveRatio(false);
                    iv.setFitWidth(SQUARE_WIDTH);
                    iv.setFitHeight(SQUARE_HEIGHT);
                    StackPane sp = new StackPane(iv);
                    sp.setAlignment(Pos.CENTER);
                    gridPane.add(sp, j, i);
                }
            }
        }
    }

    /**
     * this function is called when the user clicks on the finish my turn button
     * it will clear the changes list in the viewModel
     * it will send the changes that the user did to the viewModel and will try to place the word
     */
    public void finishMyTurnBtnHandler(){
        ViewModel.getViewModel().changesList.clear();
        for(DataChanges data: dataChangesList){
            ViewModel.getViewModel().changesList.add(data);
        }

        System.out.println("------------------View Board before try place word --------------------");
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                System.out.print(boardStatus[i][j]);
            }
            System.out.println("");
        }
        if(ViewModel.getViewModel().changesList.size()>0)
            ViewModel.getViewModel().tryPlaceWord();
    }

    /**
     * This method disables all the buttons
     */
    private void setDisableButtons(boolean disable){
        undoBtn.setDisable(disable);
        resetBtn.setDisable(disable);
        finishTurnBtn.setDisable(disable);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("View update "+arg);
        String[] splitedArgString = ((String)arg).split(":");
        String methodName = splitedArgString[0];
        String arguments=splitedArgString.length>1?splitedArgString[1]:"";
        switch (methodName){
            case MethodsNames.DISCONNECT_FROM_SERVER:
                System.out.println("In Case Disconnect");
                Platform.runLater(this::showDisconnectionAlert);
                System.out.println("Alert Created");
                break;
            case MethodsNames.SET_HAND:
                Platform.runLater(this::setHand);
                break;
            case MethodsNames.NEW_PLAYER_TURN:
                Platform.runLater(()->{
                    newPlayerTurn();
                });
                break;
            case MethodsNames.TRY_PLACE_WORD:
                Platform.runLater(()->{
                    tilesInHandGrid.setDisable(true);
                    System.out.println("In Try Place Word Case");
                    String[] splitedArguments=arguments.split(",");
                    if(splitedArguments[0].equals("0"))
                    {
                        tilesInHandGrid.setDisable(false);
                        //todo : need to make popUp for the player that his word is invalid
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid word");
                        alert.setHeaderText("Invalid word");
                        alert.setContentText("The word that you placed \\ created is invalid, please try again");
                        if(isBoardEmpty){
                            alert.setContentText(alert.getContentText() + "\n" + "You must place the first word on the star");
                        }
                        alert.showAndWait();
                    }
                    else{
                        setDisableButtons(true);
                        isBoardEmpty=false;
                        if(ViewModel.getViewModel().getCurrentPlayerId()!=ViewModel.getViewModel().getMyPlayer().getId()){
                            List<String>words=new ArrayList<>(splitedArguments.length-1);
                            for(int i=1;i<splitedArguments.length;i++){
                                words.add(splitedArguments[i]);
                            }
                            String[] wordsArray=new String[words.size()];
                            wordsArray=words.toArray(wordsArray);
                            showChallengeAlert(wordsArray);
                        }
                        else{
                            waitingChallengeText.setVisible(true);
                        }
                        dataChangesList.clear();
                        indexChangesList.clear();
                    }
                });
                break;
            case MethodsNames.CHALLENGE:
                System.out.println(arguments);
                String[] splitedArguments=arguments.split(",");
                if(splitedArguments[0].equals("0"))
                    Platform.runLater(()->{
                        if(ViewModel.getViewModel().getMyPlayer().getId()==ViewModel.getViewModel().getCurrentPlayerId())
                            resetBtnClickHandler();
                        Popup popup = new Popup();
                        popup.setX(300);
                        popup.setY(200);
                        popup.getContent().add(new Label("Someone challenged. The challenge was successful, the word will be removed from the board"));
                        PauseTransition visiblePause = new PauseTransition(
                                Duration.seconds(3)
                        );
                        visiblePause.setOnFinished(
                                event -> popup.hide()
                        );
                        popup.show(gridPane.getScene().getWindow());
                        visiblePause.play();
                    });
                else
                    Platform.runLater(()->{
                        Popup popup = new Popup();
                        popup.setX(300);
                        popup.setY(200);
                        popup.getContent().add(new Label("Someone challenged. The challenge was unsuccessful, the word will stay on the board"));
                        PauseTransition visiblePause = new PauseTransition(
                                Duration.seconds(3)
                        );
                        visiblePause.setOnFinished(
                                event -> popup.hide()
                        );
                        popup.show(gridPane.getScene().getWindow());
                        visiblePause.play();
                    });
                break;
            case MethodsNames.CLOSE_CHALLENGE_ALERT:
                Platform.runLater(()->{
                    if(challengeAlert.isShowing())
                        challengeAlert.close();
                });
                break;
            case MethodsNames.INVALID_PLACEMENT:
                //todo pop a popUp for the player that tried to put the word, that his placement is invalid
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Placement");
                    alert.setHeaderText("Invalid Placement");
                    alert.setContentText("You can't place the word in this way");
                    alert.showAndWait();
                });
                break;
            case MethodsNames.END_GAME:
                //todo pop a popUp that the game has ended->victory popUp
                //we get the players id-score list ordered by the descending scores
                Platform.runLater(()->{
                    String[] splitedArguments1=arguments.split(",");
                    List<String> playersIdScoreList=new ArrayList<>(splitedArguments1.length);
                    for(int i=0;i<splitedArguments1.length;i++){
                        playersIdScoreList.add(splitedArguments1[i]);
                    }
                    String[] playersIdScoreArray=new String[playersIdScoreList.size()];
                    playersIdScoreArray=playersIdScoreList.toArray(playersIdScoreArray);
                    ViewModel.getViewModel().close();
                    showEndGameAlert(playersIdScoreArray);
                });
                break;

            case MethodsNames.BOARD_UPDATED:
                Platform.runLater(()->{
                    copyBoardStatus();
                    drawBoard();
                });
                break;
        }

    }

    private void showEndGameAlert(String[] playersIdScoreArray) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Ended");
        alert.setHeaderText("Game Ended");
        String contentText="The winner is: ";
        for(int i=0;i<playersIdScoreArray.length;i++){
            String[] splitedPlayerIdScore=playersIdScoreArray[i].split("-");
            contentText+=playersNames[Integer.parseInt(splitedPlayerIdScore[0])].getText()+" with score: "+splitedPlayerIdScore[1];

            if(i!=playersIdScoreArray.length-1)
                contentText+="\n";
        }
        alert.setContentText(contentText);
        alert.setOnCloseRequest(event -> {

            ((Stage) alert.getDialogPane().getScene().getWindow()).close();
            Platform.runLater(this::moveToLoginScene);
            System.out.println("Alert Closed");
            ((Stage) alert.getDialogPane().getScene().getWindow()).close();
            Platform.runLater(this::moveToLoginScene);

        });
        alert.showAndWait();
    }

    private void copyBoardStatus() {
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                boardStatus[i][j]=ViewModel.getViewModel().getBoard()[i][j];
            }
        }
    }
}




