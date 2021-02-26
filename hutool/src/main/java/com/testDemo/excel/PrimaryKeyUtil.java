package com.testDemo.excel;

import lombok.Data;


/**
 **/
@Data
public class PrimaryKeyUtil {
    private SnowFlake snowFlake;
    /**
     * 分区字段前缀
     */
    private String partition="1";

    /**
     * 雪花算法数据中心标识
     */
    private long datacenterId=1L;

    /**
     * 雪花算法机器标识
     */
    private long machineId=1L;

    /**
     * 获取雪花算法主键
     *
     * @return
     */
    public Long snowFlakeKey() {
        return Long.parseLong (snowFlakeKeyStr());
    }
    private SnowFlake getSnowFlake(){
        if(snowFlake==null){
            snowFlake = new SnowFlake(datacenterId, machineId);
        }
       return snowFlake;

    }    /**
     * 获取雪花算法主键
     *
     * @return
     */
    public String snowFlakeKeyStr() {
        SnowFlake snowFlake=getSnowFlake();
        String id = partition + String.valueOf (snowFlake.nextId ());
        return id;
    }
}
