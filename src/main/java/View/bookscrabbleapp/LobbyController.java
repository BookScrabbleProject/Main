package View.bookscrabbleapp;
import ViewModel.*;
import View.LoginData;
import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import General.MethodsNames;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class LobbyController implements Initializable, Observer {
    @FXML
    Button startGameBtn;
    @FXML
    Label player1Name;
    @FXML
    Label player2Name;
    @FXML
    Label player3Name;
    @FXML
    Label player4Name;
    @FXML
    Label ip;
    @FXML
    Label port;
    @FXML
    Text welcomeText;

    LoginData ld = LoginData.getLoginData();
    Stage currentStage = new Stage();
    public void startGame(MouseEvent mouseEvent) {
        ViewModel.getViewModel().startGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewModel.getViewModel().addObserver(this);
        if(ld.getIsHost()){
            startGameBtn.setVisible(true);
            player1Name.setText("Me");
        }
        else
            handlerPlayerList();
        ip.setText(ld.getIp());
        port.setText(String.valueOf(ld.getPort()));
    }
    public void handlerPlayerList(){
        String[] arr = new String[4];
        for (int i = 0; i < 4; i++)
            arr[i] = "";
        for (int i = 0; i < ViewModel.getViewModel().getPlayers().size(); i++)
            arr[i] = ViewModel.getViewModel().getPlayers().get(i).getName();
        player1Name.setText(arr[0]);
        player2Name.setText(arr[1]);
        player3Name.setText(arr[2]);
        player4Name.setText(arr[3]);
    }
    public void moveInGameScene(){
        try {
            //close my window
            Stage stage = (Stage) startGameBtn.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("inGame.fxml"));
            stage = new Stage();
            stage.setTitle("BookScrabble!");
            stage.setScene(new Scene(root, 1400, 1000));
            stage.setOnCloseRequest( event -> {
                System.out.println("Closing Stage");
                ViewModel.getViewModel().close();
                System.exit(0);
            } );
            stage.show();
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    @Override
    public void update(Observable o, Object arg) {
        String args =   (String)arg;
        switch (args){
            case MethodsNames.PLAYERS_LIST_UPDATED:
                Platform.runLater(()->handlerPlayerList());
                break;
            case MethodsNames.START_GAME:
                Platform.runLater(()->moveInGameScene());
                break;
        }
    }
}
