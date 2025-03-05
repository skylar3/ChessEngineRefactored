module com.example.chessenginerefactored {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.chessenginerefactored to javafx.fxml;
    exports com.example.chessenginerefactored;
}