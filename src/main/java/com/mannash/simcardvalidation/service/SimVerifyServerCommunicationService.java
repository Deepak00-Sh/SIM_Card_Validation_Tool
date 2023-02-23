package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.pojo.ResponseAuthenticationPojo;
import com.mannash.simcardvalidation.pojo.ResponseProfileTestingConfig;
import com.mannash.simcardvalidation.pojo.ResponseStressTestingConfig;

public interface SimVerifyServerCommunicationService {

    public ResponseAuthenticationPojo authenticateClient(String userName, String password);


    public ResponseProfileTestingConfig getProfileTestingConfig(String iccid, String userName,String woId);

    public ResponseStressTestingConfig getStressTestingConfig(String iccid, String woId, String userName);

    public void updateWOStatus(String woID, String iccid, String status, String userID);

}
