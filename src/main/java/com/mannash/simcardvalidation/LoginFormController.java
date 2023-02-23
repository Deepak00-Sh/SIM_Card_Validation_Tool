package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.ResponseAuthenticationPojo;
import com.mannash.simcardvalidation.service.SimVerifyServerCommunicationService;
import com.mannash.simcardvalidation.service.SimVerifyServerCommunicationServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginFormController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField user_input;

    @FXML
    private PasswordField password_input;
    @FXML
    private TextField testTextField;

    @FXML
    private void onLoginPress() throws IOException {

        SimVerifyServerCommunicationService simVerifyServerCommunicationService = new SimVerifyServerCommunicationServiceImpl();

        String userId = user_input.getText();
        String password = password_input.getText();
        ResponseAuthenticationPojo responseAuthenticationPojo=simVerifyServerCommunicationService.authenticateClient(userId,password);
//        testTextField.setText("response code : "+responseAuthenticationPojo.getStatusCode()+"\n");
//        testTextField.setText("response message : "+responseAuthenticationPojo.getMessage());
        responseAuthenticationPojo.setUserName(userId);

        if (responseAuthenticationPojo.getStatusCode() != 200) {


        } else {

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

//        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
////        Scene primaryScene = loginButton.getScene();
//        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
//        primaryStage.close();
////        primaryStage.setScene(primaryScene);
//        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtel_logo.png");
//        Stage stage = new Stage();
//        Scene scene = new Scene(mainPage);
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
//        stage.setTitle("SIM Verify!");
//        stage.show();

    }
}
