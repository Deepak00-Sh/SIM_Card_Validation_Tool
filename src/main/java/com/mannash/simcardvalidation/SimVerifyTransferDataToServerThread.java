package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.RequestSimVerificationCardPojos;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimVerifyTransferDataToServerThread extends Service<Void> {


    String userName = null;

    private String directoryPath = "..\\reports\\cache\\";
    private boolean running;

    public SimVerifyTransferDataToServerThread(String loggedInUserName) {
        this.userName = loggedInUserName;
        running = true;
    }


    private int loadCacheFromDisk(File file){
        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
        RequestSimVerificationCardPojos requestSimVerificationCardPojos = new RequestSimVerificationCardPojos();
        try {
            // Create a FileInputStream to read the serialized data from the cache file
            File cacheFile = new File(file.toURI());
            FileInputStream fis = new FileInputStream(cacheFile);

            // Create an ObjectInputStream to deserialize the data and read it from the FileInputStream
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read the list of data from the cache file and cast it to a List<String>
            List<ExportTestingResultPojo> data = (List<ExportTestingResultPojo>) ois.readObject();

            // Close the ObjectInputStream and FileInputStream
            ois.close();
            fis.close();

            int responseCode = 0;
            requestSimVerificationCardPojos.setRequestSimVerificationCardPojos(data);
            try {
                responseCode = service.sendReportsToServer(requestSimVerificationCardPojos);
            }catch (Exception e){
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void stop() {
        running = false;
    }

        private final ExecutorService executorService = Executors.newSingleThreadExecutor();
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                File directory = new File(directoryPath);
                TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
                while (running) {
//                    System.out.println("inside the while loop");
                    File[] files = directory.listFiles();
//                    System.out.println("Number of file present : " +files.length);
                    if (files != null && files.length > 0) {
//                        System.out.println("Files found in directory " + directoryPath + ":");
                        for (File file : files) {
//                            System.out.println(file.getName());
                            int responseCode = loadCacheFromDisk(file);
                            if (responseCode == 200){
                                file.delete();
                            }
                        }
                    } else {
//                        System.out.println("No files found in directory " + directoryPath);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep(5000); // wait for 5 seconds before checking again
                    } catch (InterruptedException e) {
//                        System.out.println("File checker thread interrupted");
                    }
                }
                return null;
            }
            };
        }

        @Override
        public boolean cancel() {
            super.cancel();
            executorService.shutdownNow();
            return false;
        }


}
