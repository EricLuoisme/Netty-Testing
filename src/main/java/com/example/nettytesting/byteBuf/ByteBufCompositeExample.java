package com.example.nettytesting.byteBuf;

import io.netty.buffer.*;

public class ByteBufCompositeExample {
    public static void main(String[] args) {
        ByteBuf header = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf body = ByteBufAllocator.DEFAULT.buffer();

        // 组合
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        // 1. 通过使用某个ByteBuf的扩展, 来达到组合效果
        compositeByteBuf.addComponents(true, header, body);

        // 2. 可以直接调用命令
        ByteBuf composite = Unpooled.wrappedBuffer(header, body);
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
