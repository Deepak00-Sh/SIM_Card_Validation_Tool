package com.mannash.simcardvalidation;

//import com.mannash.simcardvalidation.zzzmain.StartTesting;
//import com.mannash.simcardvalidation.zzzpojo2.TestCaseStatus;
import com.mannash.simcardvalidation.card.ProfileTest3G;
import com.mannash.simcardvalidation.card.StressTest;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.LoggerService;
import com.mannash.simcardvalidation.service.LoggerServiceImpl;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
import com.mannash.simcardvalidation.threads.FieldTestingMasterThread;
//import com.mannash.simcardvalidation.threads.TestExecutor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TestingController {

    @FXML private ImageView img_test_status_1;
    @FXML private ImageView img_test_status_2;
    @FXML private ImageView img_test_status_3;
    @FXML private ImageView img_test_status_4;
    @FXML private ImageView img_test_status_5;

    @FXML private ImageView img_test_status;
    @FXML private ImageView img_test_button;

    @FXML private ImageView img_reset_button;
    @FXML private TextField input_iccid;

    @FXML private ImageView img_status;

    public int counter = 0;
//    TestCaseStatus testCaseStatus = new TestCaseStatus();

    TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl();
    private Card card = null;
    LoggerService loggerService;
    StressTest stressTest;
    Image greenCheck;
    Image redCross;

    public TestingController(ImageView img_test_status_1) {
        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
    }

    public TestingController() {

    }

//    public void changeImage() throws InterruptedException, IOException {
//
//
//
//        if(img_test_button.getImage().getUrl().contains("startTest.gif")) {
//
//            if(this.counter % 2 == 0){
//                Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testInProgress.gif");
//
//                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
//                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
//                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
//                        img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
//                        input_iccid.setText("8991000904448610811F")), new KeyFrame(Duration.seconds(15), event ->
//                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_test_status_2.setImage(newImage)), new KeyFrame(Duration.seconds(37), event ->
//                        img_test_status_3.setImage(newImage)), new KeyFrame(Duration.seconds(67), event ->
//                        img_test_status_4.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
//                        img_test_status_5.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
//                        img_status.setImage(okImage)), new KeyFrame(Duration.seconds(77), event ->
//                        img_test_button.setImage(doneButton))
//                );
//                timeline.play();
//            }
//            else{
//                Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testInProgress.gif");
//                Image newImage = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
//                Image crossImage = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
//                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");
//                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
//                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
//                        img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
//                        input_iccid.setText("8991000904448610860F")), new KeyFrame(Duration.seconds(15), event ->
//                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_test_status_2.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_test_status_3.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_test_status_4.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_test_status_5.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_status.setImage(okImage)), new KeyFrame(Duration.seconds(25), event ->
//                        img_test_button.setImage(doneButton))
//                );
//                timeline.play();
//
//            }
//
//            counter++;
//
//        }else{
//            loadTestingWindow();
//        }
//    }

    public void onStartButtonPress() throws CardException {

        initializeTerminal();

    }

    private void initializeTerminal() throws CardException {

        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
//				System.out.println("Terminal infos : "+ terminalInfos);
        if(terminalInfos.size()==0) {
//					System.out.println("Check point 3");
//					System.out.println("Terminal iterator has no data.");
            terminalInfos = terminalConnectService.fetchTerminalInfo();
//					System.out.println("Terminal infos : "+ terminalInfos);
        }

//				List<ResponseFieldTestingCardPojo> fieldTestingCardPojos = serverResponse
//						.getResponseFieldTestingCardPojos();

        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();

        while (terminalInfo.hasNext()) {
            System.out.println("Terminal iterator inside the sub-while loop...");
            TerminalInfo terminal = terminalInfo.next();
            terminal.setUserName("");
            String localIccid = terminal.getTerminalCardIccid();
            int terminalId = terminal.getTerminalNumber();
            terminal.setWoId("1184");
            System.out.println("localIccid : "+localIccid);
            card = terminal.getCt().connect("T=0");
            loggerService = new LoggerServiceImpl();
            startTesting(terminal,card,loggerService);

        }
    }

    private void startTesting(TerminalInfo terminal,Card card, LoggerService loggerService) throws CardException {
        atrVerification(terminal.getTerminalCardIccid(),true);
        System.out.println("1");
        fileSystemVerification(true);
        System.out.println("2");
        profileValidation(terminal,card,loggerService);
        System.out.println("3");
        readWriteTest(terminal,card,loggerService);
        resultCompilation(true);
    }


    public void loadTestingWindow() throws IOException {
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(testingPage);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) throws CardException {
        TestingController test = new TestingController();
        test.onStartButtonPress();
    }


    public void atrVerification(String localIccid, boolean b) {
        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
        if (b){
            input_iccid.setText(localIccid);
            img_test_status_1.setImage(greenCheck);
        }else {
            img_test_status_1.setImage(redCross);
        }
    }

    public void fileSystemVerification(boolean b) {
        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
        if (b){
            img_test_status_2.setImage(greenCheck);
        }else {
            img_test_status_2.setImage(redCross);
        }
    }

    public void profileValidation(TerminalInfo terminal,Card card, LoggerService loggerService) throws CardException {
        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
//        card = terminal.getCt().connect("T=0");
//        loggerService = new LoggerServiceImpl();
        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", terminal, card, loggerService);
        boolean runProfile = profileTest3G.runProfileTesting();
        if (runProfile){
            img_test_status_3.setImage(greenCheck);
        }else {
            img_test_status_3.setImage(redCross);
        }
    }

    public void readWriteTest(TerminalInfo terminal,Card card,LoggerService loggerService) {
        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
        stressTest = new StressTest(terminal,card,loggerService);
        stressTest.runStressTesting();

        boolean stressTestingSuccessful = stressTest.startStressTest();
        if (stressTestingSuccessful) {
            img_test_status_4.setImage(greenCheck);
        }else {
            img_test_status_4.setImage(redCross);
        }

    }

    public void resultCompilation(boolean b) {
        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
        if (b){
            img_test_status_5.setImage(greenCheck);
        }else {
            img_test_status_5.setImage(redCross);
        }
    }


}
