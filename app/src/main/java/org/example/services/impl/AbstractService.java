package org.example.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Message;

public abstract class AbstractService {
    protected final Logger logger = LogManager.getLogger(getClass().getName());
    protected Message prepareMessageConfirm(Message message){
        Message responseMessage = Message.newBuilder()
                .setOpCode(Message.OpCode.MESSAGE_CONFIRM)
                .setSeqIndex(message.getSeqIndex())
                .build();
        return responseMessage;
    }
}
