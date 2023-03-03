package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.card.FileSystemVerification;
import com.mannash.simcardvalidation.card.ProfileTest3G;
import com.mannash.simcardvalidation.card.StressTest;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.pojo.UserInfo;
import com.mannash.simcardvalidation.service.LoggerService;
import com.mannash.simcardvalidation.service.LoggerServiceImpl;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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




    Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
    Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
    Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
    Image cancelButtonImage = new Image("/com/mannash/javafxapplication/fxml/images/button_cancel.png");
//    Image doneImage = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
    Image simOk = new Image("/com/mannash/javafxapplication/fxml/images/button_OK.png");
    Image simFaulty = new Image("/com/mannash/javafxapplication/fxml/images/button_SIM_Faulty.png");
    Image notOkImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");
    Image loadingGif = new Image ("/com/mannash/javafxapplication/fxml/images/loading10.gif");
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
        img_test_button.setImage(cancelButtonImage);
        simCardVbox.getChildren().remove(img_status);
        //Add Logs area
        this.logTextArea = logTextArea;
        logTextArea.setPrefSize(simCardVbox.getPrefWidth(), simCardVbox.getPrefHeight());
        logTextArea.positionCaret(logTextArea.getLength());
        logTextArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        logTextArea.viewOrderProperty();
        logTextArea.getStyleClass().add("text-area-left-aligned");
        this.pane = pane;
        pane = new ScrollPane(logTextArea);
        Node horizontalScrollBar = pane.lookup(".scroll-bar:horizontal");if (horizontalScrollBar != null) {    horizontalScrollBar.setStyle("-fx-pref-width: 1px;");}
        pane.setFitToWidth(true);
        pane.setFitToHeight(true);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        simCardVbox.getChildren().add(pane);

            task1 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    //connect to terminal
                    System.out.println("Inside the task 1");
                    boolean b1 = initializeTerminal();
                    setCardConnected(b1);

                    if (isCardConnected()) {
                        System.out.println("Card is connected ");
                        System.out.println("Iccid : " + getTerminal().getTerminalCardIccid());
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
                    displayLogs(_terminal,_card,"Starting File System Verification");
                    System.out.println("Inside the task 2");
                    boolean b2 = fileSystemVerification();
                    setFileSystemVerification(b2);
                    return b2;
                }
            };

            task3 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    //connect to terminal
                    System.out.println("Inside the task 3");
                    displayLogs(_terminal,_card,"Starting Profile Verification");
                    boolean b3 = profileValidation();
                    setProfileTesting(b3);
                    return b3;
                }
            };

            task4 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    //connect to terminal
                    System.out.println("Inside the task 4");
                    displayLogs(_terminal,_card,"Starting Read/Write Test");
                    boolean b4 = readWriteTest();

                    setReadWriteTesting(b4);
                    return b4;
                }
            };

            task5 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    //connect to terminal
                    System.out.println("Inside the task 5");
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
                        displayLogs(_terminal,_ui,"Print ICCID");
                        input_iccid.setText(getLocalIccid());
                        displayLogs(_terminal,_ui,"Print IMSI");
                        input_imsi.setText(getImsi());
                        img_test_status_1.setImage(greenCheck);
                        img_test_status_2.setImage(loadingGif);
                    } else {
                        displayLogs(_terminal,"SIM Heartbeat failed");
                        img_test_status_1.setImage(redCross);
                        displayLogs(_terminal,"Skipping File System Verification");
                        img_test_status_2.setImage(redCross);
                        displayLogs(_terminal,"Skipping Profile Verification");
                        img_test_status_3.setImage(redCross);
                        displayLogs(_terminal,"Skipping Read/Write Test");
                        img_test_status_4.setImage(redCross);
//                        displayLogs(_terminal,_card,"Skipping Result Compilation");
                        img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                        img_test_button.setImage(simFaulty);
                        radioOptionsVBox.setVisible(true);
                        messageTextArea.setVisible(true);
                        messageTextArea.setText("Card is not healthy, please go for SIM swap");
                    }

                });
                if (result) {
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
                        displayLogs(_terminal,_card,"File Verification done");
                        img_test_status_2.setImage(greenCheck);
                        img_test_status_3.setImage(loadingGif);
                    } else {
                        displayLogs(_terminal,"File Verification failed");
                        img_test_status_2.setImage(redCross);
                        displayLogs(_terminal,"Skipping Profile Verification");
                        img_test_status_3.setImage(redCross);
                        displayLogs(_terminal,"Skipping Read/Write Test");
                        img_test_status_4.setImage(redCross);
//                        displayLogs(_terminal,"Skipping Result Compilation");
                        img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                        img_test_button.setImage(simFaulty);
                        radioOptionsVBox.setVisible(true);
                        messageTextArea.setVisible(true);
                        messageTextArea.setText("Card is not healthy, please go for SIM swap");
                    }

                });

                if (result) {
                    Thread thread3 = new Thread(task3);
                    this.threadList.add(thread3);
                    thread3.start();

                } else {
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
                        displayLogs(_terminal,"Profile Verification done");
                        img_test_status_3.setImage(greenCheck);
                        img_test_status_4.setImage(loadingGif);
                    } else {
                        displayLogs(_terminal,"Profile Verification failed");
                        img_test_status_3.setImage(redCross);
                        displayLogs(_terminal,"Skipping Read/Write Test");
                        img_test_status_4.setImage(redCross);
//                        displayLogs(_terminal,"Skipping Result Compilation");
                        img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                        img_test_button.setImage(simFaulty);
                        radioOptionsVBox.setVisible(true);
                        messageTextArea.setVisible(true);
                        messageTextArea.setText("Card is not healthy, please go for SIM swap");
                    }

                });

                if (result) {
                    Thread thread4 = new Thread(task4);
                    this.threadList.add(thread4);
                    thread4.start();
                } else {
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
                        displayLogs(_terminal,"Read/Write Test failed");
                        img_test_status_4.setImage(redCross);
                        img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                        img_test_button.setImage(simFaulty);
                        radioOptionsVBox.setVisible(true);
                        messageTextArea.setVisible(true);
                        messageTextArea.setText("Card is not healthy, please go for SIM swap");
                    }

                });

                if (result) {
                    Thread thread5 = new Thread(task5);
                    this.threadList.add(thread5);
                    thread5.start();
                } else {
                    task5.cancel();
                    return;
                }

            });

            task5.setOnSucceeded(event5 -> {
                Boolean result = task4.getValue();
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
                        messageTextArea.setText("SIM Card is not faulty, please check other SOP if problem persists");

                    } else {
                        img_test_status_5.setImage(redCross);
//                        okImageView.setImage(notOkImage);
                        img_test_button.setImage(simFaulty);
                        radioOptionsVBox.setVisible(true);
                        messageTextArea.setVisible(true);
                        messageTextArea.setText("Card is faulty, please go for SIM swap");
                    }

                });
            });

            Thread thread1 = new Thread(task1);
        this.threadList.add(thread1);
            thread1.start();

    }



    public void onButtonPress() throws IOException, CardException {
        if(img_test_button.getImage().getUrl().contains("button_Start_Testing")){
            System.out.println("inside the startTest..................");
            displayLogs(_terminal,"Verification started");
            onStartButtonPress();
        } else if (img_test_button.getImage().getUrl().contains("button_cancel.png")) {
            displayLogs(_terminal,"Verification process cancelled");
            System.out.println("inside the cancel ........................");
            cancelAllThreads();
            loadTestingWindow();
        } else if (img_test_button.getImage().getUrl().contains("button_OK.png")) {
            System.out.println("inside the done.............................");
            loadTestingWindow();
        }
        else if (img_test_button.getImage().getUrl().contains("button_SIM_Faulty.png")) {
            System.out.println("inside the done.............................");
            loadTestingWindow();
        }
    }

    public void logOut() throws IOException {
        cancelAllThreads();
        Parent logInPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/login-form.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(logInPage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadTestingWindow() throws IOException{
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(testingPage);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void displayLogs(String from, String to, String log){
        displayLogs("[INFO] ["+from+" -> "+to+"] : "+log);
    }

    public void displayLogs(String log){
        Platform.runLater(() ->{
            this.logTextArea.appendText(log+"\n");
        });
    }

    public void displayLogs(String from, String log){
        displayLogs("[INFO] ["+from+"        ] : "+log);
    }


    public void cancelAllThreads() {
        System.out.println(this.threadList);
        for (Thread thread : this.threadList) {
            thread.stop();
        }
    }

    private boolean initializeTerminal() throws CardException {
        displayLogs(_terminal,_card,"Calling fetchterminal");
        TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(this);
        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
        if(terminalInfos.size()==0) {
//            displayLogs(_terminal,_card,"No card found");
            return false;
        }else {
            TerminalInfo terminal1 = terminalInfos.get(0);
            setTerminal(terminal1);

//            this.woId = "1184";
            int terminalId1 = terminal.getTerminalNumber();
            setTerminalId(terminalId1);
            return true;
        }

//				List<ResponseFieldTestingCardPojo> fieldTestingCardPojos = serverResponse
//						.getResponseFieldTestingCardPojos();

//        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();

//        while (terminalInfo.hasNext()) {
//            System.out.println("Terminal iterator inside the sub-while loop...");
//            this.terminal = terminalInfo.next();
//            terminal.setUserName("");
//            String localIccid = terminal.getTerminalCardIccid();
//            int terminalId = terminal.getTerminalNumber();
//            terminal.setWoId("1184");
//            System.out.println("localIccid : "+localIccid);
//            card = terminal.getCt().connect("T=0");
//
//            loggerService = new LoggerServiceImpl();
//
//            testing= startTesting(terminal,card,loggerService);
//
//
//
//        }

    }

    private boolean fileSystemVerification() {
        displayLogs(_terminal,"File System Verification started");
        FileSystemVerification fileSystemVerification = new FileSystemVerification("0000000000000000", getTerminal(), getCard(), this.loggerService,this);
        return fileSystemVerification.runProfileTesting();
    }
    private boolean profileValidation() {
        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", getTerminal(), getCard(), this.loggerService,this);
        boolean runProfile = profileTest3G.runProfileTesting();
        return runProfile;
    }

    public boolean readWriteTest() {
        StressTest stressTest = new StressTest(getTerminal(),getCard(),this.loggerService,this);
        stressTest.runStressTesting();
        boolean stressTestingSuccessful = stressTest.startStressTest();

        return stressTestingSuccessful;
    }

    public boolean resultCompilation() {
        return true;
    }
}