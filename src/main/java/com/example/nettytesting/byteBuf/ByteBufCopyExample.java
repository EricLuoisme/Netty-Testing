package com.example.nettytesting.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class ByteBufCopyExample {
    public static void main(String[] args) {

        // ByteBuf提供零拷贝机制, 具体是进行拆包分离时, 内存之间的0拷贝, 与系统级别的零拷贝不一样
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeBytes(new byte[]{1, 2, 3, 4, 5});
        log(buf);
        // 浅克隆机制
        ByteBuf bb1 = buf.slice(0, 3);
        ByteBuf bb2 = buf.slice(3, 5);
        log(bb1);
        log(bb2);
        System.out.println("Change original Data");
        buf.setByte(2, 0);
        log(bb1);
        bb1.setByte(0, 9);
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
