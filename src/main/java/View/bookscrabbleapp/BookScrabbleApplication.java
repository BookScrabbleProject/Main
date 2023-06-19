package View.bookscrabbleapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookScrabbleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BookScrabbleApplication.class.getResource("welcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        stage.setTitle("BookScrabble!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}