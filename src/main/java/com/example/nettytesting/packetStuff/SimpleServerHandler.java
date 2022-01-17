package com.example.nettytesting.packetStuff;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] req = new byte[in.readableBytes()];
        in.readBytes(req);
        // 服务端接收数据进行输出
        System.out.println("Server received: " + new String(req, StandardCharsets.UTF_8));
        // 服务端立刻组装返回数据
        ByteBuf resp = Unpooled.copiedBuffer(("Server receive message successfully:" + UUID.randomUUID() + "&")
                .getBytes(StandardCharsets.UTF_8));
        // 写入ByteBuf后, flush发送 (没有Override写完的ChannelReadComplete, 这里可以直接writeAndFlush)
        ctx.writeAndFlush(resp);
    }

}
