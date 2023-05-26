package com.mannash.simcardvalidation;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.RequestSimVerificationCardPojos;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

public class ApiCallerThread extends Thread{

    int timeout = 15000;
    List<ExportTestingResultPojo> data = null;

    public void loadCacheFromDisk() {
//        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
        RequestSimVerificationCardPojos requestSimVerificationCardPojos = new RequestSimVerificationCardPojos();
        try {
            // Create a FileInputStream to read the serialized data from the cache file
            File cacheFile = new File("D:\\Work\\Delivery_singlecard\\Released_080523_638\\SIMVerify_SinglePort\\bin\\Test\\20230516_181851.ser");
            FileInputStream fis = new FileInputStream(cacheFile);

            // Create an ObjectInputStream to deserialize the data and read it from the FileInputStream
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read the list of data from the cache file and cast it to a List<String>
            data = (List<ExportTestingResultPojo>) ois.readObject();

            // Close the ObjectInputStream and FileInputStream
            ois.close();
            fis.close();



//            // to be deleted
//            File logs = new File("..\\logs");
//            if(!logs.exists()){
//                logs.mkdir();
//            }
//
//            File testFile = new File("..\\logs\\test.txt");
//            if (!testFile.exists()){
//                testFile.createNewFile();
//            }
//            for (ExportTestingResultPojo element: data) {
//                System.out.println(element.getTerminalICCID());
//                FileWriter writer = new FileWriter(testFile, true);
//                BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.append("Date : "+element.getDateOfTesting()
//                        + " Time : "+element.getTimeOfTesting()
//                        +" Iccid : "+element.getTerminalICCID()
//                        +" Status : "+element.getCardStatus()
//                        +"\n");
//                bufferedWriter.close();
//            }
//            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int loadCacheFromDisk2() {
//        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
        RequestSimVerificationCardPojos requestSimVerificationCardPojos = new RequestSimVerificationCardPojos();
        try {
            int responseCode = 0;
            requestSimVerificationCardPojos.setRequestSimVerificationCardPojos(data);
            try {
                responseCode = sendReportsToServer(requestSimVerificationCardPojos);
            } catch (Exception e) {
//                System.out.println("Request of sending result failed");
            }

//            System.out.println("Server response code : "+responseCode);
            return responseCode;

//            // to be deleted
//            File logs = new File("..\\logs");
//            if(!logs.exists()){
//                logs.mkdir();
//            }
//
//            File testFile = new File("..\\logs\\test.txt");
//            if (!testFile.exists()){
//                testFile.createNewFile();
//            }
//            for (ExportTestingResultPojo element: data) {
//                System.out.println(element.getTerminalICCID());
//                FileWriter writer = new FileWriter(testFile, true);
//                BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.append("Date : "+element.getDateOfTesting()
//                        + " Time : "+element.getTimeOfTesting()
//                        +" Iccid : "+element.getTerminalICCID()
//                        +" Status : "+element.getCardStatus()
//                        +"\n");
//                bufferedWriter.close();
//            }
//            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }




    public int sendReportsToServer(RequestSimVerificationCardPojos pojoList){


        // Create an instance of HttpClient
        HttpClient client = HttpClientBuilder.create().build();
        HttpURLConnection conn = null;
        int statusCode = 0;

        try {
            // Set the URL of the API
            String completeUrl = "http://103.228.113.86:32100/svr/receive";
            URL url = new URL(completeUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");


            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);


            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            Gson gson = new Gson();
            String json = gson.toJson(pojoList);

            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                writer.write(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the response code from the connection

            try {
                statusCode = conn.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Status code : "+statusCode);

        } catch (SocketTimeoutException e) {
            System.out.println("Request timed out");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

//		// Convert the pojo to a JSON string using Gson library
//		Gson gson = new Gson();
//		String json = gson.toJson(pojoList);
//
//		// Create an instance of HttpPost with the URL
//		HttpPost post = new HttpPost(url);
//
//		// Set the content type of the request to application/json
//		post.setHeader("Content-type", "application/json");
//
//		// Set the JSON string as the content of the request
//		try {
//			post.setEntity(new StringEntity(json));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// Execute the request and get the response
//		HttpResponse response = null;
//		try {
//			response = client.execute(post);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		int statusCode = response.getStatusLine().getStatusCode();
//
//		System.out.println("Status code : "+statusCode);
//
//
////		// Convert the response to a string
////		String responseString = null;
////		try {
////			responseString = EntityUtils.toString(response.getEntity());
////		} catch (IOException e) {
////			throw new RuntimeException(e);
////		}
////
////		// Print the response
////		System.out.println(responseString);

        return statusCode;

    }


    @Override
    public void run() {
//        ApiCallerThread obj = new ApiCaller();

        loadCacheFromDisk();
        int responseCode = 0;

        for (int i=0; i<1000; i++){
//            ResponseAuthenticationPojo pojo = obj.authenticateClient("admin","123");
            try {
                responseCode = loadCacheFromDisk2();
                System.out.println("Response code : "+responseCode);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
