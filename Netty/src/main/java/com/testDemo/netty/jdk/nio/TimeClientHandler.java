package com.testDemo.netty.jdk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Auther: zouren
 * @Date: 2019/5/13 18:01
 * @Description:
 */
public class TimeClientHandler implements Runnable  {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private  volatile boolean stop;

    /**
     *
     * @param host
     * @param port
     */
    public TimeClientHandler(String  host, int port) {
        this.host=host==null?"127.0.0.1":host;
        this.port=port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            System.out.println("key.cancel");
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                                System.out.println("key.channel.close");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        //selector关闭时，所有注册在上面的Channel等资源都会被自动去注册并关闭
        if (selector != null) {
            try {
                selector.close();
                System.out.println("selector.close");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doConnect() throws IOException {
        //如果直接连接成功，则注册到selector, 发送消息，读应答
        if (socketChannel.connect(new InetSocketAddress(this.host,this.port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
            System.out.println("doConnect. OP_READ");
            doWrite(socketChannel);
        }else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
            System.out.println("doConnect. OP_CONNECT");
        }
    }
    /**
     * @param channel
     * @throws IOException
     */
    private void doWrite(SocketChannel channel) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();

            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            //把缓冲区position设置为0
            writeBuffer.flip();
            channel.write(writeBuffer);
            if(!writeBuffer.hasRemaining()){
                System.out.println("Send order 2 server succeed.");
            }
    }

    /**
     * 根据SelectionKey 建立连接并操作
     *
     * @param key
     * @throws IOException
     */
    private void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            //判断是否连接成功 发送信息
            if (key.isConnectable()) {
                System.out.println("handleInput isConnectable");
                if(sc.finishConnect()){
                    System.out.println("handleInput finishConnect");
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else {
                    //连接失败，退出
                    System.exit(1);
                }
            }
            //服务端回复信息
            if (key.isReadable()) {
                System.out.println("handleInput isReadable");
                //1K的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    System.out.println("handleInput isReadable  readBytes > 0");
                    //把缓冲区position设置为0
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "utf-8");
                    System.out.println("Now is :" + body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                    System.out.println("this time client 端链路关闭");
                } else {
                    //读到0字节忽略
                }
            }
        }
    }

    
}
