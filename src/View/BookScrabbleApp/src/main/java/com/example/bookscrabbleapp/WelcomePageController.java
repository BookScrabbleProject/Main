package com.example.bookscrabbleapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.regex.Pattern;

public class WelcomePageController {
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
    Boolean isHost=false;



    public void joinBtnHandler(){
        ipInput.setText("");
        portInput.setText("");
        nameInput.setText("");
        ipDiv.setVisible(true);
        ipWarningDiv.setVisible(false);
        portWarningDiv.setVisible(false);
        nameWarningLabel.setVisible(false);
        isHost=false;
        initBtn.setText("Join");
        createBtn.setStyle("-fx-background-color:GREY");
        joinBtn.setStyle("-fx-background-color:LightBlue");
    }
    public void createBtnHandler(){
        ipInput.setText("");
        portInput.setText("");
        nameInput.setText("");
        ipDiv.setVisible(false);
        ipWarningDiv.setVisible(false);
        portWarningDiv.setVisible(false);
        nameWarningLabel.setVisible(false);
        isHost=true;
        initBtn.setText("Open Lobby");
        createBtn.setStyle("-fx-background-color:LightBlue");
        joinBtn.setStyle("-fx-background-color:GREY");
    }


    public void initBtnHandler(ActionEvent actionEvent) {
       boolean isError=false;
        if(!validName(nameInput.getText())) {
            nameWarningLabel.setVisible(true);
            isError=true;
       }
        else
            nameWarningLabel.setVisible(false);
       if(!validPort(portInput.getText())) {
           portWarningDiv.setVisible(true);
           isError=true;
       }
       else
           portWarningDiv.setVisible(false);
       if(!isHost) {
           if (!validIpAddress(ipInput.getText())){
               ipWarningDiv.setVisible(true);
               isError=true;
           }
           else
               ipWarningDiv.setVisible(false);
       }
       if(isError)
           return;
        else {
            Label l = new Label("Melech");
            userInputDiv.getChildren().add(l);
        }
    }
    private boolean validName(String name){
        return !name.equals("");
    }
    private boolean validPort(String port){
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
        if (Integer.parseInt(portInput.getText().trim()) < 1000 || Integer.parseInt(portInput.getText().trim()) > 10000) // the number isn't a valid port number
        {
            System.out.println(Integer.parseInt(portInput.getText().trim()));
            portWarningDiv.setVisible(true);
            return false;
        }
        return true;
    }
    private boolean validIpAddress(String ip){
        String IP_ADDRESS_PATTERN =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        if (!pattern.matcher(ipInput.getText()).matches()) {
            ipWarningDiv.setVisible(true);
            return false;
        }
        return true;
    }
}
