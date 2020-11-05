package com.testDemo.netty.cp02.aio;

import java.io.IOException;

/**AIO   NIO2的异步
 * AsyncTimeServerHandler-》AcceptCompletionHandler-》ReadCompletionHandler
 *异步回调是由sun.nio.chAsynchronousChannelGroupImpl实现，它经过层层调用，最终回调
 * com.phei.netty.aio.AsyncTimeClientHandler$1.completed方法完成回调通知。
 * 异步SocketChannel是被动执行对象。
 *AsynchronousServerSocketChannel和AsynchronousSocketChannel，都是由JDK底层线程池
 * 负责回调并读写
 *
 * @Auther: zouren
 * @Date: 2019/5/13 17:53
 * @Description:
 */
public class TimeServer {

    /**AIO   NIO2的异步
     * AIO其实是一种在读写操作结束之前允许进行其他操作的I/O处理。
     * AIO是对JDK1.4中提出的同步非阻塞I/O(NIO)的进一步增强。
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
