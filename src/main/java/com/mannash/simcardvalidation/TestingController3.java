//package com.mannash.simcardvalidation;
//
//import com.mannash.simcardvalidation.card.ProfileTest3G;
//import com.mannash.simcardvalidation.card.StressTest;
//import com.mannash.simcardvalidation.pojo.TerminalInfo;
//import com.mannash.simcardvalidation.service.LoggerService;
//import com.mannash.simcardvalidation.service.LoggerServiceImpl;
//import com.mannash.simcardvalidation.service.TerminalConnectService;
//import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
//import javafx.application.Platform;
//import javafx.concurrent.Task;
//import javafx.fxml.FXML;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
//import javax.smartcardio.Card;
//import javax.smartcardio.CardException;
//import java.util.List;
//
//
//public class TestingController3 {
//
//    public TestingController3()  {
//        this.loggerService = new LoggerServiceImpl();
//    }
//
//    @FXML private ImageView img_test_status_1;
//    @FXML private ImageView img_test_status_2;
//    @FXML private ImageView img_test_status_3;
//    @FXML private ImageView img_test_status_4;
//    @FXML private ImageView img_test_status_5;
//    @FXML private ImageView img_test_status;
//    @FXML private ImageView img_test_button;
//    @FXML private ImageView img_reset_button;
//    @FXML private TextField input_iccid;
//    @FXML private ImageView img_status;
//
//
//    public Card getCard() {
//        return card;
//    }
//
//    public void setCard(Card card) {
//        this.card = card;
//    }
//
//    public String getLocalIccid() {
//        return localIccid;
//    }
//
//    public void setLocalIccid(String localIccid) {
//        this.localIccid = localIccid;
//    }
//
//    public int getTerminalId() {
//        return terminalId;
//    }
//
//    public void setTerminalId(int terminalId) {
//        this.terminalId = terminalId;
//    }
//
//    public TerminalInfo getTerminal() {
//        return terminal;
//    }
//
//    public void setTerminal(TerminalInfo terminal) {
//        this.terminal = terminal;
//    }
//
//    public boolean isAtr() {
//        return isAtr;
//    }
//
//    public void setAtr(boolean atr) {
//        isAtr = atr;
//    }
//
//    public boolean isFileSystemVerification() {
//        return fileSystemVerification;
//    }
//
//    public void setFileSystemVerification(boolean fileSystemVerification) {
//        this.fileSystemVerification = fileSystemVerification;
//    }
//
//    public boolean isProfileTesting() {
//        return profileTesting;
//    }
//
//    public void setProfileTesting(boolean profileTesting) {
//        this.profileTesting = profileTesting;
//    }
//
//    public boolean isReadWriteTesting() {
//        return readWriteTesting;
//    }
//
//    public void setReadWriteTesting(boolean readWriteTesting) {
//        this.readWriteTesting = readWriteTesting;
//    }
//
//    public boolean isCardConnected() {
//        return cardConnected;
//    }
//
//    public void setCardConnected(boolean cardConnected) {
//        this.cardConnected = cardConnected;
//    }
//
//    public boolean isResultCompilation() {
//        return resultCompilation;
//    }
//
//    public void setResultCompilation(boolean resultCompilation) {
//        this.resultCompilation = resultCompilation;
//    }
//
//    private Card card;
//    private String localIccid;
//    private int terminalId;
//    private TerminalInfo terminal;
//    private boolean isAtr = false;
//
//    private boolean fileSystemVerification = false;
//    private boolean profileTesting = false;
//    private boolean readWriteTesting = false;
//    private boolean cardConnected=false;
//
//    private boolean resultCompilation=false;
//    private LoggerService loggerService;
//
//    TerminalConnectService terminalConnectService ;
//    Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
//    Image redCross = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
//    Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
//
//    public void onStartButtonPress(){
//        Task<Void> task1 = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                //connect to terminal
//                try {
//                    System.out.println("Inside the task 1");
//                    boolean b1 = initializeTerminal();
//                    setCardConnected(b1);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                if (isCardConnected()){
//                    System.out.println("Card is connected ");
//                    System.out.println("Iccid : "+getTerminal().getTerminalCardIccid());
//                    String s1 = getTerminal().getTerminalCardIccid();
//                    setLocalIccid(s1);
//
//                }
//                //get card from terminal
//                try {
//                    Card card1 = getTerminal().getCt().connect("T=0");
//                    setCard(card1);
//                } catch (Exception exception){
//                    exception.printStackTrace();
//                }
//                return null;
//            }
//        };
//
//        Task<Void> task2 = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                //connect to terminal
//                try {
//                    System.out.println("Inside the task 2");
//                    boolean b2 = fileSystemVerification();
//                    setFileSystemVerification(b2);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//
//        Task<Void> task3 = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                //connect to terminal
//                try {
//                    System.out.println("Inside the task 3");
//                    boolean b3 = profileValidation();
//                    setProfileTesting(b3);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//
//        Task<Void> task4 = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                //connect to terminal
//                try {
//                    System.out.println("Inside the task 4");
//                    boolean b4 = readWriteTest();;
//                    setReadWriteTesting(b4);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//
//        Task<Void> task5 = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                //connect to terminal
//                try {
//                    System.out.println("Inside the task 5");
//                    boolean b5 = resultCompilation();
//                    setResultCompilation(b5);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//
//        task1.setOnSucceeded(event1 -> {
//            Platform.runLater(() -> {
//                //set iccid in UI
//                if (isCardConnected()){
//                    input_iccid.setText(getLocalIccid());
//                    img_test_status_1.setImage(greenCheck);
//                }else {
//                    img_test_status_1.setImage(redCross);
//                }
//
//            });
//            Thread thread2 = new Thread(task2);
//            thread2.start();
//        });
//
//        task2.setOnSucceeded(event2 -> {
//            Platform.runLater(() -> {
//                if (isFileSystemVerification()){
//                    img_test_status_2.setImage(greenCheck);
//                }else {
//                    img_test_status_2.setImage(redCross);
//                }
//
//            });
//            Thread thread3 = new Thread(task3);
//            thread3.start();
//        });
//
//        task3.setOnSucceeded(event3 -> {
//            Platform.runLater(() -> {
//                if (isProfileTesting()){
//                    img_test_status_3.setImage(greenCheck);
//                }else {
//                    img_test_status_3.setImage(redCross);
//                }
//
//            });
//            Thread thread4 = new Thread(task4);
//            thread4.start();
//        });
//
//        task4.setOnSucceeded(event4 -> {
//            Platform.runLater(() -> {
//                if (isReadWriteTesting()){
//                    img_test_status_4.setImage(greenCheck);
//                }else {
//                    img_test_status_4.setImage(redCross);
//                }
//
//            });
//            Thread thread5 = new Thread(task5);
//            thread5.start();
//        });
//
//        task5.setOnSucceeded(event5 -> {
//            Platform.runLater(() -> {
//                if (isResultCompilation()){
//                    img_test_status_5.setImage(greenCheck);
//                }else {
//                    img_test_status_5.setImage(redCross);
//                }
//                img_status.setImage(okImage);
//            });
//        });
//
//        Thread thread1 = new Thread(task1);
//        thread1.start();
//
//    }
//
//
//
//
//    private boolean initializeTerminal() throws CardException {
//
//        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
//        if(terminalInfos.size()==0) {
//            return false;
//        }else {
//            TerminalInfo terminal1 = terminalInfos.get(0);
//            setTerminal(terminal1);
//
////            this.woId = "1184";
//            int terminalId1 = terminal.getTerminalNumber();
//            setTerminalId(terminalId1);
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
//    private boolean fileSystemVerification() {
//        return true;
//    }
//    private boolean profileValidation() {
//        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", getTerminal(), getCard(), this.loggerService);
//        boolean runProfile = profileTest3G.runProfileTesting();
//        return runProfile;
//    }
//
//    public boolean readWriteTest() {
//        StressTest stressTest = new StressTest(getTerminal(),getCard(),this.loggerService);
//        stressTest.runStressTesting();
//        boolean stressTestingSuccessful = stressTest.startStressTest();
//
//        return stressTestingSuccessful;
//    }
//
//    public boolean resultCompilation() {
//        return true;
//    }
//}