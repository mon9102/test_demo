package com.testDemo.object.change1;

import com.testDemo.object.VData;

/**
 * @Auther: zouren
 * @Date: 2019/4/30 10:28
 * @Description:
 */
public class NewBeaServer extends OldBeaServerUI {
    @Override
    public boolean submitData(VData vData, String Operater){
        System.out.println("BeaOneServer submitData");

        return  RestService.submitData(this);
    }
    public static OldBeaServerUI builder(){
        return new NewBeaServer();
    }
}
