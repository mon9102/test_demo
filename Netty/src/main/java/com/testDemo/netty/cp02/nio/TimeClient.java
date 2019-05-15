package com.testDemo.netty.cp02.nio;

/**
 * @Auther: zouren
 * @Date: 2019/5/13 18:01
 * @Description:
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(port);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        //创建Reactor线程
        TimeClientHandler timeServerHandler = new TimeClientHandler("127.0.0.1",port);
        for (int i=0;i<1;i++){
            new Thread(timeServerHandler,"NIO-timeServerHandler-00"+i).start();
            System.out.println("start  NIO-timeServerHandler-00"+i);

        }
    }
}
