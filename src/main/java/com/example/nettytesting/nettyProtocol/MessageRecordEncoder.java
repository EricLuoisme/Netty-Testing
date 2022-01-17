package com.example.nettytesting.nettyProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class MessageRecordEncoder extends MessageToByteEncoder<MessageRecord> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageRecord messageRecord, ByteBuf byteBuf) throws Exception {
        Header header = messageRecord.getHeader();
        byteBuf.writeLong(header.getSessionId());
        byteBuf.writeByte(header.getReqType());

        Object body = messageRecord.getBody();
        if (null != body) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(body);
            byte[] bytes = bos.toByteArray();
            // 写body的长度
            byteBuf.writeInt(bytes.length);
            // 写body
            byteBuf.writeBytes(bytes);
        } else {
            byteBuf.writeInt(0);
        }
    }
}
