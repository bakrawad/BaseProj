module com.example.baseproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.baseproj to javafx.fxml;
    exports com.example.baseproj;
}