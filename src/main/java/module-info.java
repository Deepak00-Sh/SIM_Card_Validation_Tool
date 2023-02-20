module com.mannash.javafxapplication.simcardvalidationtool {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mannash.javafxapplication.simcardvalidationtool to javafx.fxml;
    exports com.mannash.javafxapplication.simcardvalidationtool;
}