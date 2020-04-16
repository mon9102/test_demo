package com.testDemo.netty.cp02.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**读取半包消息和发送应答，本例不对半包操作
 * @Auther: zouren
 * @Date: 2019/5/14 15:56
 * @Description:
 */
public class ReadCompletionHandler implements
        CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if (this.channel == null)
            this.channel = channel;
    }

    /**
     * 读取到消息后的处理
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //从缓冲读到数据
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            //客户端请求的内容
            String req = new String(body, "UTF-8");
            System.out.println("The time server receive order : " + req);
            //判断是 QUERY TIME ORDER 返回系统时间
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new java.util.Date(
                    System.currentTimeMillis()).toString() : "BAD ORDER";
            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把信息发送给客户端
     * @param currentTime
     */
    private void doWrite(String currentTime) {
        //进行合法性校验
        if (currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = (currentTime).getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer, writeBuffer,
                    new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            // 如果没有发送完成，继续发送
                            if (buffer.hasRemaining()){
                                channel.write(buffer, buffer, this);
                            }

                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                channel.close();
                            } catch (IOException e) {
                                // ingnore on close
                            }
                        }
                    });
        }
    }

    /**
     * 有异常时 释放资源
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
