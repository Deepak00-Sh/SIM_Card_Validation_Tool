package com.mannash.simcardvalidation.pojo;

public class RequestUserDataPojo {
    private int id;
    private String updateRequestType;
    private int usrDataFlag;
    private String usrDataVersion;
    private String usrEmail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateRequestType() {
        return updateRequestType;
    }

    public void setUpdateRequestType(String updateRequestType) {
        this.updateRequestType = updateRequestType;
    }

    public int getUsrDataFlag() {
        return usrDataFlag;
    }

    public void setUsrDataFlag(int usrDataFlag) {
        this.usrDataFlag = usrDataFlag;
    }

    public String getUsrDataVersion() {
        return usrDataVersion;
    }

    public void setUsrDataVersion(String usrDataVersion) {
        this.usrDataVersion = usrDataVersion;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

}
