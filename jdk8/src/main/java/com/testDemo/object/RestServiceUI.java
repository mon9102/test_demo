package com.testDemo.object;

/**
 * @Auther: zouren
 * @Date: 2019/4/30 11:16
 * @Description:
 */
public interface RestServiceUI {
    /**
     * 增加一个错误信息
     * @param erro
     */
    public void setErro(String erro);

    /**
     * 增加一个错误对象
     * @param cError
     */
    public void setErro(CError cError);

    /**
     * 对返回结果增加一个对象
     * @param result
     */
    public void setResult(Object result);
    public boolean submitData(VData vData,String Operater);
}
