package com.testDemo.netty.cp10.http;

import com.sun.net.httpserver.HttpsParameters;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;

/**
 * @auther: zouren
 * @date: 2021/1/14
 * @description: 文件服务器的业务逻辑处理
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.getDecoderResult().isSuccess()) {
            //对http消息的解码失败  400
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (request.getMethod() != HttpMethod.GET) {
            //判断是从浏览器或者表单设置为GRT请求  405
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        final String uri = request.getUri();
        final String path = sanitizeUri(uri);
        if (path == null) {
            //uri不合法，返回403
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        //使用新的路径构造File对象
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            //文件不存在或者系统隐藏文件 404
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        if (file.isDirectory()) {
            //如是文件目录，则发送目录链接给客户端浏览器
            if (uri.endsWith("/")) {
                sendListing(ctx, file);
            } else {
                sendRedirect(ctx, uri + "/");
            }
            return;
        }

        if (!file.isFile()) {
            //不是合法文件 返回 403
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        //随机文件读写类
        RandomAccessFile randomAccessFile = null;
        try {
            //以只读方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            //文件打开失败 404
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        //文件长度
        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //消息头设置
        setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        //判断KeepAlive
        if (isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        //发送响应信息
        ctx.write(response);
        //通过Netty的ChunkedFile对象直接将文件写入到发送缓冲区
        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192)
                , ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {
                if (total < 0) {
                    System.err.println("Transfer progress:" + progress);
                } else {
                    System.err.println("Transfer progress:" + progress + "/" + total);
                }
            }

            /**
             * 发送完成
             * @param channelProgressiveFuture
             * @throws Exception
             */
            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("Transfer complete");
            }
        });
        //chunked编码，最后需要发一个编码结束的空消息体，将LastHttpContent的EMPTY_LAST_CONTENT发送到缓冲区
        //标识所有的消息体已经发送完成，同时调用flush方法将之前在发送缓冲区的消息刷新到SocketChannel中发送给对方
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!isKeepAlive(request)) {
            //非KeepAlive服务端主动关闭连接
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);

        }
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    /**
     * 对uri进行urlDecoder解码
     *
     * @param uri 使用当前运行程序的工程目录+uri构造绝对路径返回
     * @return
     */
    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new Error();
            }
        }
        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }
        //文件分隔符替换为本地系统的文件分隔符
        uri = uri.replace('/', File.separatorChar);
        //二次合法性校验
        if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) ||
                uri.startsWith(".") || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        //使用当前运行程序的工程目录+uri构造绝对路径返回
        return System.getProperty("user.dir") + uri;
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    /**
     * 显示当前文件夹下所有文件
     *
     * @param ctx
     * @param file
     */
    private static void sendListing(ChannelHandlerContext ctx, File file) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //设置消息头
        response.headers().set(CONTENT_TYPE, "text/html;charset=UTF-8");
        StringBuilder builder = new StringBuilder();
        String dirPath = file.getPath();
        builder.append("<!DOCTYPE html>\r\n");
        builder.append("<html><head>title>");
        builder.append(dirPath);
        builder.append(" 目录：");
        builder.append("</title></head><body>\r\n");
        builder.append("<h3>");
        builder.append(dirPath).append(" 目录：");
        builder.append("</h3>\r\n");
        builder.append("<ul>");
        builder.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        //当前目录所有文件
        for (File f : file.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            builder.append("<li>链接：<a href=\"");
            builder.append(name);
            builder.append("\">");
            builder.append(name);
            builder.append("</a></li>\r\n");
        }
        builder.append("</ul></body></html>\r\n");
        //分配对应消息的缓冲对象
        ByteBuf byteBuf = Unpooled.copiedBuffer(builder, CharsetUtil.UTF_8);
        //将缓冲区的内容放入http应答消息中
        response.content().writeBytes(byteBuf);
        //释放缓冲区
        byteBuf.release();
        //装响应消息发送到缓冲区并刷新到SocketChannel中
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status
                , Unpooled.copiedBuffer("Failure：" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getPath()));

    }
}
