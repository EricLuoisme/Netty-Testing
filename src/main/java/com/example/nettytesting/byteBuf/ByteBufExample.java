package com.example.nettytesting.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class ByteBufExample {
    public static void main(String[] args) {
        // 构建一个ByteBuf
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1; i++) {
            sb.append("-" + i);
        }
        buf.writeBytes(sb.toString().getBytes());
        System.out.println("============");
        log(buf);

        // 测试读取字节
        buf.writeInt(5);
        log(buf);
        // 记录当前reader的index
        buf.markReaderIndex();
        byte b = buf.readByte();
        System.out.println(b);
        log(buf);
        // 重置reader的index到mark的位置, 从而实现重复读取内容
        buf.resetReaderIndex();
        byte b2 = buf.readByte();
        System.out.println(b2);
        buf.writeInt(1);
        log(buf);
    }

    private static void log(ByteBuf buf) {
        StringBuilder sb = new StringBuilder();
        sb.append(" read index:").append(buf.readerIndex());
        sb.append(" write index:").append(buf.writerIndex());
        sb.append(" capacity:").append(buf.capacity());
        // ByteBuf用于可读化整理的工具
        ByteBufUtil.appendPrettyHexDump(sb, buf);
        System.out.println(sb);
    }
}
