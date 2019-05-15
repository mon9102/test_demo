package com.testDemo.netty.jdk.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: zouren
 * @Date: 2019/5/14 15:29
 * @Description:
 */
public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel
                    .open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
   /*初始化CountDownLatch对象，允许当前线程一直阻塞，本例是让线程在此阻塞防止服务端执行完成退出。
实际项目中，不需要启动独立的线程来处理AsynchronousServerSocketChannel，这仅仅是个demo
* */
        latch = new CountDownLatch(1);
        //接收客户的连接，由于异步操作
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept() {
        asynchronousServerSocketChannel.accept(this,
                new AcceptCompletionHandler());
    }

}
