package com.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Pgp4Shell {
    public static String callCmd(String command) throws IOException {
        try{
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            p.getOutputStream().flush();
            p.getOutputStream().close();

            String message="";
            StringBuffer result = new StringBuffer();
            while((message = br.readLine())!= null){
                result.append(message).append("\n");
            }
            return result.toString();
        } catch (IOException e) {

            return e.getMessage();

        }

    }

    public static void main(String[] args) {
        try {
            System.out.println(callCmd("C:\\Users\\wangh\\Desktop\\pgpEncryptAndSign.bat"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
