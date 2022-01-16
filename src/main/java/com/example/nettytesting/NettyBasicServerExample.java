package com.example.nettytesting;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 简单的主从Reactor模型
 */
public class NettyBasicServerExample {

    public static void main(String[] args) {

        // 主线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 多个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup(4);

        // 服务端启动需要使用的bootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // 构建Handler的初始
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 提前将Handler放入Pipeline中
                        ch.pipeline()
                                .addLast(new NormalMessageHandler()); // 处理IO事件
                    }
                });
        try {
            // 同步阻塞, 等待客户端连接
            ChannelFuture sync = serverBootstrap.bind(8080).sync();
            System.out.println("Netty Server Started Successfully");
            // 同步阻塞, 等待服务端监听端口关闭
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放线程组
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

}
