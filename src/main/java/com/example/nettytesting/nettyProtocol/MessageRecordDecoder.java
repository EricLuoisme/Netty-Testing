package com.example.nettytesting.nettyProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class MessageRecordDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessageRecord record = new MessageRecord();
        // 获取header内容
        Header header = new Header();
        header.setSessionId(byteBuf.readLong());
        header.setReqType(byteBuf.readByte());
        header.setLength(byteBuf.readInt());
        record.setHeader(header);
        // 获取消息体
        if (header.getLength() > 0) {
            // 1. 根据长度, 从ByteBuf中读取剩余的Content
            byte[] contents = new byte[header.getLength()];
            byteBuf.readBytes(contents);
            // 2. 进行反序列化, 组装为可读内容 (使用Java原生对象流)
            ByteArrayInputStream bis = new ByteArrayInputStream(contents);
            ObjectInputStream ios = new ObjectInputStream(bis);
            record.setBody(ios.readObject());
            System.out.println("De-serialized:" + record);
            // 3. 添加对象
            list.add(record);
        } else {
            System.out.println("Empty Content");
        }
    }
}
