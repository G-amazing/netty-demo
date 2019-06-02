package com.gamazing.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现客户端发送一个请求，服务器会返回 hello netty
 */
public class HelloServer {
    public static void main(String[] args) throws Exception{
        // =====定义一对线程组（也就是两个线程池）=====
        // 主线程组，用于接受客户端的连接，但是不做任何事情
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组，主线程组会把任务丢给他
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // netty服务器的创建，serverBootstrap 是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)          // 设置主从线程组
                           .channel(NioServerSocketChannel.class)  // 设置nio的双向通道
                           .childHandler(new HelloServerInitializer());                    // 子处理器，用于处理 workerGroup

            // 启动服务 并且设置8088为启动端口号，同时启动的方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 监听关闭的 channel ，设置为同步方法
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
