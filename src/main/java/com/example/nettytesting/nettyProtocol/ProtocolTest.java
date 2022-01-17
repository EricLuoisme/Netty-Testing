package com.example.nettytesting.nettyProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

public class ProtocolTest {

    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel(
                // 添加解码器才能解决粘包问题 (8 + 1 = Header长度), (4 = Length长度)
                new LengthFieldBasedFrameDecoder(1024 * 1024,
                        8 + 1, 4, 0, 0),
                new LoggingHandler(),
                new MessageRecordEncoder(),
                new MessageRecordDecoder());

        Header h = new Header();
        h.setSessionId(123456);
        h.setReqType(OpCode.REQ.getCode());

        MessageRecord mr = new MessageRecord();
        mr.setHeader(h);
        mr.setBody("Hello Protocol");

        // 编码后, 写到Channel
        channel.writeOutbound(mr);

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageRecordEncoder().encode(null, mr, buf);
        // 解码后, 读取消息
        channel.writeInbound(buf);
    }

}
