package com.testDemo.netty.cp02.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/** 回调
 * @Auther: zouren
 * @Date: 2019/5/14 15:38
 * @Description:
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandler> {
    /**
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        //这是asynchronousServerSocketChannel可以处理多个客户端，需要继续调用它的accept方法，接收其它客户连接，最终形成一个循环
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
       //1M 物理内存
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //接收客户端的请求消息
        result.read(buffer,buffer,new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
