package com.mannash.simcardvalidation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUpdate {

    private static final String VERSION_FILE_URL = "http://trakme.mannash.com/trakmeserver/simverify/singleCard/version.txt"; // URL of version file on server
    private static final String JAR_FILE_URL = "http://trakme.mannash.com/trakmeserver/simverify/singleCard/SIMCardValidationTool-1.0-SNAPSHOT.jar";
    private static final String LOCAL_FILE_PATH = "..\\lib\\SIMCardValidationTool-1.0-SNAPSHOT.jar";
    private static final String LOCAL_JAR_DOWNLOAD_PATH = "..\\updates\\SIMCardValidationTool-1.0-SNAPSHOT.jar";
    private String currentVersion;
    private String latestVersion;
    String versionFilePath = "..\\config\\version.txt";

    public String getLatestVersion() throws IOException {
        URL url = new URL(VERSION_FILE_URL);
        Scanner scanner = new Scanner(url.openStream());
        String latestVersion = scanner.nextLine();
        scanner.close();
        return latestVersion;
    }

    public String getCurrentVersion() {
        InputStream inputStream = CheckUpdate.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);

        } catch (IOException e) {
            System.out.println("Application.properties file not found!!");
            e.printStackTrace();
        }
        System.out.println("Version is : " + properties.getProperty("version"));
        return properties.getProperty("version");

    }

    public void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Available");
        ButtonType downloadButton = new ButtonType("Download");
        ButtonType noThanks = new ButtonType("No, thanks", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.setContentText("A new version of the app is available.\n" +
                "Do you want to download and install it now?");
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().setAll(downloadButton, noThanks);
        alert.showAndWait().ifPresent(button -> {
            if (button == downloadButton) {
                downloadJarFile();
            }
        });
    }

    public void downloadOnStart(){
        try {
            // create updates directory if it doesn't exist
            Path updateDir = Paths.get("..\\updates");
            if (!Files.exists(updateDir)) {
                Files.createDirectories(updateDir);
            }
            // download JAR file to updates directory
            Path updateFilePath = Paths.get(LOCAL_JAR_DOWNLOAD_PATH);
            URL url = new URL(JAR_FILE_URL);
            Files.copy(url.openStream(), updateFilePath, StandardCopyOption.REPLACE_EXISTING);
            // show download complete message and close the application
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Download Complete");
//            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
//            alert.setHeaderText("The update has been downloaded.");
//            alert.setContentText("Please restart the application to apply the update.");
//            alert.showAndWait();
//
//            //updating new version in config
////            updateNewVersionInConfig("1.1");
//            //closing application
//            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Download Failed");
//            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
//            alert.setHeaderText("Failed to download the update.");
//            alert.setContentText("Please try again later.");
//            alert.showAndWait();
        }
    }

    public void downloadJarFile() {
        try {
            // create updates directory if it doesn't exist
            Path updateDir = Paths.get("..\\updates");
            if (!Files.exists(updateDir)) {
                Files.createDirectories(updateDir);
            }
            // download JAR file to updates directory
            Path updateFilePath = Paths.get(LOCAL_JAR_DOWNLOAD_PATH);
            URL url = new URL(JAR_FILE_URL);
            Files.copy(url.openStream(), updateFilePath, StandardCopyOption.REPLACE_EXISTING);
            // show download complete message and close the application
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Complete");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("The update has been downloaded.");
            alert.setContentText("Please restart the application to apply the update.");
            alert.showAndWait();

            //updating new version in config
//            updateNewVersionInConfig("1.1");
            //closing application
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("Failed to download the update.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }

}