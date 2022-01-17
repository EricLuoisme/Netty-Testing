package com.example.nettytesting.nettyProtocol;

import lombok.Data;

/**
 * 总共13字节的header
 */
@Data
public class Header {

    // 会话id, 8字节
    private long sessionId;

    // 消息类型, 1字节
    private byte reqType;

    // 消息体长度, 4字节
    private int length;
}
