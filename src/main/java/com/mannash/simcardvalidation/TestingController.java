//package com.mannash.simcardvalidation;
//
////import com.mannash.simcardvalidation.zzzmain.StartTesting;
////import com.mannash.simcardvalidation.zzzpojo2.TestCaseStatus;
//import com.mannash.simcardvalidation.card.ProfileTest3G;
//import com.mannash.simcardvalidation.card.StressTest;
//import com.mannash.simcardvalidation.pojo.TerminalInfo;
//import com.mannash.simcardvalidation.service.LoggerService;
//import com.mannash.simcardvalidation.service.LoggerServiceImpl;
//import com.mannash.simcardvalidation.service.TerminalConnectService;
//import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
////import com.mannash.simcardvalidation.threads.TestExecutor;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
//import javafx.stage.Stage;
//
//import javax.smartcardio.Card;
//import javax.smartcardio.CardException;
//import java.io.IOException;
//import java.util.List;
//
//public class TestingController {
//
//    @FXML private ImageView img_test_status_1;
//    @FXML private ImageView img_test_status_2;
//    @FXML private ImageView img_test_status_3;
//    @FXML private ImageView img_test_status_4;
//    @FXML private ImageView img_test_status_5;
//
//    @FXML private ImageView img_test_status;
//    @FXML private ImageView img_test_button;
//
//    @FXML private ImageView img_reset_button;
//    @FXML private TextField input_iccid;
//
//    @FXML private ImageView img_status;
//
//    public int counter = 0;
////    TestCaseStatus testCaseStatus = new TestCaseStatus();
//
//    TerminalConnectService terminalConnectService ;
//    private Card card = null;
//    private LoggerService loggerService;
//    StressTest stressTest;
////    Image greenCheck;
////    Image redCross;
//    private String localIccid;
//    private String woId;
//    private int terminalId;
//    private TerminalInfo terminal;
//    private boolean isCardConnected=false;
//
//
//    Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
//    Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
//
//    public TestingController(ImageView img_test_status_1) {
//        Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
//        Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
//    }
//
//    public TestingController()  {
//
//
//        this.loggerService = new LoggerServiceImpl();
//
//
//    }
//
////    public void changeImage() throws InterruptedException, IOException {
////
////
////
////        if(img_test_button.getImage().getUrl().contains("startTest.gif")) {
////
////            if(this.counter % 2 == 0){
////                Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testInProgress.gif");
////
////                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
////                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
////                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
////                        img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
////                        input_iccid.setText("8991000904448610811F")), new KeyFrame(Duration.seconds(15), event ->
////                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_test_status_2.setImage(newImage)), new KeyFrame(Duration.seconds(37), event ->
////                        img_test_status_3.setImage(newImage)), new KeyFrame(Duration.seconds(67), event ->
////                        img_test_status_4.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
////                        img_test_status_5.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
////                        img_status.setImage(okImage)), new KeyFrame(Duration.seconds(77), event ->
////                        img_test_button.setImage(doneButton))
////                );
////                timeline.play();
////            }
////            else{
////                Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testInProgress.gif");
////                Image newImage = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
////                Image crossImage = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
////                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");
////                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
////                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
////                        img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
////                        input_iccid.setText("8991000904448610860F")), new KeyFrame(Duration.seconds(15), event ->
////                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_test_status_2.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_test_status_3.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_test_status_4.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_test_status_5.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_status.setImage(okImage)), new KeyFrame(Duration.seconds(25), event ->
////                        img_test_button.setImage(doneButton))
////                );
////                timeline.play();
////
////            }
////
////            counter++;
////
////        }else{
////            loadTestingWindow();
////        }
////    }
//
//    public void onStartButtonPress() throws CardException {
//
//        Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");
//
//        //connect to terminal
//        try {
//            this.isCardConnected = initializeTerminal();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        //get card from terminal
//        try {
//            this.card = this.terminal.getCt().connect("T=0");
//        } catch (Exception exception){
//            exception.printStackTrace();
//        }
//
//        //get iccid from card
//        this.localIccid = terminal.getTerminalCardIccid();
//
//        //set iccid in UI
//        input_iccid.setText(this.localIccid);
//
//        //set ATR status in UI
//        img_test_status_1.setImage(greenCheck);
//
//        //run file system validation testing
//       boolean b = fileSystemVerification();
//
//        //set fileSystem status in UI
//        if (b){
//            img_test_status_2.setImage(greenCheck);
//        }else {
//            img_test_status_2.setImage(redCross);
//        }
//
//        //run Profile testing
//        boolean isProfileSuccessful = profileValidation();
//
//        //set profile testing status in UI
//        if (isProfileSuccessful){
//            img_test_status_3.setImage(greenCheck);
//        }else {
//            img_test_status_3.setImage(redCross);
//        }
//
//        //run read-write test
//        boolean isStressSuccessful = readWriteTest();
//
//        //set stress testing status in UI
//        if (isStressSuccessful) {
//            img_test_status_4.setImage(greenCheck);
//        }else {
//            img_test_status_4.setImage(redCross);
//        }
//
//        //result compilation
//        boolean isResultCompiled = resultCompilation();
//        if (isResultCompiled){
//            img_test_status_5.setImage(greenCheck);
//        }else {
//            img_test_status_5.setImage(redCross);
//        }
//
//    }
//
//    private boolean initializeTerminal() throws CardException {
//
//        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
//        if(terminalInfos.size()==0) {
//            return false;
//        }else {
//            this.terminal = terminalInfos.get(0);
//
////            this.woId = "1184";
//            this.terminalId = terminal.getTerminalNumber();
//            return true;
//        }
//
////				List<ResponseFieldTestingCardPojo> fieldTestingCardPojos = serverResponse
////						.getResponseFieldTestingCardPojos();
//
////        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
//
////        while (terminalInfo.hasNext()) {
////            System.out.println("Terminal iterator inside the sub-while loop...");
////            this.terminal = terminalInfo.next();
////            terminal.setUserName("");
////            String localIccid = terminal.getTerminalCardIccid();
////            int terminalId = terminal.getTerminalNumber();
////            terminal.setWoId("1184");
////            System.out.println("localIccid : "+localIccid);
////            card = terminal.getCt().connect("T=0");
////
////            loggerService = new LoggerServiceImpl();
////
////            testing= startTesting(terminal,card,loggerService);
////
////
////
////        }
//
//    }
//
//    public void loadTestingWindow() throws IOException {
//        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
//        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
//        Scene scene = new Scene(testingPage);
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//    }
//
//    public static void main(String[] args) throws CardException {
//        TestingController test = new TestingController();
//        test.onStartButtonPress();
//    }
//
//
//    public boolean fileSystemVerification() {
//        return true;
//    }
//
//    public boolean profileValidation() {
//        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", this.terminal, this.card, this.loggerService);
//        boolean runProfile = profileTest3G.runProfileTesting();
//        return runProfile;
//    }
//
//    public boolean readWriteTest() {
//        stressTest = new StressTest(this.terminal,this.card,this.loggerService);
//        stressTest.runStressTesting();
//        boolean stressTestingSuccessful = stressTest.startStressTest();
//
//        return stressTestingSuccessful;
//    }
//
//    public boolean resultCompilation() {
//        return true;
//    }
//
//
//}
