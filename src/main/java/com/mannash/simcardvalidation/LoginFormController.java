package com.mannash.simcardvalidation;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.pojo.*;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationService;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
//import com.mannash.simcardvalidation.zzzservice2.SimVerifyServerCommunicationService;
//import com.mannash.simcardvalidation.zzzservice2.SimVerifyServerCommunicationServiceImpl;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    private Label promptLabel;
    String userId = null;
    UserInfo userInfo;
    public static String loggedInUserName;

    @FXML
    private void onLoginPress() throws IOException {
//        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
////        Scene primaryScene = loginButton.getScene();
//        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
//        primaryStage.close();
////        primaryStage.setScene(primaryScene);
//        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
//        Stage stage = new Stage();
//        Scene scene = new Scene(mainPage);
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
//        stage.setTitle("SIM Verify!");
//        stage.show();


        TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();

        String userId = user_input.getText();
        String password = password_input.getText();
//        String userId="piyush.srivastava@mannash.com";
//        String password="TMe#2020";

        String hardCodeUserId = "store_user@mannash.com";
        String hardCodePassword = "12345";

//        String encodedUserid =  org.apache.commons.codec.digest.DigestUtils.sha256Hex(userId);
//        String encodedPwd =  org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);

        LoginFormController.loggedInUserName = userId;

        ResponseAuthenticationPojo responseAuthenticationPojo = trakmeServerCommunicationService.authenticateClient(userId, password);

        if (responseAuthenticationPojo.getStatusCode() != 200) {
            promptLabel.setTextFill(Color.rgb(255, 0, 0));
            promptLabel.setText("Invalid username or password!");
        } else {

            loadTestingWindowData();

            startTransferDataThread(LoginFormController.loggedInUserName);

            try {
                writeConfigFile("", "1", userId);
            } catch (Exception e) {
                System.out.println("Server not responding for writeing config");
//                e.printStackTrace();
            }

        }

    }

    private void loadTestingWindowData() {
        Parent mainPage = null;
        try {
            mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
//        Scene primaryScene = loginButton.getScene();
        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
        primaryStage.close();
//        primaryStage.setScene(primaryScene);
        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
        Stage stage = new Stage();
        Scene scene = new Scene(mainPage);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("SIM Verify-v"+LoginForm.getVersion());
//            stage.setResizable(false);

//            MenuButton logOut = (MenuButton) scene.lookup("#logOut");
//            logOut.setText(userId);
        stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTransferDataThread(String loggedInUserName) {
        SimVerifyTransferDataToServerThread transferDataThread = new SimVerifyTransferDataToServerThread(loggedInUserName);
        transferDataThread.start();
    }

    private void writeConfigFile(String terminalCardICCID, String woId2, String userName) throws IOException {
        TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();

        ResponseProfileTestingConfig serverResponse = trakmeServerCommunicationService.getProfileTestingConfig(terminalCardICCID, woId2, userName);

        ResponseStressTestingConfig serverResponse2 = trakmeServerCommunicationService.getStressTestingConfig(terminalCardICCID, woId2, userName);

        ResponseTestingConfig serverResponse3 = trakmeServerCommunicationService.getTestingConfig(terminalCardICCID, woId2, userName);


        if (serverResponse == null) {
            System.out.println("server response is null");
        }

        if (serverResponse != null && serverResponse2 != null && serverResponse3 != null) {
            File config = new File("..\\config\\testingConfig.conf");
            if (!config.exists()) {
                config.createNewFile();
            }

            try {
                FileWriter fileWriter = new FileWriter(config);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write("{\n\"fileSystemConfig\": " + new Gson().toJson(serverResponse.getFileSystemConfig()) + ",\n\"fileContentConfig\": " + new Gson().toJson(serverResponse.getFileContentConfig()) +
                        ",\n\"fileVerificationSystemConfig\": " + new Gson().toJson(serverResponse3.getFileVerificationSystemConfigs()) + ",\n\"fileVerificationContentConfig\": " + new Gson().toJson(serverResponse3.getFileVerificationContentConfigs()) +
                        ",\n\"apduList\": " + new Gson().toJson(serverResponse2.getApduList()) + ",\n\"loopCount\": " + new Gson().toJson(serverResponse2.getLoopCount()) + "\n}");

                bufferedWriter.close();
                System.out.println("Successfully wrote to file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleKeyForPassword(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            loginButton.fire();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB && event.isShiftDown()) {
            event.consume();
            user_input.requestFocus();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
            event.consume();
            loginButton.requestFocus();
        }
    }

    @FXML
    private void handleKeyForUserInput(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            loginButton.fire();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB && event.isShiftDown()) {
            event.consume();
            loginButton.requestFocus();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
            event.consume();
            password_input.requestFocus();
        }
    }

    @FXML
    private void handleKeyForLoginButton(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            loginButton.fire();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB && event.isShiftDown()) {
            event.consume();
            password_input.requestFocus();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
            event.consume();
            user_input.requestFocus();
        }
    }

    public static void main(String[] args) throws IOException {
        LoginFormController controller = new LoginFormController();
        controller.onLoginPress();
    }
}
