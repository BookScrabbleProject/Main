module com.example.bookscrabbleapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bookscrabbleapp to javafx.fxml;
    exports com.example.bookscrabbleapp;
}