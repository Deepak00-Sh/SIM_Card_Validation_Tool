package com.mannash.simcardvalidation;

import java.io.File;

public class TestEnigma {

    public static void main(String[] args) {
        File file1 = new File("C:\\ProgramData\\archive");
        if (!file1.exists()){
            try {
                file1.mkdir();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        System.out.println("exit");
    }
}
