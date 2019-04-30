package com.testDemo.object.change1;

import com.testDemo.object.CError;
import com.testDemo.object.CErrors;
import com.testDemo.object.VData;

/**
 * @Date: 2019/4/30 10:27
 * @Description:
 */
public class OldBeaServerUI implements RestServiceUI{

    private VData vResult = new VData();

    private CErrors cCErros =  new CErrors();

    private VData vInputData = new VData();
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

//TODO 需要实现3个接口，不需要修改属性  使用接口
    @Override
    public void setErro(String erro) {
        cCErros.addOneError(erro);
    }
    @Override
    public void setErro(CError cError) {
        cCErros.addOneError(cError);
    }
    @Override
    public void setResult(Object result) {
        vResult.add(result);
    }
}
