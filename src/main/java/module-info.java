module com.example.bookscrabbleapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens View.bookscrabbleapp to javafx.fxml;
    exports View.bookscrabbleapp;
}