module com.example.bookscrabbleapp {
    requires javafx.controls;
    requires javafx.fxml;

    exports ViewModel;
    opens ViewModel to javafx.fxml;
    opens View.bookscrabbleapp to javafx.fxml;
    exports View.bookscrabbleapp;
}