package com.testDemo.netty.cp02.nio;


import java.io.IOException;

/**NIO - 不考虑分包机制
 * 缓冲区　       Buffer    数据都在这处理
 * 通道　        Channel    读写数据，有网络(ServerSocketChannel)与文件(FileChannel)
 * 多路复用器    Selector   用来轮询注册在其上的Channel，如某个Channel有新的tcp，这个Channel处于就绪状态，会进行后续i/o操作
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
    //创建Reactor线程 并跑Selector
        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        for (int i=0;i<1;i++){
            new Thread(multiplexerTimeServer,"NIO-multiplexerTimeServer-00"+i).start();
            System.out.println("start  NIO-multiplexerTimeServer-00"+i);
        }



    }
}
