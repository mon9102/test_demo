package com.testDemo.object.change2;

import com.testDemo.object.CError;
import com.testDemo.object.CErrors;
import com.testDemo.object.VData;

/**
 * @Date: 2019/4/30 11:16
 * @Description:
 */
public abstract class RestServiceUI {
    protected VData vResult = new VData();

    protected CErrors cCErros =  new CErrors();

    protected VData vInputData = new VData();
    /**
     * 增加一个错误信息
     * @param erro
     */
    public void setErro(String erro){

    }

    /**
     * 增加一个错误对象
     * @param cError
     */
    public void setErro(CError cError){

    }

    /**
     * 对返回结果增加一个对象
     * @param result
     */
    public void setResult(Object result){

    }
    public abstract boolean submitData(VData vData, String Operater);
}
