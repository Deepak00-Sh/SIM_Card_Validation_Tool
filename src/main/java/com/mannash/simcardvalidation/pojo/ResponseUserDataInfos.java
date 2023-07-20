package com.mannash.simcardvalidation.pojo;

import java.sql.Timestamp;
import java.util.Date;

public class ResponseUserDataInfos {
    private int id;

    @Override
    public String toString() {
        return "ResponseUserDataInfos{" +
                "id=" + id +
                ", usrDisplayName='" + usrDisplayName + '\'' +
                ", usrEmail='" + usrEmail + '\'' +
                ", usrContactNumber='" + usrContactNumber + '\'' +
                ", usrDescription='" + usrDescription + '\'' +
                ", usrCreatedBy='" + usrCreatedBy + '\'' +
                ", usrCreatedOn='" + usrCreatedOn + '\'' +
                ", rolePojo=" + rolePojo +
                ", responseUserDataPojo=" + responseUserDataPojo +
                '}';
    }

    private String usrDisplayName;
    private String usrEmail;
    private String usrContactNumber;
    private String usrDescription;
    private String usrCreatedBy;
    private String usrCreatedOn;
    private RolePojo rolePojo;
    private ResponseUserDataPojo responseUserDataPojo;

    public String getUsrCreatedOn() {
        return usrCreatedOn;
    }

    public void setUsrCreatedOn(String usrCreatedOn) {
        this.usrCreatedOn = usrCreatedOn;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsrDisplayName() {
        return usrDisplayName;
    }

    public void setUsrDisplayName(String usrDisplayName) {
        this.usrDisplayName = usrDisplayName;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    public String getUsrContactNumber() {
        return usrContactNumber;
    }

    public void setUsrContactNumber(String usrContactNumber) {
        this.usrContactNumber = usrContactNumber;
    }

    public String getUsrDescription() {
        return usrDescription;
    }

    public void setUsrDescription(String usrDescription) {
        this.usrDescription = usrDescription;
    }

    public String getUsrCreatedBy() {
        return usrCreatedBy;
    }

    public void setUsrCreatedBy(String usrCreatedBy) {
        this.usrCreatedBy = usrCreatedBy;
    }



    public RolePojo getRolePojo() {
        return rolePojo;
    }

    public void setRolePojo(RolePojo rolePojo) {
        this.rolePojo = rolePojo;
    }

    public ResponseUserDataPojo getResponseUserDataPojo() {
        return responseUserDataPojo;
    }

    public void setResponseUserDataPojo(ResponseUserDataPojo responseUserDataPojo) {
        this.responseUserDataPojo = responseUserDataPojo;
    }
}
