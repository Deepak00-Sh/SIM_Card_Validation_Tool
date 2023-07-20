package com.mannash.simcardvalidation;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.common.UpdateRequestType;
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


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

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

    String libFolder = "..\\lib\\";
    String licenseFile = libFolder + "License.lic";
    String licenseKey = "simVerifyMannash@#2023";
    static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int ITERATION_COUNT = 65536; // Adjust this as needed
    private static final int KEY_LENGTH = 256;

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

            updateVersionOnServerAndFlagInLocalFile(userId);

            startTransferDataThread(LoginFormController.loggedInUserName);

            try {
                writeConfigFile("", "1", userId);
            } catch (Exception e) {
                System.out.println("Server not responding for writing config");
//                e.printStackTrace();
            }

        }

    }

    private void updateVersionOnServerAndFlagInLocalFile(String userId) {
        TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
        CheckUpdate checkUpdate = new CheckUpdate();
        ResponseUserDataInfos responseUserDataInfos = trakmeServerCommunicationService.getUserByEmail(userId);
        if (responseUserDataInfos != null){
            int id = responseUserDataInfos.getId();
            int flag = responseUserDataInfos.getResponseUserDataPojo().getUsrDataFlag();
            String localVersion = checkUpdate.getCurrentVersion();
            int versionUpdateStatusCode = trakmeServerCommunicationService.updateUserData(userId, id, UpdateRequestType.VERSION.toString(),1, localVersion);
            if (versionUpdateStatusCode == 200){
                System.out.println("Version updated successfully on server. Status code : "+versionUpdateStatusCode);
            }else {
                System.out.println("Version could not be updated on server. Status code : "+versionUpdateStatusCode);
            }
            System.out.println("FLAG : "+flag);
            if (flag != 0 && flag != 1){
                updateLocalFlag(1, userId);
            }else {
                updateLocalFlag(flag, userId);
            }
        }
    }

    private void updateLocalFlag(int usrDataFlag, String userId) {
        File lib = new File(libFolder);
        if (!lib.exists()){
            lib.mkdir();
        }

        File license = new File(licenseFile);
        if (!license.exists()){
            try {
                license.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String data = userId + "_" + usrDataFlag;
        try {
            encryptAndUpdateData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptAndUpdateData(String data){
        try {
            SecretKey secretKey = generateSecretKey();
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            // Update the encrypted data in the file
            Path file = Paths.get(licenseFile);
            Files.write(file, Base64.getEncoder().encode(encryptedBytes), StandardOpenOption.TRUNCATE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String readAndDecryptData() {
        try {
            SecretKey secretKey = generateSecretKey();
            // Read the encrypted data from the file
            byte[] encryptedBytes = Files.readAllBytes(Paths.get(licenseFile));

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedBytes));
            return new String(decryptedBytes);
        }catch (Exception e){
            return null;
        }
    }

    private SecretKey generateSecretKey() {
        SecretKeyFactory factory = null;
        try {
            try {
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        KeySpec spec = new PBEKeySpec(licenseKey.toCharArray(), "salt".getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = null;
        try {
            tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return new SecretKeySpec(tmp.getEncoded(), ENCRYPTION_ALGORITHM);
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
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
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

        System.out.println(" response 1 : "+serverResponse);
        System.out.println(" response 2 : "+serverResponse2);
        System.out.println(" response 3 : "+serverResponse3);

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
