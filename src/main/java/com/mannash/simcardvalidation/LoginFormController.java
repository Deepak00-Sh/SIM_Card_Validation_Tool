package com.mannash.simcardvalidation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginFormController {
    @FXML
    private Button loginButton;

    @FXML
    private ImageView img_status;

    @FXML
    private void onLoginPress() throws IOException {
        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
//        Scene primaryScene = loginButton.getScene();
          Stage primaryStage = (Stage) loginButton.getScene().getWindow();
          primaryStage.close();


        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtel_logo.png");
        Stage stage = new Stage();
        Scene scene = new Scene(mainPage,850,550);
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);

        stage.setTitle("SIM Verify!");
        stage.show();
    }

}

