package com.testDemo.netty.cp04.LineBasedFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 增加拆包与粘包功能
 * 使用LineBasedFrameDecoder来实现
 * LineBasedFrameDecoder 工作原理依次遍历ByteBuf中的可读字节，判断看是否有"\n"或"\r\n"，以此为结束位置
 * StringDecoder 将接收的对象转为字符串
 */
public class TimeServer {

    public void bind(int port) throws Exception {
	// 配置服务端的NIO线程组
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .option(ChannelOption.SO_BACKLOG, 1024)
		    .childHandler(new ChildChannelHandler());
	    // 绑定端口，同步等待成功
	    ChannelFuture f = b.bind(port).sync();

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
	    arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//增加解码器
	    arg0.pipeline().addLast(new StringDecoder());//增加解码器
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
