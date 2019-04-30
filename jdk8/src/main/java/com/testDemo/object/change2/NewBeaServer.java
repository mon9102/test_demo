package com.testDemo.object.change2;

import com.testDemo.object.VData;

/**
 * @Date: 2019/4/30 10:28
 * @Description:
 */
public class NewBeaServer extends OldBeaServerUI {
    //TODO 增加子类重写
    @Override
    public boolean submitData(VData vData, String Operater){
        System.out.println("BeaOneServer submitData");

        return  RestService.submitData(this);
    }
    public static OldBeaServerUI builder(){
        return new NewBeaServer();
    }
}
