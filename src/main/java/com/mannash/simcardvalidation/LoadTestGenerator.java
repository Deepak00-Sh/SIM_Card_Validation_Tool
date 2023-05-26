package com.mannash.simcardvalidation;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.RequestSimVerificationCardPojos;
import com.mannash.simcardvalidation.pojo.ResponseAuthenticationPojo;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class LoadTestGenerator {

    public static void main(String[] args) {
        int tps = 50;
        for (int i=0; i<tps ; i++){
            ApiCallerThread thread = new ApiCallerThread();
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
