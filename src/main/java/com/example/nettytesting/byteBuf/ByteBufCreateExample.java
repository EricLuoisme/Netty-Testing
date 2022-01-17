package com.example.nettytesting.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufCreateExample {
    public static void main(String[] args) {
        // 由JVM管理的堆内存
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        // 堆外内存 (读写内存更快, 减少了用户内存到内核内存的copy)
        ByteBufAllocator.DEFAULT.directBuffer();

        // ByteBuf池化技术, PooledUnsafeHeapByteBuf(ridx: 0, widx: 0, cap: 256)
        System.out.println(byteBuf);
    }
}
