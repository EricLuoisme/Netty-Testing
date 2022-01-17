package com.example.nettytesting.packetStuff;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    int count = 0;

    /**
     * 一旦Channel建立成功调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client successfully create channel connection");
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("Client msg:" + i + "&", StandardCharsets.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }
        super.channelActive(ctx);
    }

    /**
     * 一旦Channel读取到数据调用, 也就是收到Server响应的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive Server's msg");
        // 接收的数据强转ByteBuf类型
        ByteBuf buf = (ByteBuf) msg;
        // ByteBuf转可读的Byte数组
        byte[] data = new byte[buf.readableBytes()];
        // 从可读的Byte数组中一次性读取所有数据
        buf.readBytes(data);
        // 读取到的Byte数组, 使用Utf-8转可读的String类型
        String message = new String(data, StandardCharsets.UTF_8);
        System.out.println("The received data is: " + message);
        System.out.println("Received count: " + (++count));
        super.channelRead(ctx, msg);
    }
}
