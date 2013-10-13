package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Client;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;

public class MapperRegistry {

    private static ClientMapper clientMapper;
    private static MessageMapper messageMapper;
    private static QueueMapper queueMapper;

    public static AbstractMapper lookup(Class clazz) {

        if (Client.class.equals(clazz)) {
            return getClientMapper();
        } else if (Message.class.equals(clazz)) {
            return getMessageMapper();
        } else if (Queue.class.equals(clazz)) {
            return getQueueMapper();
        } else {
            return null;
        }
    }

    private static ClientMapper getClientMapper() {
        if (clientMapper == null) {
            clientMapper = new ClientMapper();
        }
        return clientMapper;
    }

    private static QueueMapper getQueueMapper() {
        if (queueMapper == null) {
            queueMapper = new QueueMapper();
        }
        return queueMapper;
    }

    private static MessageMapper getMessageMapper() {
        if (messageMapper == null) {
            messageMapper = new MessageMapper();
        }
        return messageMapper;
    }



}
