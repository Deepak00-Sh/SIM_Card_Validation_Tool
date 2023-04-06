package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.card.FileSystemVerification;
import com.mannash.simcardvalidation.card.ProfileTest3G;
import com.mannash.simcardvalidation.card.StressTest;
import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.RequestSimVerificationCardPojos;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.smartcardio.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class TestingController4 extends LoginFormController implements Initializable {

    public TestingController4() {
        this.loggerService = new LoggerServiceImpl();
    }


    @FXML
    private ImageView img_test_status_1;
    @FXML
    private ImageView img_test_status_2;
    @FXML
    private ImageView img_test_status_3;
    @FXML
    private ImageView img_test_status_4;
    @FXML
    private ImageView img_test_status_5;
    @FXML
    private ImageView img_test_status;
    @FXML
    private ImageView img_test_button;
    @FXML
    private ImageView img_reset_button;
    @FXML
    private TextField input_iccid;
    @FXML
    private ImageView img_status;
    @FXML
    private VBox simCardVbox;
    @FXML
    private TextField input_imsi;
    @FXML
    private ImageView okImageView;

    //new impl
    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    private ToggleGroup toggleGroup;

    @FXML
    private VBox radioOptionsVBox;
    @FXML
    private Label messageTextArea;
    @FXML
    private ImageView exportIcon;
    //

    private Task<Boolean> task1;
    private Task<Boolean> task2;
    private Task<Boolean> task3;
    private Task<Boolean> task4;
    private Task<Boolean> task5;


    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getLocalIccid() {
        return localIccid;
    }

    public void setLocalIccid(String localIccid) {
        this.localIccid = localIccid;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public TerminalInfo getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalInfo _terminal) {
        terminal = _terminal;
    }

    public boolean isAtr() {
        return isAtr;
    }

    public void setAtr(boolean atr) {
        isAtr = atr;
    }

    public boolean isFileSystemVerification() {
        return fileSystemVerification;
    }

    public void setFileSystemVerification(boolean _fileSystemVerification) {
        fileSystemVerification = _fileSystemVerification;
    }

    public boolean isProfileTesting() {
        return profileTesting;
    }

    public void setProfileTesting(boolean profileTesting) {
        this.profileTesting = profileTesting;
    }

    public boolean isReadWriteTesting() {
        return readWriteTesting;
    }

    public void setReadWriteTesting(boolean readWriteTesting) {
        this.readWriteTesting = readWriteTesting;
    }

    public boolean isCardConnected() {
        return cardConnected;
    }

    public void setCardConnected(boolean cardConnected) {
        this.cardConnected = cardConnected;
    }

    public boolean isResultCompilation() {
        return resultCompilation;
    }

    public void setResultCompilation(boolean resultCompilation) {
        this.resultCompilation = resultCompilation;
    }

    public boolean isTestingRunning() {
        return testingRunning;
    }

    public void setTestingRunning(boolean testingRunning) {
        this.testingRunning = testingRunning;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    private Card card;
    private String localIccid;
    private int terminalId;
    private TerminalInfo terminal;
    private boolean isAtr = false;
    private boolean fileSystemVerification = false;
    private boolean profileTesting = false;
    private boolean readWriteTesting = false;
    private boolean cardConnected = false;
    private boolean resultCompilation = false;
    private boolean testingRunning = false;
    private LoggerService loggerService;
    private String imsi;
    public String _terminal = "T";
    public String _card = "C";
    public String _device = "D";
    public String _ui = "UI";
    CardChannel cardChannel;
    public static List<ExportTestingResultPojo> cardTestingPojosList = new ArrayList<ExportTestingResultPojo>();

    private static final String CACHE_FILE_PATH = "..\\reports\\cache\\";
    private Object csvLock = new Object();
    private boolean headersPrinted = false;


    Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
    Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
    Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
    Image cancelButtonImage = new Image("/com/mannash/javafxapplication/fxml/images/button_cancel.png");
    //    Image doneImage = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
    Image simOk = new Image("/com/mannash/javafxapplication/fxml/images/button_OK.gif");
    Image simFaulty = new Image("/com/mannash/javafxapplication/fxml/images/button_faulty.gif");
    Image notOkImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");
    Image loadingGif = new Image("/com/mannash/javafxapplication/fxml/images/loading10.gif");
    List<Thread> threadList = new ArrayList<>();
    TextArea logTextArea = new TextArea();
    ScrollPane pane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.toggleGroup = new ToggleGroup();

//        radioButton1.setToggleGroup(toggleGroup);
//        radioButton2.setToggleGroup(toggleGroup);
//        radioButton3.setToggleGroup(toggleGroup);
//        toggleGroup.selectToggle(radioButton1);

    }

    public void onStartButtonPress() {
        ExportTestingResultPojo testingResultPojo = new ExportTestingResultPojo();
        cardTestingPojosList.clear();
        img_test_button.setImage(cancelButtonImage);
        simCardVbox.getChildren().remove(img_status);
        //Add Logs area
        this.logTextArea = logTextArea;
        logTextArea.setPrefSize(simCardVbox.getPrefWidth(), simCardVbox.getPrefHeight());
        logTextArea.positionCaret(logTextArea.getLength());
        logTextArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        logTextArea.setEditable(false);
        logTextArea.viewOrderProperty();
        logTextArea.getStyleClass().add("text-area-left-aligned");
        this.pane = pane;
        pane = new ScrollPane(logTextArea);
        Node horizontalScrollBar = pane.lookup(".scroll-bar:horizontal");
        if (horizontalScrollBar != null) {
            horizontalScrollBar.setStyle("-fx-pref-width: 1px;");
        }
        pane.setFitToWidth(true);
        pane.setFitToHeight(true);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        simCardVbox.getChildren().add(pane);

        task1 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                //connect to terminal
                // System.out.println("Inside the task 1");
                boolean b1 = initializeTerminal();
                setCardConnected(b1);

                if (isCardConnected()) {
                    // System.out.println("Card is connected ");
                    // System.out.println("Iccid : " + getTerminal().getTerminalCardIccid());
                    setLocalIccid(getTerminal().getTerminalCardIccid());
                    setImsi(getTerminal().getImsi());

                    //get card from terminal
                    try {
                        Card card1 = getTerminal().getCt().connect("T=0");
                        setCard(card1);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return true;
                } else {
                    return false;
                }

            }
        };

        task2 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                //connect to terminal
                displayLogs(_terminal, _card, "Starting File System Verification");
                // System.out.println("Inside the task 2");
                boolean b2 = fileSystemVerification();
                setFileSystemVerification(b2);
                return b2;
            }
        };

        task3 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                //connect to terminal
                // System.out.println("Inside the task 3");
                displayLogs(_terminal, _card, "Starting Profile Verification");
                boolean b3 = profileValidation();
                // System.out.println("profile test status : "+b3);
                setProfileTesting(b3);
                return b3;
            }
        };

        task4 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                //connect to terminal
                // System.out.println("Inside the task 4");
                displayLogs(_terminal, _card, "Starting Read/Write Test");
                boolean b4 = readWriteTest();

                setReadWriteTesting(b4);
                return b4;
            }
        };

        task5 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                //connect to terminal
                // System.out.println("Inside the task 5");
                boolean b5 = resultCompilation();
                setResultCompilation(b5);
                return b5;
            }
        };

        task1.setOnSucceeded(event1 -> {
            Boolean result = task1.getValue();
            Platform.runLater(() -> {
                //set iccid in UI
                if (result) {
                    img_test_status_1.setImage(loadingGif);
                    displayLogs(_terminal, _ui, "Print ICCID");
                    input_iccid.setText(getLocalIccid());
                    displayLogs(_terminal, _ui, "Print IMSI");
                    input_imsi.setText(getImsi());
                    img_test_status_1.setImage(greenCheck);
                    img_test_status_2.setImage(loadingGif);
                } else {
                    displayLogs(_terminal, "SIM Heartbeat failed");
                    img_test_status_1.setImage(redCross);
                    displayLogs(_terminal, "Skipping File System Verification");
                    img_test_status_2.setImage(redCross);
                    displayLogs(_terminal, "Skipping Profile Verification");
                    img_test_status_3.setImage(redCross);
                    displayLogs(_terminal, "Skipping Read/Write Test");
                    img_test_status_4.setImage(redCross);
//                        displayLogs(_terminal,_card,"Skipping Result Compilation");
                    img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                    img_test_button.setImage(simFaulty);
                    radioOptionsVBox.setVisible(true);
                    messageTextArea.setVisible(true);
                    messageTextArea.setTextFill(Color.RED);
                    messageTextArea.setText("Card is faulty, please go for SIM swap");
                }

            });
            if (result) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
                testingResultPojo.setUserName(LoginFormController.loggedInUserName);
                testingResultPojo.setDateOfTesting(LocalDate.now().format(dateFormatter));
                testingResultPojo.setTimeOfTesting(LocalTime.now().format(timeFormatter));
                testingResultPojo.setTerminalNumber(this.terminal.getTerminalNumber() + 1);
                testingResultPojo.setTerminalICCID(this.localIccid);
                testingResultPojo.setTerminalIMSI(this.imsi);
                testingResultPojo.setSimHeartbeat("OK");


                Thread thread2 = new Thread(task2);
                this.threadList.add(thread2);
                thread2.start();
            } else {
                task2.cancel();
                task3.cancel();
                task4.cancel();
                task5.cancel();
                return;
            }

        });

        task2.setOnSucceeded(event2 -> {
            Boolean result = task2.getValue();
            Platform.runLater(() -> {
                if (result) {
                    displayLogs(_terminal, _card, "File Verification done");
                    img_test_status_2.setImage(greenCheck);
                    img_test_status_3.setImage(loadingGif);
                } else {
                    displayLogs(_terminal, "File Verification failed");
                    img_test_status_2.setImage(redCross);
                    displayLogs(_terminal, "Skipping Profile Verification");
                    img_test_status_3.setImage(redCross);
                    displayLogs(_terminal, "Skipping Read/Write Test");
                    img_test_status_4.setImage(redCross);
//                        displayLogs(_terminal,"Skipping Result Compilation");
                    img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                    img_test_button.setImage(simFaulty);
                    radioOptionsVBox.setVisible(true);
                    messageTextArea.setVisible(true);
                    messageTextArea.setTextFill(Color.RED);
                    messageTextArea.setText("Card is faulty, please go for SIM swap");
                }

            });

            if (result) {
                testingResultPojo.setFileSystemVerification("OK");
                Thread thread3 = new Thread(task3);
                this.threadList.add(thread3);
                thread3.start();

            } else {
                testingResultPojo.setFileSystemVerification("NOT OK");
                testingResultPojo.setProfileTesting("NOT OK");
                testingResultPojo.setReadWrite("NOT OK");
                testingResultPojo.setCardStatus("FAULTY");
                testingResultPojo.setTestCompilation("OK");
                testingResultPojo.setUserName(LoginFormController.loggedInUserName);
                cardTestingPojosList.add(testingResultPojo);
                sendResultToServer();
                exportIcon.setVisible(true);

                task3.cancel();
                task4.cancel();
                task5.cancel();
                return;
            }

        });

        task3.setOnSucceeded(event3 -> {
            Boolean result = task3.getValue();
            Platform.runLater(() -> {
                if (result) {
                    displayLogs(_terminal, "Profile Verification done");
                    img_test_status_3.setImage(greenCheck);
                    img_test_status_4.setImage(loadingGif);
                } else {
                    displayLogs(_terminal, "Profile Verification failed");
                    img_test_status_3.setImage(redCross);
                    displayLogs(_terminal, "Skipping Read/Write Test");
                    img_test_status_4.setImage(redCross);
//                        displayLogs(_terminal,"Skipping Result Compilation");
                    img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                    img_test_button.setImage(simFaulty);
                    radioOptionsVBox.setVisible(true);
                    messageTextArea.setVisible(true);
                    messageTextArea.setTextFill(Color.RED);
                    messageTextArea.setText("Card is faulty, please go for SIM swap");
                }

            });

            if (result) {
                testingResultPojo.setProfileTesting("OK");
                Thread thread4 = new Thread(task4);
                this.threadList.add(thread4);
                thread4.start();
            } else {
                testingResultPojo.setProfileTesting("NOT OK");
                testingResultPojo.setReadWrite("NOT OK");
                testingResultPojo.setCardStatus("FAULTY");
                testingResultPojo.setTestCompilation("OK");
                testingResultPojo.setUserName(LoginFormController.loggedInUserName);
                cardTestingPojosList.add(testingResultPojo);
                sendResultToServer();
                exportIcon.setVisible(true);

                task4.cancel();
                task5.cancel();
                return;
            }

        });

        task4.setOnSucceeded(event4 -> {
            Boolean result = task4.getValue();
            Platform.runLater(() -> {
                if (result) {
                    img_test_status_4.setImage(greenCheck);
                    img_test_status_5.setImage(loadingGif);
                } else {
                    displayLogs(_terminal, "Read/Write Test failed");
                    img_test_status_4.setImage(redCross);
                    img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                    img_test_button.setImage(simFaulty);
                    radioOptionsVBox.setVisible(true);
                    messageTextArea.setVisible(true);
                    messageTextArea.setTextFill(Color.RED);
                    messageTextArea.setText("Card is faulty, please go for SIM swap");
                }

            });

            if (result) {
                testingResultPojo.setReadWrite("OK");
                Thread thread5 = new Thread(task5);
                this.threadList.add(thread5);
                thread5.start();
            } else {
                testingResultPojo.setReadWrite("NOT OK");
                testingResultPojo.setCardStatus("FAULTY");
                testingResultPojo.setTestCompilation("OK");
                testingResultPojo.setUserName(LoginFormController.loggedInUserName);
                cardTestingPojosList.add(testingResultPojo);
                sendResultToServer();
                exportIcon.setVisible(true);

                task5.cancel();
                return;
            }

        });

        task5.setOnSucceeded(event5 -> {
            Boolean result = task5.getValue();
            Platform.runLater(() -> {
                if (result) {
                    img_test_status_5.setImage(greenCheck);
                    img_test_button.setImage(simOk);
//                        simCardVbox.getChildren().remove(this.pane);
//                        simCardVbox.getChildren().add(img_status);
//                        img_status.setImage(okImage);
//                        okImageView.setImage(okImage);
                    radioOptionsVBox.setVisible(true);
                    messageTextArea.setVisible(true);
                    messageTextArea.setTextFill(Color.GREEN);
                    messageTextArea.setText("Card is OK, please check other SOP if problem persists");

                } else {
                    img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                    img_test_button.setImage(simFaulty);
                    radioOptionsVBox.setVisible(true);
                    messageTextArea.setVisible(true);
                    messageTextArea.setTextFill(Color.RED);
                    messageTextArea.setText("Card is faulty, please go for SIM swap");
                }

            });
            if (result) {
                testingResultPojo.setCardStatus("OK");
                testingResultPojo.setTestCompilation("OK");
                testingResultPojo.setUserName(LoginFormController.loggedInUserName);
                cardTestingPojosList.add(testingResultPojo);
                sendResultToServer();
                exportIcon.setVisible(true);

            } else {

            }
        });

        Thread thread1 = new Thread(task1);
        this.threadList.add(thread1);
        thread1.start();

    }


    public void onButtonPress() throws IOException, CardException {
        if (img_test_button.getImage().getUrl().contains("button_Start_Testing")) {
            // System.out.println("inside the startTest..................");
            displayLogs(_terminal, "Verification started");
            onStartButtonPress();
        } else if (img_test_button.getImage().getUrl().contains("button_cancel.png")) {
            displayLogs(_terminal, "Verification process cancelled");
            // System.out.println("inside the cancel ........................");
            cancelAllThreads();
//            clearTerminal();
            loadTestingWindow();
        } else if (img_test_button.getImage().getUrl().contains("button_OK")) {
            // System.out.println("inside the done.............................");
//            clearTerminal();
            loadTestingWindow();
        } else if (img_test_button.getImage().getUrl().contains("button_faulty")) {
            // System.out.println("inside the done.............................");
//            clearTerminal();
            loadTestingWindow();
        }
    }

    public void checkUpdate() {
        CheckUpdate checkUpdate = new CheckUpdate();
        String latestVersion = null;
        String currentVersion = "1.0";
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle("Checking for updates ...");

        try {
            latestVersion = checkUpdate.getLatestVersion();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (currentVersion.equals(latestVersion)) {
            Platform.runLater(() -> {
                Stage stage = (Stage) alert1.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                alert1.setContentText("You are running the latest version.");
                alert1.setHeaderText("No updates available");
                alert1.showAndWait();
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Checking for updates ...");
                ButtonType downloadUpdate = new ButtonType("Download");
                ButtonType noThanks = new ButtonType("No, thanks");
                alert.setContentText("Update Available.");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                alert.getButtonTypes().clear();
                alert.getButtonTypes().setAll(downloadUpdate, noThanks);
                alert.showAndWait().ifPresent(button -> {
                    if (button == downloadUpdate) {
                        checkUpdate.downloadJarFile();
                    }
                });
            });
        }
    }

    @FXML
    public void onExportButtonPress() {
        String csvFilePath = "..\\reports\\";
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String dateTimeString = currentDateTime.format(formatter);
        String csvFileName = "TestingResult_" + dateTimeString + ".csv";
        String fileName = csvFilePath + "TestingResult_" + dateTimeString + ".csv";
        File csvFile = new File(fileName);


        File path = new File(csvFilePath);
        if (!path.exists()) {
            path.mkdir();
        }
        if (csvFile.exists()) {
            this.headersPrinted = true;
        }
        if (!csvFile.exists()) {
            try {
                csvFile.createNewFile();
                this.headersPrinted = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Collections.sort(cardTestingPojosList, new Comparator<ExportTestingResultPojo>() {
//            @Override
//            public int compare(ExportTestingResultPojo obj1, ExportTestingResultPojo obj2) {
//                return obj1.getTerminalNumber() - obj2.getTerminalNumber();
//            }
//        });


        try (FileWriter writer = new FileWriter(csvFile, true); CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            if (!this.headersPrinted) {
                csvPrinter.printRecord("Username", "Slot", "Date", "Time", "ICCID", "IMSI", "SIM Heartbeat", "File System Verification", "Profile Testing", "Read/Write", "Card Status");
                this.headersPrinted = true;
            }
            synchronized (csvLock) {

//                for (ExportTestingResultPojo value : cardTestingPojosList) {
//                    csvPrinter.printRecord(SimVerifyMasterThread2.loggedInUserName, widgetIdSeq[value.getTerminalNumber() - 1], value.getDateOfTesting(), value.getTimeOfTesting(), value.getTerminalICCID()+"\t", value.getTerminalIMSI()+"\t", value.getSimHeartbeat(), value.getFileSystemVerification(), value.getProfileTesting(), value.getReadWrite(), value.getCardStatus());
//                }

//                for (ExportTestingResultPojo value : cardTestingPojosList) {
//                    csvPrinter.printRecord(SimVerifyMasterThread2.loggedInUserName, widgetIdSeq[value.getTerminalNumber() - 1], value.getDateOfTesting(), value.getTimeOfTesting(), value.getTerminalICCID()+"\t", value.getTerminalIMSI()+"\t", value.getSimHeartbeat(), value.getFileSystemVerification(), value.getProfileTesting(), value.getReadWrite(), value.getCardStatus());
//                }


                ExportTestingResultPojo resultPojo = cardTestingPojosList.get(0);
                csvPrinter.printRecord(LoginFormController.loggedInUserName, resultPojo.getTerminalNumber(), resultPojo.getDateOfTesting(), resultPojo.getTimeOfTesting(), resultPojo.getTerminalICCID() + "\t", resultPojo.getTerminalIMSI() + "\t", resultPojo.getSimHeartbeat(), resultPojo.getFileSystemVerification(), resultPojo.getProfileTesting(), resultPojo.getReadWrite(), resultPojo.getCardStatus());
                csvPrinter.flush();
            }
            exportButtonOperations();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void exportButtonOperations() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Export Successful");
            ButtonType okButton = new ButtonType("OK");
            alert.setHeaderText(null);
            alert.setContentText("The testing result exported successfully");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            Image icon = new Image("/com/mannash/javafxapplication/fxml/images/check-1.png");
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(48); // Set the size of the image
            imageView.setFitHeight(48);
            alert.getDialogPane().setGraphic(imageView);
            alert.getButtonTypes().clear();
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        });
        exportIcon.setVisible(false);
    }

    private void sendResultToServer() {
        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
        RequestSimVerificationCardPojos cardPojos = new RequestSimVerificationCardPojos();
        System.out.println("Size of pojo list : "+cardTestingPojosList.size());
//        if (!cardTestingPojosList.get(0).getTerminalICCID().equals(null)) {
            cardPojos.setRequestSimVerificationCardPojos(cardTestingPojosList);
            int statusCode = 0;
            try {
                statusCode = service.sendReportsToServer(cardPojos);
            } catch (Exception e) {
                System.out.println("Reports are failed to send to server");
            }

            if (statusCode != 200) {
                serializeCacheToDisk();
            }
//        }
    }

    private void serializeCacheToDisk() {
        System.out.println("creating cache....");
        File cacheDir = new File(CACHE_FILE_PATH);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String dateTimeString = currentDateTime.format(formatter);

        try (FileOutputStream fileOutputStream = new FileOutputStream(CACHE_FILE_PATH + dateTimeString + ".ser");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(cardTestingPojosList);
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error serializing cache to disk.");
            e.printStackTrace();
        }
    }

    public void onLogOutButtonPress() {
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Confirm logout");
        Stage stage = (Stage) logoutAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
        ButtonType logoutButton = new ButtonType("Logout");
        ButtonType cancel = new ButtonType("Cancel");
        logoutAlert.setContentText("Are you sure that you want to logout ?");
        logoutAlert.setHeaderText(null);
        logoutAlert.getButtonTypes().clear();
        logoutAlert.getButtonTypes().setAll(logoutButton, cancel);
        logoutAlert.showAndWait().ifPresent(button -> {
            if (button == logoutButton) {
                try {
                    logOut();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void logOut() throws IOException {
        cancelAllThreads();
        Parent logInPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/login-form.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(logInPage);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.show();
    }


    private void loadTestingWindow() throws IOException {
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(testingPage);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public void deadCardAlert() {

        ExportTestingResultPojo testingResultPojo = new ExportTestingResultPojo();
        testingResultPojo.setProfileTesting("NOT OK");
        testingResultPojo.setSimHeartbeat("NOT OK");
        testingResultPojo.setFileSystemVerification("NOT OK");
        testingResultPojo.setReadWrite("NOT OK");
        testingResultPojo.setTestCompilation("OK");
        testingResultPojo.setCardStatus("FAULTY");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        testingResultPojo.setDateOfTesting(LocalDate.now().format(dateFormatter));
        testingResultPojo.setTimeOfTesting(LocalTime.now().format(timeFormatter));
        testingResultPojo.setTerminalNumber(1);
        testingResultPojo.setUserName(LoginFormController.loggedInUserName);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Card is ABSENT/FAULTY. Please enter the ICCID");


            // Add a warning icon to the dialog pane
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
// alert.getDialogPane().setGraphic(new ImageView("/com/mannash/javafxapplication/fxml/images/check-1.png"));


            // Create a label for the textField
            Label label = new Label("ICCID:");
            label.setPadding(new Insets(5, 10, 0, 0));
            label.setStyle("-fx-font-weight: bold;");


            // Create a textField
            TextField textField = new TextField();
// textField.setStyle("-fx-background-color: inherit;");
            textField.setPrefWidth(174);
            // Create an HBox to hold the label and textField
            HBox hbox = new HBox(label, textField);
            hbox.setSpacing(15);


            Insets insets = new Insets(10, 0, 0, 0);
            GridPane.setMargin(hbox, insets);


            // Create a grid pane to layout the content
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 70, 10, 10));
            grid.add(new Label(alert.getContentText()), 0, 0);
            grid.add(hbox, 0, 1);


            // Set the content of the dialog pane to the grid pane
            alert.getDialogPane().setContent(grid);


            ButtonType sendButton = new ButtonType("Send");
            ButtonType cancelButton = new ButtonType("Cancel");

//            ButtonType sendButton = new ButtonType("Send" , ButtonBar.ButtonData.OK_DONE);
//            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);


            alert.getButtonTypes().clear();
            alert.getButtonTypes().setAll(sendButton, cancelButton);

//            Button sendButtonLookup = (Button) alert.getDialogPane().lookupButton(sendButton);
//            sendButtonLookup.setOnAction(e -> {
//                System.out.println("calling function");
//                deadCardSendButton(testingResultPojo,textField.getText());
//            });

            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/com/mannash/javafxapplication/fxml/application.css").toExternalForm());
            // Set focus to the textField when the alert is shown
            alert.setOnShown(event -> textField.requestFocus());
            alert.setOnCloseRequest(event -> {// Perform some action before the dialog is closed
                alert.close();
            });
//            sendButton.setOnAction(event -> { deadCardAlert();});


//            alert.showAndWait();



            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == sendButton) {
                System.out.println("calling function");
                try {
                    deadCardSendButton(testingResultPojo,textField.getText());
                }catch (Exception e){
                    System.out.println("Exception in deadCardSendButton");
                    e.printStackTrace();
                }

            }

//            alert.showAndWait().ifPresent(button -> {
//                if (button == sendButton) {
//                    deadCardSendButton(testingResultPojo,textField.getText());
//                }
//            });
        });
    }

    private void deadCardSendButton(ExportTestingResultPojo resultPojo, String iccid){
        RequestSimVerificationCardPojos requestSimVerificationCardPojos = new RequestSimVerificationCardPojos();
        input_iccid.setText(iccid);
        resultPojo.setTerminalICCID(iccid);
        resultPojo.setTerminalIMSI(null);
        System.out.println("Pojo from dead alert"+resultPojo.toString());
        cardTestingPojosList.add(resultPojo);
        sendResultToServer();
        exportIcon.setVisible(true);
    }

    public void displayLogs(String from, String to, String log) {
        displayLogs("[INFO] [" + from + " -> " + to + "] : " + log);
    }

    public void displayLogs(String log) {
        Platform.runLater(() -> {
            this.logTextArea.appendText(log + "\n");
        });
    }

    public void displayLogs(String from, String log) {
        displayLogs("[INFO] [" + from + "        ] : " + log);
    }


    public void cancelAllThreads() {
        // System.out.println(this.threadList);
        for (Thread thread : this.threadList) {
            thread.stop();
        }
    }

    private boolean initializeTerminal() throws CardException {
        displayLogs(_terminal, _card, "Calling fetchterminal");
        TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(this);
        try {
            List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
            if (terminalInfos.size() == 0) {
//            displayLogs(_terminal,_card,"No card found");
                return false;
            } else {
                TerminalInfo terminal1 = terminalInfos.get(0);
                setTerminal(terminal1);

//            this.woId = "1184";
                int terminalId1 = terminal.getTerminalNumber();
                setTerminalId(terminalId1);
                return true;
            }
        } catch (Exception e) {
            // System.out.println("Testing controller exception");
        }


//				List<ResponseFieldTestingCardPojo> fieldTestingCardPojos = serverResponse
//						.getResponseFieldTestingCardPojos();

//        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();

//        while (terminalInfo.hasNext()) {
//            // System.out.println("Terminal iterator inside the sub-while loop...");
//            this.terminal = terminalInfo.next();
//            terminal.setUserName("");
//            String localIccid = terminal.getTerminalCardIccid();
//            int terminalId = terminal.getTerminalNumber();
//            terminal.setWoId("1184");
//            // System.out.println("localIccid : "+localIccid);
//            card = terminal.getCt().connect("T=0");
//
//            loggerService = new LoggerServiceImpl();
//
//            testing= startTesting(terminal,card,loggerService);
//
//
//
//        }

        return false;
    }

    private boolean fileSystemVerification() {
        displayLogs(_terminal, "File System Verification started");
        FileSystemVerification fileSystemVerification = new FileSystemVerification("0000000000000000", getTerminal(), getCard(), this.loggerService, this);
        return fileSystemVerification.runProfileTesting();
    }

    private boolean profileValidation() {
        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", getTerminal(), getCard(), this.loggerService, this);
//        boolean runProfile = profileTest3G.runProfileTesting();
        return profileTest3G.runProfileTesting();
    }

    public boolean readWriteTest() {
        StressTest stressTest = new StressTest(getTerminal(), getCard(), this.loggerService, this);
        stressTest.runStressTesting();
        boolean stressTestingSuccessful = stressTest.startStressTest();

        return stressTestingSuccessful;
    }

    public boolean resultCompilation() {
        return true;
    }

    private void clearTerminal() {
        try {
            Class pcscterminal = Class.forName("sun.security.smartcardio.PCSCTerminals");
            Field contextId = pcscterminal.getDeclaredField("contextId");
            contextId.setAccessible(true);

            if (contextId.getLong(pcscterminal) != 0L) {
                // First get a new context value
                Class pcsc = Class.forName("sun.security.smartcardio.PCSC");
                Method SCardEstablishContext = pcsc.getDeclaredMethod(
                        "SCardEstablishContext",
                        new Class[]{Integer.TYPE}
                );
                SCardEstablishContext.setAccessible(true);

                Field SCARD_SCOPE_USER = pcsc.getDeclaredField("SCARD_SCOPE_USER");
                SCARD_SCOPE_USER.setAccessible(true);

                long newId = ((Long) SCardEstablishContext.invoke(pcsc,
                        new Object[]{SCARD_SCOPE_USER.getInt(pcsc)}
                ));
                contextId.setLong(pcscterminal, newId);


                // Then clear the terminals in cache
                TerminalFactory factory = TerminalFactory.getDefault();
                CardTerminals terminals = factory.terminals();
                Field fieldTerminals = pcscterminal.getDeclaredField("terminals");
                fieldTerminals.setAccessible(true);
                Class classMap = Class.forName("java.util.Map");
                Method clearMap = classMap.getDeclaredMethod("clear");

                clearMap.invoke(fieldTerminals.get(terminals));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}