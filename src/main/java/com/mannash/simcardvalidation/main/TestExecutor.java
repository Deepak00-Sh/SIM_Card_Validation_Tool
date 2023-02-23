package com.mannash.simcardvalidation.main;

import com.mannash.simcardvalidation.TestingController;
import com.mannash.simcardvalidation.cardTest.ProfileTest3G;
import com.mannash.simcardvalidation.cardTest.ReadWriteTest;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.pojo.TestCaseStatus;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestExecutor implements Runnable {

    private String localIccid;
    private TerminalInfo terminal;
    private Card card = null;
    private AtomicBoolean running = new AtomicBoolean(false);
    boolean readWriteTestSuccessful = true;
    boolean profileTestingSuccessful = true;
    boolean atrVerification = true;
    ReadWriteTest readWriteTest;
//    TestingController testingController = new TestingController();


    public TestExecutor(String threadName, TerminalInfo terminal) {
        this.localIccid = terminal.getTerminalCardIccid();
        this.terminal = terminal;

        try {
            this.card = this.terminal.getCt().connect("T=0");
        } catch (CardException e1){
            e1.printStackTrace();
            running.set(false);
        }

    }

    public void interrupt() {
        try {
            // this.logger.debug("Got interrupt");
            this.card.disconnect(true);
        } catch (CardException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        running.set(false);
    }

    @Override
    public void run() {

        running.set(true);
        while (running.get()){
            try{
                List<Integer> testCase = new ArrayList();
                testCase.add(1);
                testCase.add(2);
                testCase.add(3);
                TestCaseStatus testCaseStatus = new TestCaseStatus();
                Iterator<Integer> testCaseIterator = testCase.iterator();
                while (testCaseIterator.hasNext()){
                    switch (testCaseIterator.next()){
                        case 1:
                            testCaseStatus.setAtrVarification(true);
//                            testingController.changeImage1(this.localIccid);
//                            updateStatus(true);
                            break;
                        case 2:
                            this.profileTestingSuccessful = this.startProfilevalidationTesting();
                            if (this.profileTestingSuccessful){
                                testCaseStatus.setProfileVarification(true);
//                                testingController.changeImage2();
//                                updateStatus(true);
                            }else {
                                testCaseStatus.setProfileVarification(false);
                                updateStatus(false);
                            }
                            break;
                        case 3:
                            this.readWriteTestSuccessful = this.startReadWriteTest();
                            if (readWriteTestSuccessful){
                                testCaseStatus.setReadWriteTest(true);
                            }else {
                                testCaseStatus.setReadWriteTest(false);
//                                testingController.changeImage3();
                            }
                            break;
                        default:
                            interrupt();
                    }
                    }




            }catch (Exception exception){

            }
        }

    }

    private void updateStatus(boolean b) {
    }

    private boolean startReadWriteTest() {
            readWriteTest.runStressTesting();

            if(!readWriteTest.startStressTest()) {
                return false;
            }

            return true ;
        }

    private boolean startProfilevalidationTesting() throws IOException {
        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", this.terminal, this.card);
        boolean runPrifile = profileTest3G.runProfileTesting();
        if (!runPrifile) {
            return false;
        }
        return true;
    }
}
