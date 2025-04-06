module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.demo7 to javafx.fxml;
    exports com.example.demo7;
}