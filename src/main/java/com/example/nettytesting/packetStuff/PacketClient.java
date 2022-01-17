package com.example.nettytesting.packetStuff;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 可以很明显的看出, 没有使用编解码器进行通讯, Client发送的内容, 以及接收的内容
 * 会产生粘包问题, 导致数据无法被读出
 */
public class PacketClient {
    public static void main(String[] args) {
        // 配置
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleClientHandler());
                    }
                });
        // 连接
        try {
            ChannelFuture localhost = clientBootstrap.connect("localhost", 8080).sync();
            localhost.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭
            eventLoopGroup.shutdownGracefully();
        }
    }
}
