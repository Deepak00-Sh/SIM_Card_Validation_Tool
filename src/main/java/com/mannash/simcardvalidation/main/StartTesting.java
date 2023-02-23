package com.mannash.simcardvalidation.main;

import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;

import java.util.Iterator;
import java.util.List;

public class StartTesting {


    private String loggedinUsername;

    private TerminalConnectService terminalConnectService;

    public StartTesting() {
        terminalConnectService = new TerminalConnectServiceImpl();
    }

    public void onPressStartButton(){
        setLoggedinUsername("mudit.sharma@mannash.com");
        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
        if(terminalInfos.size()==0) {
            terminalInfos = terminalConnectService.fetchTerminalInfo();
        }
        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
        while (terminalInfo.hasNext()) {
            TerminalInfo terminal = terminalInfo.next();
            String localIccid = terminal.getTerminalCardIccid();
            System.out.println("iccid : "+localIccid);
            int terminalId = terminal.getTerminalNumber();
            startTestingThread(terminal);
        }
    }

    public String getLoggedinUsername() {
        return loggedinUsername;
    }

    public void setLoggedinUsername(String loggedinUsername) {
        this.loggedinUsername = loggedinUsername;
    }

    private void startTestingThread(TerminalInfo terminal) {
        String threadName = terminal.getTerminalNumber() + "_" + terminal.getTerminalCardIccid();
        Thread localTestExecutor = new Thread(new TestExecutor(threadName, terminal), threadName);
        localTestExecutor.start();
    }

    public static void main(String[] args) {
        StartTesting startTesting = new StartTesting();
        startTesting.onPressStartButton();
    }

}
