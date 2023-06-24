package View.bookscrabbleapp;
import General.MethodsNames;
import Model.GuestModel;
import Model.HostModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;
import View.LoginData;
import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class WelcomePageController implements Observer, Initializable {
    @FXML
    Button joinBtn;
    @FXML
    Button createBtn;
    @FXML
    Button initBtn;
    @FXML
    VBox userInputDiv;
    @FXML
    VBox ipWarningDiv;
    @FXML
    VBox portWarningDiv;
    @FXML
    TextField nameInput;
    @FXML
    TextField portInput;
    @FXML
    TextField ipInput;
    @FXML
    HBox nameDiv;
    @FXML
    HBox portDiv;
    @FXML
    HBox ipDiv;
    @FXML
    Label nameWarningLabel;
    @FXML
    Label serverWarning;
    @FXML
    AnchorPane welcomePane;
    Boolean isHost = false;

    /**
     * button handler that presents the Input fields for joining an existing lobby
     */
    public void joinBtnHandler() {
        if (!isHost)
            return;
        ipInput.setText("");
        portInput.setText("");
        nameInput.setText("");
        ipDiv.setVisible(true);
        ipWarningDiv.setVisible(false);
        portWarningDiv.setVisible(false);
        nameWarningLabel.setVisible(false);
        serverWarning.setVisible(false);
        isHost = false;
        initBtn.setText("Join");
        createBtn.setStyle("-fx-background-color:GREY");
        joinBtn.setStyle("-fx-background-color:LightBlue");
    }

    /**
     * button handler that presents the Input fields for creating a lobby
     */
    public void createBtnHandler() {
        if (isHost)
            return;
        ipInput.setText("");
        portInput.setText("");
        nameInput.setText("");
        ipDiv.setVisible(false);
        ipWarningDiv.setVisible(false);
        portWarningDiv.setVisible(false);
        nameWarningLabel.setVisible(false);
        serverWarning.setVisible(false);
        isHost = true;
        initBtn.setText("Open Lobby");
        createBtn.setStyle("-fx-background-color:LightBlue");
        joinBtn.setStyle("-fx-background-color:GREY");
    }

    /**
     * this function validates the user's input and if no problems with the input were detected,asks permission from the hostServer to connect
     * ,and redirects the user to the lobby page
     * @param actionEvent - the event that we caught
     *
     */
    public void initBtnHandler(ActionEvent actionEvent) {
        boolean isError = false;
        if (!validName(nameInput.getText())) {
            nameWarningLabel.setVisible(true);
            isError = true;
        } else
            nameWarningLabel.setVisible(false);
        if (!validPort(portInput.getText())) {
            portWarningDiv.setVisible(true);
            isError = true;
        } else
            portWarningDiv.setVisible(false);
        if (!isHost) {
            if (!validIpAddress(ipInput.getText())) {
                ipWarningDiv.setVisible(true);
                isError = true;
            } else
                ipWarningDiv.setVisible(false);
        }
        if (isError)
            return;
        else {
            if (isHost) {
                MyServer server = new MyServer(1235, new BookScrabbleHandler());
                server.start();
                System.out.println("Host");
                LoginData ld = LoginData.getLoginData();
                ld.setIp("127.0.0.1");
                ld.setPort(Integer.parseInt(portInput.getText()));
                ld.setIsHost(true);
                ld.setPlayerName(nameInput.getText());
                ViewModel vm = ViewModel.getViewModel();
                HostModel hm = HostModel.getHost();
                hm.setPlayerName(ld.getPlayerName());
                vm.resetModel();
                vm.setModel(hm);
                hm.connectToBookScrabbleServer(ld.getPort(), ld.getIp(), 1235);
                moveToLobby();
            } else {
                System.out.println("Client");
                LoginData ld = LoginData.getLoginData();
                ld.setIp(ipInput.getText());
                ld.setPort(Integer.parseInt(portInput.getText()));
                ld.setIsHost(false);
                ld.setPlayerName(nameInput.getText());
                ViewModel vm = ViewModel.getViewModel();
                GuestModel gm = new GuestModel(ld.getIp(), ld.getPort(), ld.getPlayerName());
                vm.resetModel();
                vm.setModel(gm);
                gm.connectToHostServer();
            }
        }
    }

    /**
     *
     * @param name - the user's name
     * @return - true if the name isn't empty,false otherwise
     */
    private boolean validName(String name) {
        return !name.equals("");
    }

    /**
     *
     * @param port- the host's port
     * @return -true if the port is valid,false otherwise
     */
    private boolean validPort(String port) {
        if (portInput.getText().equals("")) {
            portWarningDiv.setVisible(true);
            return false;
        }
        if (!portInput.getText().trim().matches("\\d+")) // the string contains a non digit character
        {
            System.out.println(portInput.getText().trim());
            portWarningDiv.setVisible(true);
            return false;
        }
        if (Integer.parseInt(portInput.getText().trim()) < 0 || Integer.parseInt(portInput.getText().trim()) > 65536) // the number isn't a valid port number
        {
            System.out.println(Integer.parseInt(portInput.getText().trim()));
            portWarningDiv.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     *
     * @param ip - the host's ip address
     * @return - true if the ip address is valid,false otherwise
     */
    private boolean validIpAddress(String ip) {
        String IP_ADDRESS_PATTERN =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        if (!pattern.matcher(ipInput.getText()).matches()) {
            ipWarningDiv.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * sets background image to the front page of the app
     * sets this class as an observer of the ViewModel
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewModel.getViewModel().addObserver(this);
        BackgroundImage myBI = new BackgroundImage(new Image("file:src/main/resources/Images/LobbyBG.jpeg", 800, 600, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        welcomePane.setBackground(new Background(myBI));


    }

    /**
     *
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     * example of usage:  methodName:argument1,argument2,argument3....argumentn
     */
    @Override
    public void update(Observable o, Object arg) {
        String[] arguments = ((String) arg).split(":");
        switch (arguments[0]) {
            case MethodsNames.CONNECT:
                if (arguments[1].equals("1"))
                    Platform.runLater(this::moveToLobby);
                else {
                    ViewModel.getViewModel().close();
                    ViewModel.getViewModel().resetModel();
                    serverWarning.setText("Server is full");
                    serverWarning.setVisible(true);
                }
                break;
            case MethodsNames.GAME_ALREADY_STARTED:
                ViewModel.getViewModel().close();
                ViewModel.getViewModel().resetModel();
                serverWarning.setText("Game already started");
                serverWarning.setVisible(true);
                break;

        }
    }

    /**
     * moves to the lobby screen of the application
     */
    private void moveToLobby() {
        try {
            //close my window
            Stage stage = (Stage) initBtn.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            stage = new Stage();
            stage.setTitle("BookScrabble!");
            stage.setScene(new Scene(root, 600, 450));
            stage.setOnCloseRequest(event -> {
                System.out.println("Closing Stage");
                ViewModel.getViewModel().close();
                System.exit(0);
            });
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
