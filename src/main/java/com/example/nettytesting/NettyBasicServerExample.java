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
        // 多个工作线程组(接收到连接后, 需要register到某个eventGroup上)
        EventLoopGroup workGroup = new NioEventLoopGroup(4);

        // 服务端启动需要使用的serverbootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                // 设置channel类型, 这里使用的是NIO的服务端SocketChannel
                // 也可以直接设置使用EPoll, Poll等
                .channel(NioServerSocketChannel.class)
                // 具体工作处理, 处理相关SocketChannel的就绪事件
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // event触发的回调
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 按照顺序进行添加
                        ch.pipeline()
                                .addLast(new NormalMessageHandler()); // 处理IO事件
                    }
                });
        try {
            // 同步阻塞, 等待客户端连接 (bind是触发绑定处理)
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
