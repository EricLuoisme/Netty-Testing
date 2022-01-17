package com.example.nettytesting.nettyProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtocolClient {
    public static void main(String[] args) {

        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        bs.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024,
                                        9, 4, 0, 0))
                                .addLast(new MessageRecordEncoder())
                                .addLast(new MessageRecordDecoder())
                                .addLast(new ProtocolClientHandler());
                    }
                });

        try {
            // 异步获取建立好的channel
            ChannelFuture channelFuture = bs.connect("localhost", 8080).sync();
            // 准备发送消息
            Channel channel = channelFuture.channel();
            for (int i = 0; i < 1; i++) {
                MessageRecord record = new MessageRecord();
                Header header = new Header();
                header.setSessionId(100001);
                header.setReqType(OpCode.REQ.getCode());
                record.setHeader(header);
                String body = "Request Msg:" + i;
                record.setBody(body);
                // 传输
                channel.writeAndFlush(record);
            }
            // 同步等待Server端的Channel执行完, 主线程才继续往下走
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
