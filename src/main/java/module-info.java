module potato {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;


    opens potato to javafx.fxml;
    exports potato;

    exports potato.models to com.fasterxml.jackson.databind;
}