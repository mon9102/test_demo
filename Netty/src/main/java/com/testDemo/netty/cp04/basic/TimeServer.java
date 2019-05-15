package com.testDemo.netty.cp04.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty的NIO服务
 */
public class TimeServer {

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组，一个用于服务端接收客户连接，一个用于socketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//Netty用于启动NIO服务端的输助启动类
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)   //对应JDK NIO类库的ServerSocketChannel类
                    .option(ChannelOption.SO_BACKLOG, 1024) //NioServerSocketChannel的TCP参数
                    .childHandler(new ChildChannelHandler());//主要处理网络I/O事件，例如记录日志、对消息进行解码等
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();//ChannelFuture 主要用于异步操作的通知回调

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            arg0.pipeline().addLast(new TimeServerHandler());
        }

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new TimeServer().bind(port);
    }
}
