//package com.mannash.simcardvalidation;
//
//import com.mannash.simcardvalidation.pojo.TerminalInfo;
//import com.mannash.simcardvalidation.service.TerminalConnectService;
//import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
//import javafx.fxml.FXML;
//import javafx.scene.control.TextField;
//
//import java.util.Iterator;
//import java.util.List;
//
//
//public class demoController {
//
//    @FXML
//    private TextField input_iccid;
//
//    public void onStartButtonPress(){
//        String localIccid = "1234";
////        StartTesting startTesting = new StartTesting();
////        startTesting.onPressStartButton();
//        TerminalConnectService terminalConnectService ;
//        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
//        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
//
//        while (terminalInfo.hasNext()) {
////					System.out.println("Terminal iterator inside the sub-while loop...");
//            TerminalInfo terminal = terminalInfo.next();
//            localIccid = terminal.getTerminalCardIccid();
//            System.out.println(localIccid);
//
//        }
//
//        input_iccid.setText(localIccid);
//
//
//
//    }
//
//    public static void main(String[] args) {
//        demoController controller = new demoController();
//        controller.onStartButtonPress();
//    }
//
//}
