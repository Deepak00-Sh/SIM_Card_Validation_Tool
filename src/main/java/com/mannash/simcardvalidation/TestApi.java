package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.common.UpdateRequestType;
import com.mannash.simcardvalidation.pojo.ResponseUserDataInfos;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;

public class TestApi {
    public static void main(String[] args) {
        String usrId = "dumy.user@gmail.com";
        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
        ResponseUserDataInfos pojo = service.getUserByEmail(usrId);
//        System.out.println(pojo);
        System.out.println(pojo.getResponseUserDataPojo().getUsrDataFlag());

        int status = service.updateUserData(usrId, pojo.getId(), UpdateRequestType.FLAG.toString(),1,"1234567890");
        System.out.println("Status code : "+status);
    }
}
