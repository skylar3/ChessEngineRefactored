module com.example.chessenginerefactored {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chessenginerefactored to javafx.fxml;
    exports com.example.chessenginerefactored;
}