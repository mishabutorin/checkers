module com.example.help {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.example.help to javafx.fxml;
    exports com.example.help;
}