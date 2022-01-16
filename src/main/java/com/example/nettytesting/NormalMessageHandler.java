package com.example.nettytesting;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class NormalMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] req = new byte[in.readableBytes()];
        in.readBytes(req);
        // 服务端接收数据进行输出
        System.out.println("Server received: " + new String(req, StandardCharsets.UTF_8));
        // 服务端立刻组装返回数据
        ByteBuf resp = Unpooled.copiedBuffer(("Server receive message successfully")
                .getBytes(StandardCharsets.UTF_8));
        ctx.write(resp);
    }

    /**
     * 当响应数据写完到buffer, 就会调用这里
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 当响应数据写完到buffer, flush发送回去, 并且添加一个关闭事件的listener
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
