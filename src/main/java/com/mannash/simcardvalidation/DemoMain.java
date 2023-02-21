package com.mannash.simcardvalidation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DemoMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader testingLoader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/TestingViewDemo.fxml"));
        Parent testLoader = testingLoader.load();
        Scene testScene = new Scene(testLoader);
        stage.setScene(testScene);
        stage.show();

    }
}
