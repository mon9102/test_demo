package com.testDemo.object;

/**
 * @Auther: zouren
 * @Date: 2019/4/30 10:53
 * @Description:
 */
public class RestService {
    private String Errors;

    public static void do1(){
        System.out.println("RestService do1");
    }
    public static String getErrors(){
        System.out.println("RestService getErrors");

        return "RestService getErrors";
    }
    public static String  getResult() {
        System.out.println("RestService getResult");

        return "RestService getResult";
    }
    public static boolean submitData(RestServiceUI c){

        System.out.println("RestService submitData:"+c.getClass().getName());
        //do something
        c.setErro(getErrors());
        c.setResult(getResult());
        return true;
    }
}
