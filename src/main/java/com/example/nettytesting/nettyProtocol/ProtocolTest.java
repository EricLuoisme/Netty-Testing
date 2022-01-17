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
        System.out.println("======Encode======");
        channel.writeOutbound(mr);
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageRecordEncoder().encode(null, mr, buf);
        // 解码后, 拆包, 读取消息
        System.out.println("======Decode======");
        ByteBuf slice_1 = buf.slice(0, 7);
        ByteBuf slice_2 = buf.slice(7, buf.readableBytes() - 7);
        // 由于浅复制, 并且不在JVM中管理, 所以引用计数器也是用的同一个
        // 当别的地方的调用释放后, 需要调用retain, 将引用+1, 维持可用性
        slice_1.retain();

        channel.writeInbound(slice_1);
        channel.writeInbound(slice_2);
    }

}
