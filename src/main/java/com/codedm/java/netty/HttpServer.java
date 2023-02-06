package com.codedm.java.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * HttpServer 启动类
 *
 * @author Dongming WU
 */
public class HttpServer {

    public static void main(String[] args) throws InterruptedException {
        // 接收连接，但是不处理
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        // 真正处理连接的Group
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            // Netty 的启动类
            ServerBootstrap server = new ServerBootstrap();
            server.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer());
            //绑定监听端口
            ChannelFuture channel = server
                    .bind(9999).sync();
            channel.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

}
