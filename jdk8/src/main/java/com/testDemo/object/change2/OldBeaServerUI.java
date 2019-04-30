package com.testDemo.object.change2;

import com.testDemo.object.CError;
import com.testDemo.object.CErrors;
import com.testDemo.object.VData;


public class OldBeaServerUI extends RestServiceUI {
//TODO vResult cCErros vInputData 移到父类，需要修改属性并要求命名统一 使用抽象类

    /**提交业务处理*/
    @Override
    public boolean submitData(VData vData,String Operater){
        vInputData = vData;
        //do something
        vResult = vInputData;
        return true;
    }
    /**获取处理结果*/
    public VData getResult(){
        return vResult;
    }
    /**获取错误信息*/
    public CErrors getErrors(){
        return cCErros;
    }



}
