package com.example.nettytesting.nettyProtocol;

public enum OpCode {

    REQ((byte) 0),
    RESP((byte) 1),
    PING((byte) 2),
    PONG((byte) 3);

    private byte code;

    OpCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
