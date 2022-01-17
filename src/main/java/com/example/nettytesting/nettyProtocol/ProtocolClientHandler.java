package com.example.nettytesting.nettyProtocol;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolClientHandler extends ChannelInboundHandlerAdapter {

    // 接收Server响应数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRecord record = (MessageRecord) msg;
        log.info("Client Receive Msg:" + record);
        super.channelRead(ctx, msg);
    }
}
