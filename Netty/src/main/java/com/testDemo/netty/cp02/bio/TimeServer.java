package com.testDemo.netty.cp02.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** 传统的BIO，
 * 每次连接都new一个线程，多台访问内存溢出
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
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
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
