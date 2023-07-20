package com.mannash.simcardvalidation.pojo;

import java.util.Date;

public class ResponseUserDataPojo {
    private int id;
    private int usrDataFlag;

    @Override
    public String toString() {
        return "ResponseUserDataPojo{" +
                "id=" + id +
                ", usrDataFlag=" + usrDataFlag +
                ", usrDataVersion='" + usrDataVersion + '\'' +
                ", usrDataFlagUpdateTimestamp='" + usrDataFlagUpdateTimestamp + '\'' +
                ", usrDataVersionUpdateTimestamp='" + usrDataVersionUpdateTimestamp + '\'' +
                '}';
    }

    private String usrDataVersion;
    private String usrDataFlagUpdateTimestamp;
    private String usrDataVersionUpdateTimestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUsrDataFlagUpdateTimestamp() {
        return usrDataFlagUpdateTimestamp;
    }

    public void setUsrDataFlagUpdateTimestamp(String usrDataFlagUpdateTimestamp) {
        this.usrDataFlagUpdateTimestamp = usrDataFlagUpdateTimestamp;
    }

    public String getUsrDataVersionUpdateTimestamp() {
        return usrDataVersionUpdateTimestamp;
    }

    public void setUsrDataVersionUpdateTimestamp(String usrDataVersionUpdateTimestamp) {
        this.usrDataVersionUpdateTimestamp = usrDataVersionUpdateTimestamp;
    }


}
