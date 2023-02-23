package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.pojo.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class SimVerifyServerCommunicationServiceImpl implements SimVerifyServerCommunicationService{


    @Override
    public ResponseAuthenticationPojo authenticateClient(String userName, String password) {
        ResponseAuthenticationPojo responseAuthenticationPojo = new ResponseAuthenticationPojo();
        if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
            responseAuthenticationPojo.setMessage("Username or Password can not be empty");
            return responseAuthenticationPojo;
        }

        else {
            try {
                org.apache.http.client.HttpClient client = new DefaultHttpClient();
                String completeUrl = "http://localhost:8080/trakmeserver/api/external/auth/validateUser?userId="
                        + userName + "&password=" + URLEncoder.encode(password);

                HttpPost httpPost = new HttpPost(completeUrl);
                httpPost.setHeader("Content-type", "application/json");
                // this.logger.info("Sending request to TrakmeServer for authenticating user : " + userName);
                HttpResponse response = client.execute(httpPost);
                if (response != null) {
                    String responseString = EntityUtils.toString((HttpEntity) response.getEntity());
                    if (response.getStatusLine().getStatusCode() != 200) {
                        if (responseString != null) {
                            if (response.getStatusLine().getStatusCode() == 401) {
                                responseAuthenticationPojo.setMessage("Invalid username/password");
                                // this.logger.error("Invalid Username & Password , Status code : " + 401);
                                responseAuthenticationPojo.setStatusCode(401);
                            } else {
                                responseAuthenticationPojo.setMessage("Unable to authenticate user");
                                // this.logger.debug("Unable to authenticate user with Status code : " + response.getStatusLine().getStatusCode());
                                responseAuthenticationPojo.setStatusCode(response.getStatusLine().getStatusCode());
                            }
                        } else {
                            responseAuthenticationPojo.setMessage("Unable to authenticate user");
                            responseAuthenticationPojo.setStatusCode(500);
                            // this.logger.debug("Unable to authenticate user with Status code : " + 500);
                        }
                    } else {
                        responseAuthenticationPojo.setStatusCode(200);
                        // this.logger.info("TrakmeServer authenticated successfully with user" + userName);
                        // System.out.println("Status Code : " + 200);
                    }
                } else {
                    responseAuthenticationPojo.setMessage("Unable to authenticate user");
                    responseAuthenticationPojo.setStatusCode(500);
                    // this.logger.debug("Unable to authenticate user, response is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseAuthenticationPojo.setMessage("Unable to authenticate user");
                responseAuthenticationPojo.setStatusCode(500);
                // this.logger.info("Unable to authentcate user");
            }
            return responseAuthenticationPojo;
        }
    }

    @Override
    public ResponseProfileTestingConfig getProfileTestingConfig(String iccid, String userName, String woId) {
        CloseableHttpClient client = HttpClients.createDefault();
        ResponseProfileTestingConfig responseProfileTestingConfig = new ResponseProfileTestingConfig();
        try {

            String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
                    + userName + "&woId=" + woId;

            // this.logger.debug("Calling  Server : " + completeUrl);

            HttpGet get = new HttpGet(completeUrl);

            Gson gson = new Gson();
            CloseableHttpResponse response = client.execute(get);
//			String responseString = "{\"fileSystemConfig\":[\"2F05,3F00,T,ALWAYS,CHV1,ADM,ADM,YES,NA,NA,8\",\"2FE2,3F00,T,ALWAYS,NEVER,ADM,ADM,NO,NA,NA,10\",\"2F00,3F00,LF,ALWAYS,ADM,ADM,ADM,YES,53,1,53\"],\"fileContentConfig\":[\"2F05,3F00,T,ALWAYS,1,1,FFFFFFFFFFFFFFFF\",\"2FE2,3F00,T,ALWAYS,1,1,ICCIDI\",\"2F00,3F00,LF,ALWAYS,1,1,41697274656C203447\"]}";
            // this.System.out.println("Sever Response : " + responseString);
            String responseString = EntityUtils.toString(response.getEntity());

            ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = (ResponseFieldTestingProfileConfigPojo) gson
                    .fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
            responseProfileTestingConfig
                    .setFileContentConfig(responseFieldTestingProfileConfigPojo.getFileContentConfigs());
            responseProfileTestingConfig
                    .setFileSystemConfig(responseFieldTestingProfileConfigPojo.getFileSystemConfigs());
            return responseProfileTestingConfig;

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

        finally {

            try {

                client.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    }

    @Override
    public ResponseStressTestingConfig getStressTestingConfig(String iccid, String woId, String userName) {

        CloseableHttpClient client = HttpClients.createDefault();
        ResponseStressTestingConfig responseStressTestingConfig = new ResponseStressTestingConfig();

        try {
            String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
                    + userName + "&woId=" + woId;

            // this.logger.debug("Calling  Server : " + completeUrl);

            HttpGet get = new HttpGet(completeUrl);

            Gson gson = new Gson();
            CloseableHttpResponse response = client.execute(get);
            // String responseString =
            // "{\"apduList\":[\"RESET\",\"A0A40000023F00\",\"A0A40000027F20\",\"A0A40000026F7E\"],\"loopCount\":10000,\"startCounter\":1}";
            // this.System.out.println("Sever Response : " + responseString);
            String responseString = EntityUtils.toString(response.getEntity());
            ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = (ResponseFieldTestingProfileConfigPojo) gson
                    .fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
            responseStressTestingConfig.setApduList(responseFieldTestingProfileConfigPojo.getStressTestingApdus());
            responseStressTestingConfig.setLoopCount(responseFieldTestingProfileConfigPojo.getStressTestingLoopCount());
            responseStressTestingConfig.setStartCounter(1);

            return responseStressTestingConfig;

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

        finally {

            try {

                client.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    }


    @Override
    public void updateWOStatus(String iccid, String status, String userName,String woID) {

        CloseableHttpClient client = HttpClients.createDefault();

        try {

            String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/client/wo/status?usrId="
                    + userName + "&woId=" + woID + "&iccid=" + iccid + "&status=" + status;

            // this.logger.debug("Calling  Server : " + completeUrl);

            HttpPost post = new HttpPost(completeUrl);

            Gson gson = new Gson();
            CloseableHttpResponse response = client.execute(post);
            String responseString = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String sendLogsToServer(List<RequestClientLogPojo> requestLogPojos) {

        CloseableHttpClient client = HttpClients.createDefault();
        try {

            String logPushUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/logs/post";
            HttpPost post = new HttpPost(logPushUrl);
            RequestClientLogsPojo pushRequest = new RequestClientLogsPojo();
            pushRequest.setRequestClientLogPojos(requestLogPojos);

            Gson gson = new Gson();

            StringEntity input = new StringEntity(gson.toJson(pushRequest));
            input.setContentType("application/json");
            post.setEntity(input);

            // this.logger.info("Sending logs to server");

            CloseableHttpResponse response = client.execute(post);

            String responseString = EntityUtils.toString(response.getEntity());

//			  ServerResponseLogPojo serverResponse = (ServerResponseLogPojo) gson.fromJson(responseString, ServerResponseLogPojo.class);
            String status = "OK";
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
