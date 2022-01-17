package com.example.nettytesting.eventLoop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EventLoopExample {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(2);
        // 可以发现next调用的都是下一个work线程
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        // next之后获取的就是单个线程, 可以通过submit来提交任务执行
        group.next().submit(() -> {
            System.out.println(Thread.currentThread().getName());
        });
    }
}
