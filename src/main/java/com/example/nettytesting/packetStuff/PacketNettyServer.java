package com.example.nettytesting.packetStuff;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.charset.StandardCharsets;

public class PacketNettyServer {
    public static void main(String[] args) {
        // 配置
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 1. 使用固定长度编解码器, 避免粘包
//                        ch.pipeline().addLast(new FixedLengthFrameDecoder(36));
                        // 2. 使用分隔符解码器, 避免粘包
//                        ByteBuf delimiter = Unpooled.copiedBuffer("&".getBytes(StandardCharsets.UTF_8));
//                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(120, true, true, delimiter));
                        // 3. 长度域解码器, 避免粘包
                        ch.pipeline().addLast(
                                // 表示传输消息时, 在消息报文中增加4个字节的Length
                                new LengthFieldBasedFrameDecoder(1024 * 1024,
                                        0, 4, 0, 4));

                        ch.pipeline().addLast(new SimpleServerHandler());
                    }
                });
        // 开发端口
        try {
            ChannelFuture sync = serverBootstrap.bind(8080).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
