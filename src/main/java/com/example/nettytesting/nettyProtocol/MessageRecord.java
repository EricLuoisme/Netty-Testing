package com.example.nettytesting.nettyProtocol;

import lombok.Data;

@Data
public class MessageRecord {
    private Header header;
    private Object body;
}
