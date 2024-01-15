package org.example.webSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.ErrorMessage;
import org.example.Message;
import org.example.services.ClanService;
import org.example.services.UserService;
import org.example.services.impl.ClanServiceImpl;
import org.example.services.impl.UserServiceImpl;

public class MessageProcessor {
    private String UNKNOWN_OPCODE_ERROR_MESSAGE = "Unknown opcode";

    private final Logger logger = LogManager.getLogger(getClass().getName());
    private final ClanService clanService = new ClanServiceImpl();
    private final UserService userService = new UserServiceImpl();

    public Message process(Message message){
        Message responseMessage;
        try {
        switch (message.getOpCode()){
            case CREATE_CLAN:
                responseMessage = clanService.createClan(message);
                break;
            case ADD_MONEY_TO_CLAN:
               responseMessage = clanService.addMoneyToClan(message);
               break;
            case TAKE_MONEY_FROM_CLAN:
                responseMessage = clanService.takeMoneyFromClan(message);
                break;
            case CREATE_USER:
                responseMessage = userService.createUser(message);
                break;
            default:
                responseMessage = Message.newBuilder()
                        .setOpCode(Message.OpCode.ERROR)
                        .setErrorMessage(ErrorMessage.newBuilder()
                                .setSeqIndex(message.getSeqIndex())
                                .setErrorMessage(UNKNOWN_OPCODE_ERROR_MESSAGE)
                                .build())
                        .build();
        }
        } catch (RuntimeException e){
            logger.error("Error on processing message", e);
            responseMessage = Message.newBuilder()
                    .setOpCode(Message.OpCode.ERROR)
                    .setErrorMessage(ErrorMessage.newBuilder()
                            .setSeqIndex(message.getSeqIndex())
                            .setErrorMessage(e.getMessage())
                            .build())
                    .build();
        }
        return responseMessage;
    }
}
