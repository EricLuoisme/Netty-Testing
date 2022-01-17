package com.example.nettytesting.nettyProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 此时应该是Channel就绪事件
        MessageRecord record = (MessageRecord) msg;
        log.info("Server Receive Msg:" + record);
        // 组装返回并flush
        record.setBody("Server Received Client's Msg");
        record.getHeader().setReqType(OpCode.RESP.getCode());
        ctx.writeAndFlush(record);
    }
}
