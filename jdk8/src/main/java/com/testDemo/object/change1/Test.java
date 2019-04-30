package com.testDemo.object.change1;

import com.testDemo.object.VData;

/**
 * @Auther: zouren
 * @Date: 2019/4/30 10:29
 * @Description:
 */
public class Test {
    @org.junit.jupiter.api.Test
    public void test() {
        OldBeaServerUI b =  NewBeaServer.builder();
        VData vInputData = new VData();

        b.submitData(vInputData,"");
        b.getErrors();
        b.getResult();
    }
}
