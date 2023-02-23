package com.mannash.simcardvalidation.pojo;

public class TestCaseStatus {
    public boolean isAtrVarification() {
        return AtrVarification;
    }

    public void setAtrVarification(boolean atrVarification) {
        AtrVarification = atrVarification;
    }

    public boolean isProfileVarification() {
        return profileVarification;
    }

    public void setProfileVarification(boolean profileVarification) {
        this.profileVarification = profileVarification;
    }

    public boolean isReadWriteTest() {
        return readWriteTest;
    }

    public void setReadWriteTest(boolean readWriteTest) {
        this.readWriteTest = readWriteTest;
    }

    private boolean AtrVarification=false;
    private boolean profileVarification=false;
    private boolean readWriteTest = false;
}
