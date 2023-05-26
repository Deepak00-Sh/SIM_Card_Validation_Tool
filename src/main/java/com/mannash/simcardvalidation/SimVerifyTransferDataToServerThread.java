package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.RequestSimVerificationCardPojos;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimVerifyTransferDataToServerThread extends Service<Void> {


    String userName = null;

    private String directoryPath = "..\\reports\\cache\\";
    private boolean running;
    CheckUpdate checkUpdate = new CheckUpdate();
    ImageView startButton;

    public SimVerifyTransferDataToServerThread(String loggedInUserName) {
        this.userName = loggedInUserName;
        running = true;
    }

    public SimVerifyTransferDataToServerThread(ImageView imgTestButton) {
        this.startButton = imgTestButton;
    }


    private int loadCacheFromDisk(File file) {
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

    public void stop() {
        running = false;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

//    @Override
//    protected Task<Void> createTask() {
//        String user = this.userName;
//        ImageView tempStartButton = this.startButton;
//        return new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                File directory = new File(directoryPath);
//                TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
//                Properties properties = new Properties();
//                String ackFileName = TestingController4.ACK_FILE_PATH + "cacheAcknowledgement.properties";
//
//                while (running) {
//
//                    System.out.println("################################################################################");
//                    System.out.println();
//                    System.out.println("Running transfer data thread");
//                    try {
//                        FileInputStream fileInputStream = new FileInputStream(ackFileName);
//                        properties.load(fileInputStream);
//                        fileInputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Set<String> keys = properties.stringPropertyNames();
//                    String[] keyArray = keys.toArray(new String[0]);
//
//
////                    String isUserAccessed = null;
////                    System.out.println("data transfer thread is runnuing");
////                    try {
////                        isUserAccessed = checkUpdate.checkUserAccess(user);
////                        System.out.println("isUserAccessed "+isUserAccessed);
////                    }catch (Exception e){
////                        e.printStackTrace();
////                    }
////
////
////                    if (isUserAccessed.equalsIgnoreCase("no")){
////                        try {
////                            TestingController4 testObj = new TestingController4();
////                            testObj.disableStartButton();
//////                            tempStartButton.setDisable(true);
////                        }catch (Exception e){
////                            e.printStackTrace();
////                        }
//
////                    }
////                    System.out.println("inside the while loop");
//                    System.out.println("Size of the key array : "+keyArray.length);
//                    File[] files = directory.listFiles();
//                    System.out.println("Size of the files array : "+files.length);
////                    System.out.println("Number of file present : " +files.length);
//                    if (files != null && files.length > 0) {
//                        for (String key : keyArray) {
//                            System.out.println("key : "+key+" value : "+properties.getProperty(key));
//                            if (properties.getProperty(key).equals("0")) {
//                                System.out.println("Key is 0, inside the if condition");
////                        System.out.println("Files found in directory " + directoryPath + ":");
//                                for (File file : files) {
//                                    System.out.println("inside the file for loop");
//                                    System.out.println("File : "+file.getName());
////                            System.out.println(file.getName());
//                                    if (file.getName().toString().equals(key)) {
//                                        System.out.println("Key and file get matched !!");
//                                        int responseCode = loadCacheFromDisk(file);
//                                        if (responseCode == 200) {
//                                            try {
//                                                properties.setProperty(key, "1");
//                                                // Save the modified properties back to the file
//                                                OutputStream outputStream = new FileOutputStream(ackFileName);
//                                                properties.store(outputStream, null);
//                                                outputStream.close();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            System.out.println("value of key after the updation : "+properties.getProperty(key));
//
////                                            try {
////                                                file.delete();
////                                            } catch (Exception e) {
////                                                e.printStackTrace();
////                                            }
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else {
////                        System.out.println("No files found in directory " + directoryPath);
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                    try {
//                        Thread.sleep(5000); // wait for 5 seconds before checking again
//                    } catch (InterruptedException e) {
////                        System.out.println("File checker thread interrupted");
//                    }
//                }
//                return null;
//            }
//        };
//    }


    //----------------------------------------------------------

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File directory = new File(directoryPath);
                TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
                while (running) {
// System.out.println("inside the while loop");
                    File[] files = directory.listFiles();
// System.out.println("Number of file present : " +files.length);
                    if (files != null && files.length > 0) {
// System.out.println("Files found in directory " + directoryPath + ":");
                        for (File file : files) {
                            if (file.isDirectory()){
                                continue;
                            }
                            System.out.println(file.getName());
                            int responseCode = loadCacheFromDisk(file);
                            System.out.println("Sending reports response : " + responseCode);
                            if (responseCode == 200) {
                                System.out.println("Reports sends to server successfully!!");
                                System.out.println("Going to delete the file : " + file);
                                file.delete();
                                if (file.exists()) {
                                    System.out.println("File could not be deleted!!");
                                    file.delete();
                                }
                            }


                        }
                    } else {
// System.out.println("No files found in directory " + directoryPath);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep(5000); // wait for 5 seconds before checking again
                    } catch (InterruptedException e) {
// System.out.println("File checker thread interrupted");
                    }
                }
                return null;
            }
        };
    }


    //-----------------------------------------------------

    @Override
    public boolean cancel() {
        super.cancel();
        executorService.shutdownNow();
        return false;
    }


}
