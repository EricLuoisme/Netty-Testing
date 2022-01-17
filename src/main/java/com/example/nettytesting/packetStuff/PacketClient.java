package com.example.nettytesting.packetStuff;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.charset.StandardCharsets;

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
//                        // 1. 使用固定长度编解码器, 避免粘包
//                        ch.pipeline().addLast(new FixedLengthFrameDecoder(36));
                        // 2. 使用分隔符解码器, 避免粘包
//                        ByteBuf delimiter = Unpooled.copiedBuffer("&".getBytes(StandardCharsets.UTF_8));
//                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(128, true, true, delimiter));
                        // 3. 长度域解码器, 避免粘包
                        ch.pipeline().addLast(
                                // 表示传输消息时, 在消息报文中增加4个字节的Length
                                new LengthFieldPrepender(4, 0, false));

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
