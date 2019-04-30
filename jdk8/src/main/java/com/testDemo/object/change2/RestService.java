package com.testDemo.object.change2;

/**
 * @Date: 2019/4/30 10:53
 * @Description:
 */
public class RestService {



    private static String getErrors(){
        System.out.println("RestService getErrors");

        return "RestService getErrors";
    }
    private static String  getResult() {
        System.out.println("RestService getResult");

        return "RestService getResult";
    }
    //TODO 新增业务处理
    public static boolean submitData(RestServiceUI c){

        System.out.println("RestService submitData:"+c.getClass().getName());
        //do something
        c.setErro(getErrors());
        c.setResult(getResult());
        return true;
    }
}
