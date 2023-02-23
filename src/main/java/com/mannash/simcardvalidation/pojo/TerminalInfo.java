package com.mannash.simcardvalidation.pojo;

import javax.smartcardio.CardTerminal;

public class TerminalInfo {

    private String terminalCardIccid;
    private int terminalNumber;
    private CardTerminal ct;

    public String getWoId() {
        return woId;
    }

    public void setWoId(String woId) {
        this.woId = woId;
    }

    private String woId;

    public String getTerminalCardIccid() {
        return terminalCardIccid;
    }

    public void setTerminalCardIccid(String terminalCardIccid) {
        this.terminalCardIccid = terminalCardIccid;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public int getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(int terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public CardTerminal getCt() {
        return ct;
    }

    public void setCt(CardTerminal ct) {
        this.ct = ct;
    }




}
