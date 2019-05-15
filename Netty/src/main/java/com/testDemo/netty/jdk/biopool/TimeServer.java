package com.testDemo.netty.jdk.biopool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**伪异步BIO
 * 每次连接都放到线程池中，解决内存溢出，但本质还是BIO
 * @Auther: zouren
 * @Date: 2019/5/13 17:53
 * @Description:
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(port);
            } catch (NumberFormatException e) {

            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            //任务线程池
            TimeServerHandlerExecutePool timeServerHandlerExecutePool = new TimeServerHandlerExecutePool(50,1000);
            while (true) {
                socket = server.accept();
                timeServerHandlerExecutePool.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
