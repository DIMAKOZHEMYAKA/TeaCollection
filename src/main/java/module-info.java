module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;


    opens org.example.application to javafx.fxml;
    exports org.example.application;

    exports org.example.models to com.fasterxml.jackson.databind;

}