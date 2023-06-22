package com.mannash.simcardvalidation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Properties;

import java.net.Authenticator;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.HttpURLConnection;

public class CheckUpdate {

    private static final String VERSION_FILE_URL = "http://103.228.113.86:32100/trakmeserver/simverify/singleCard/version.txt";
    // URL of version file on server
    private static final String USER_ACCESS_FILE = "http://103.228.113.86:32100/trakmeserver/simverify/singleCard/userAccess/admin.txt";
    private static final String JAR_FILE_URL = "http://103.228.113.86:32100/trakmeserver/simverify/singleCard/SIMCardValidationTool-1.0-SNAPSHOT.jar";
    private static final String LOCAL_FILE_PATH = "..\\lib\\SIMCardValidationTool-1.0-SNAPSHOT.jar";
    private static final String LOCAL_JAR_DOWNLOAD_PATH = "..\\updates\\SIMCardValidationTool-1.0-SNAPSHOT.jar";
    private String currentVersion;
    String latestVersion = null;
    String versionFilePath = "..\\config\\version.txt";

    Properties properties = new Properties();
    FileInputStream input = null;
    String filePath = "..\\config\\";

    String proxyUser = null;
    String proxyPassword = null;
    String proxyAddress = null;
    String proxyPort = null;
    String userAgent = null;
    String proxyEnabled = null;
    File file ;
    int timeout = 20000;

