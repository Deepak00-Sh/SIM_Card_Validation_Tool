package com.mannash.simcardvalidation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginFormController {
    @FXML
    private Button loginButton;

    @FXML
    private void onLoginPress() throws IOException {
        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
//        Scene primaryScene = loginButton.getScene();
          Stage primaryStage = (Stage) loginButton.getScene().getWindow();
          primaryStage.close();
//        primaryStage.setScene(primaryScene);
        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtel_logo.png");
        Stage stage = new Stage();
        Scene scene = new Scene(mainPage);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle("SIM Verify!");
        stage.show();



    }
}