    public CheckUpdate(){
        file = new File(filePath+"proxy.properties");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        try {
            this.input = new FileInputStream(filePath+"proxy.properties");
            this.properties.load(input);
            this.proxyUser = this.properties.getProperty("proxyUser");
            this.proxyPassword = this.properties.getProperty("proxyPassword");
            this.proxyEnabled = this.properties.getProperty("proxyEnabled");
            this.proxyAddress = this.properties.getProperty("proxyAddress");
            this.proxyPort = this.properties.getProperty("proxyPort");

            System.out.println("proxyUser : "+this.proxyUser+" proxyPassword: "+this.proxyPassword+" proxyAddress : "+this.proxyAddress+" proxyPort : "+this.proxyPort);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        if (this.proxyEnabled.equalsIgnoreCase("yes")) {
            System.out.println("Proxy is enabled, going to authenticate the proxy.");
            this.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";
            System.setProperty("http.proxyHost", this.proxyAddress);
            System.setProperty("http.proxyPort", this.proxyPort);
            System.setProperty("http.proxyUser", this.proxyUser);
            System.setProperty("http.proxyPassword", this.proxyPassword);
            System.setProperty("http.agent", this.userAgent);

            String encodedUserPwd = new String(Base64.encodeBase64((this.proxyUser + ":" + this.proxyPassword).getBytes()));
            try {
                Authenticator.setDefault(new ProxyAuthenticator(this.proxyUser, this.proxyPassword));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public String getLatestVersion2() throws IOException {

        String line = null;

        try {
            URL url = new URL(VERSION_FILE_URL);

//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            String proxyUser = null;
            String proxyPassword = null;

//            System.setProperty("http.maxRedirects", "100");

            String proxyAddress = null;
            String proxyPort = null;

            try {
                input = new FileInputStream(filePath+"proxy.properties");
                properties.load(input);
                proxyUser = properties.getProperty("proxyUser");
                proxyPassword = properties.getProperty("proxyPassword");

                proxyAddress = properties.getProperty("proxyAddress");
                proxyPort = properties.getProperty("proxyPort");

                System.out.println("proxyUser : "+proxyUser+" proxyPassword: "+proxyPassword+" proxyAddress : "+proxyAddress+" proxyPort : "+proxyPort);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


//            InetSocketAddress sa = new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort));
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
//            conn.setInstanceFollowRedirects(false);
////        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//            String encodedUserPwd = new String(Base64.encodeBase64((proxyUser + ":" + proxyPassword).getBytes()));
//            conn.setRequestProperty("Accept-Charset", "UTF-8");
//            conn.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);


            final String authUser = "myuser";final String authPassword = "secret";

            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";

            System.setProperty("http.proxyHost", proxyAddress);
            System.setProperty("http.proxyPort", proxyPort );
            System.setProperty("http.proxyUser", proxyUser);
            System.setProperty("http.proxyPassword", proxyPassword);
            System.setProperty("http.agent", userAgent);
//            System.setProperty("http.auth.preference", "Negotiate");

            String encodedUserPwd = new String(Base64.encodeBase64((proxyUser+":"+proxyPassword).getBytes()));


            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
            conn = (HttpURLConnection) url.openConnection(proxy);
//            sun.net.www.protocol.http.HttpURLConnection httpConn = (sun.net.www.protocol.http.HttpURLConnection) conn;
//            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
//            HttpURLConnection

            conn.setRequestProperty("Authorization", "Negotiate" + encodedUserPwd);

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68");


//            httpConn.setAuthenticationProperty("Authorization", "Negotiate " + encodedUserPwd);

            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
//            latestVersion = line;
            reader.close();

// Disconnect the connection
            conn.disconnect();


//            Scanner scanner = new Scanner(url.openStream());
//            latestVersion = scanner.nextLine();
//            scanner.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return line;
    }

    public void proxyAuthentication(){
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        String proxyUser = null;
        String proxyPassword = null;

//            System.setProperty("http.maxRedirects", "100");

        String proxyAddress = null;
        String proxyPort = null;

        try {
            input = new FileInputStream(filePath+"proxy.properties");
            properties.load(input);
            proxyUser = properties.getProperty("proxyUser");
            proxyPassword = properties.getProperty("proxyPassword");

            proxyAddress = properties.getProperty("proxyAddress");
            proxyPort = properties.getProperty("proxyPort");

            System.out.println("proxyUser : "+proxyUser+" proxyPassword: "+proxyPassword+" proxyAddress : "+proxyAddress+" proxyPort : "+proxyPort);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


//            InetSocketAddress sa = new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort));
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
//            conn.setInstanceFollowRedirects(false);
////        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//            String encodedUserPwd = new String(Base64.encodeBase64((proxyUser + ":" + proxyPassword).getBytes()));
//            conn.setRequestProperty("Accept-Charset", "UTF-8");
//            conn.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);


        final String authUser = "myuser";final String authPassword = "secret";

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68";

        System.setProperty("http.proxyHost", proxyAddress);
        System.setProperty("http.proxyPort", proxyPort );
        System.setProperty("http.proxyUser", proxyUser);
        System.setProperty("http.proxyPassword", proxyPassword);
        System.setProperty("http.agent", userAgent);
//            System.setProperty("http.auth.preference", "Negotiate");

        String encodedUserPwd = new String(Base64.encodeBase64((proxyUser+":"+proxyPassword).getBytes()));


        Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));
    }

    public String getLatestVersion() throws IOException {

//        String version = null;

        try {
            URL url = new URL(VERSION_FILE_URL);

//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            HttpURLConnection conn = null;
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
            conn = (HttpURLConnection) url.openConnection();
//            sun.net.www.protocol.http.HttpURLConnection httpConn = (sun.net.www.protocol.http.HttpURLConnection) conn;
//            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
//            HttpURLConnection

//            conn.setRequestProperty("Authorization", "Negotiate" + encodedUserPwd);

//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68");


//            httpConn.setAuthenticationProperty("Authorization", "Negotiate " + encodedUserPwd);

            conn.setRequestMethod("GET");

            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);


            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                latestVersion = line;
//                System.out.println(line);
            }
//            latestVersion = line;
            reader.close();

// Disconnect the connection
            conn.disconnect();

            //write latest version into properties file
//            InputStream inputStream = CheckUpdate.class.getClassLoader().getResourceAsStream("application.properties");
//            Properties properties = new Properties();
//            try {
//                properties.load(inputStream);
//
//            } catch (IOException e) {
//                System.out.println("Application.properties file not found!!");
//                e.printStackTrace();
//            }
//            System.out.println("Version is : " + properties.getProperty("version"));
//
//            if (latestVersion != null || !latestVersion.isEmpty() || !latestVersion.isBlank()){
//                properties.setProperty("versionFromServer",latestVersion);
//                try {
////                    File file = new File("application.properties");
////                    File file = new File(CheckUpdate.class.getClassLoader().getResource("application.properties").toURI());
////                    URL resourceUrl = CheckUpdate.class.getClassLoader().getResource("application.properties");
//
//
////                    String filePath = CheckUpdate.class.getClassLoader().getResource("application.properties").getFile();
//
//
//                    ClassLoader classLoader = CheckUpdate.class.getClassLoader();
//                    URL resourceUrl = classLoader.getResource("application.properties");
//                    File propertiesFile = new File(resourceUrl.getFile());
//
//                    // Create an output stream to write to the file
//                    OutputStream outputStream = new FileOutputStream(propertiesFile);
//                    // Write the properties to the output stream
//                    properties.store(outputStream,null);
//                    // Close the output stream
//                    outputStream.close();
////                    System.out.println("Properties written successfully to file.");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }




//            Scanner scanner = new Scanner(url.openStream());
//            latestVersion = scanner.nextLine();
//            scanner.close();

        } catch (SocketTimeoutException e) {
            System.out.println("Request timed out");
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return latestVersion;
    }

    public String checkUserAccess(String user){
        String isUserAccessed = null;

        try {
            URL url = new URL(USER_ACCESS_FILE);

//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            HttpURLConnection conn = null;
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
            conn = (HttpURLConnection) url.openConnection();
//            sun.net.www.protocol.http.HttpURLConnection httpConn = (sun.net.www.protocol.http.HttpURLConnection) conn;
//            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
//            HttpURLConnection

//            conn.setRequestProperty("Authorization", "Negotiate" + encodedUserPwd);

//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.68");


//            httpConn.setAuthenticationProperty("Authorization", "Negotiate " + encodedUserPwd);

            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("line : "+line);
                isUserAccessed = line;
//                System.out.println(line);
            }
//            latestVersion = line;
            reader.close();

// Disconnect the connection
            conn.disconnect();


//            Scanner scanner = new Scanner(url.openStream());
//            latestVersion = scanner.nextLine();
//            scanner.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return isUserAccessed;
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

//    public void showUpdateAlert() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Update Available");
//        ButtonType downloadButton = new ButtonType("Download");
//        ButtonType noThanks = new ButtonType("No, thanks", ButtonBar.ButtonData.CANCEL_CLOSE);
//        alert.setContentText("A new version of the app is available.\n" +
//                "Do you want to download and install it now?");
//        alert.setHeaderText(null);
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
//        alert.getButtonTypes().clear();
//        alert.getButtonTypes().setAll(downloadButton, noThanks);
//        alert.showAndWait().ifPresent(button -> {
//            if (button == downloadButton) {
//                downloadJarFile();
//            }
//        });
//    }

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

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Files.copy(inputStream, updateFilePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (SocketTimeoutException e) {
            System.out.println("Request timed out");
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

//    public void downloadJarFile() {
//        try {
//            // create updates directory if it doesn't exist
//            Path updateDir = Paths.get("..\\updates");
//            if (!Files.exists(updateDir)) {
//                Files.createDirectories(updateDir);
//            }
//            // download JAR file to updates directory
//            Path updateFilePath = Paths.get(LOCAL_JAR_DOWNLOAD_PATH);
//            URL url = new URL(JAR_FILE_URL);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            connection.setConnectTimeout(timeout);
//            connection.setReadTimeout(timeout);
//
//            connection.connect();
//            InputStream inputStream = connection.getInputStream();
//            Files.copy(inputStream, updateFilePath, StandardCopyOption.REPLACE_EXISTING);
//
//
////            Files.copy(url.openStream(), updateFilePath, StandardCopyOption.REPLACE_EXISTING);
////            // show download complete message and close the application
////            Alert alert = new Alert(Alert.AlertType.INFORMATION);
////            alert.setTitle("Download Complete");
////            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
////            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
////            alert.setHeaderText("The update has been downloaded.");
////            alert.setContentText("Please restart the application to apply the update.");
////            alert.showAndWait();
//
//            //updating new version in config
////            updateNewVersionInConfig("1.1");
//            //closing application
////            System.exit(0);
//
//        } catch (SocketTimeoutException e) {
//            System.out.println("Request timed out");
//            updateErrorAlert();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            updateErrorAlert();
//        }
//    }





    public void updateErrorAlert(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("Failed to download the update.");
            alert.setContentText("Please try again later.");
            alert.getButtonTypes().clear();
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().add(okButton);
            alert.showAndWait().ifPresent(button -> {
                if (button == okButton) {
                    alert.close();
                }
            });
        });
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

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            connection.connect();
            long contentLength = connection.getContentLengthLong();

            InputStream inputStream = connection.getInputStream();

            // Create a custom dialog for the progress
            Dialog<Void> progressDialog = new Dialog<>();
            progressDialog.setTitle("Downloading Update");
            progressDialog.setWidth(300);
            Stage progressDialogStage = (Stage) progressDialog.getDialogPane().getScene().getWindow();
            progressDialogStage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            progressDialog.setHeaderText("Downloading the update...");

            // Create a VBox to hold the progress indicator
            VBox vbox = new VBox();
            ProgressBar progressIndicator = new ProgressBar();
            progressIndicator.setPrefWidth(300);
            vbox.getChildren().add(progressIndicator);
            progressDialog.getDialogPane().setContent(vbox);

            // Create a Task for downloading the file
            Task<Void> downloadTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    FileOutputStream fileOutputStream = new FileOutputStream(updateFilePath.toFile());
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        // Update the progress
                        double progress = (double) totalBytesRead / contentLength;
                        Platform.runLater(() -> progressIndicator.setProgress(progress));
                    }

                    fileOutputStream.close();
                    inputStream.close();

                    return null;
                }
            };

            // Set up completion behavior for the Task
            downloadTask.setOnSucceeded(event -> {
                progressDialogStage.close();

                updateDownloadedAlert();

            });



            downloadTask.setOnFailed(event -> {
                progressDialogStage.close();
                // Show the error alert
                updateErrorAlert();
            });



            // Set the task on the progress dialog
            progressDialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.CANCEL) {
                    downloadTask.cancel();
                }
                return null;
            });
            progressDialog.setOnCloseRequest(event -> downloadTask.cancel());
            progressDialog.setOnShown(event -> new Thread(downloadTask).start());
            progressDialog.showAndWait();



        } catch (SocketTimeoutException e) {
            System.out.println("Request timed out");
            updateErrorAlert();
        } catch (IOException e) {
            e.printStackTrace();
            updateErrorAlert();
        }
        catch (Exception e){
            e.printStackTrace();
            updateErrorAlert();
        }
    }



    public void updateDownloadedAlert(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Complete");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("The update has been downloaded.");
            alert.setContentText("Please restart the application to apply the update.");
            alert.getButtonTypes().clear();
            ButtonType restartButton = new ButtonType("Restart");
            alert.getButtonTypes().add(restartButton);
            alert.showAndWait().ifPresent(button -> {
                if (button == restartButton) {
                    System.exit(0);
                }
            });
        });
//        System.exit(0);


    }


    public boolean isJarDownloaded(){
        File jar = new File(LOCAL_JAR_DOWNLOAD_PATH);
        if(jar.exists()){
            return true;
        }else{
            return false;
        }
    }

    public void authenticateProxy(){

    }

}