package ch.ethz.asl.message.service;

import ch.ethz.asl.message.Errors;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.persistence.MapperRegistry;
import ch.ethz.asl.message.persistence.MessageMapper;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.MapKey;
import ch.ethz.asl.util.MessageUtils;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Message service.
 */
public class MessageService {

    private static final Log LOG = LogFactory.getLog(MessageService.class);

    private MessageMapper mapper = (MessageMapper) MapperRegistry.lookup(Message.class);

    public ByteBuffer addMessage(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        Message message = (Message) parameters.get(MapKey.MESSAGE);
        try {
            message = mapper.persist(message);
            responseArray = new String[] {String.valueOf(message.getId())};
        } catch (SQLException e) {
            LOG.error("Error persisting message", e);
            responseArray = new String[] {String.valueOf(Errors.DATABASE_FAILURE)};
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer receiveMessageForClientInQeueus(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);

        String[] responseArray;
        try {
            List<Long> queueIds = (List<Long>) parameters.get(MapKey.QUEUE_ID_LIST);
            List<Message> messages = mapper.receiveMessagesFromQueues(connectionId,queueIds);
            responseArray = messagesToStringArray(messages);
        } catch (SQLException e) {
            LOG.error("Error reading messages from multiple queues " + connectionId, e);
            responseArray = Errors.encodeError(Errors.DATABASE_FAILURE);
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer getMessagesForClientFromSender(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        int senderId = (Integer) parameters.get(MapKey.SENDER_ID);
        try {
            List<Message> messages = mapper.getMessagesFromSender(connectionId, senderId);
            responseArray = messagesToStringArray(messages);
        } catch (SQLException e) {
            LOG.error("Error reading messages of client " + connectionId + " from sender " + senderId, e);
            responseArray = Errors.encodeError(Errors.DATABASE_FAILURE);
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer readPriority(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        int queueId = (Integer) parameters.get(MapKey.QUEUE_ID);
        try {
            Message message = mapper.getMessage(connectionId, queueId);
            responseArray = messageToStringArray(message);
        } catch (SQLException e) {
            LOG.error("Error reading message with highest priority for client " + connectionId, e);
            responseArray = Errors.encodeError(Errors.DATABASE_FAILURE);
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer readEarliest(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        int queueId = (Integer) parameters.get(MapKey.QUEUE_ID);
        try {
            Message message = mapper.getEarliestMessage(connectionId, queueId);
            responseArray = messageToStringArray(message);
        } catch (SQLException e) {
            LOG.error("Error reading earliest message for client " + connectionId, e);
            responseArray = Errors.encodeError(Errors.DATABASE_FAILURE);
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer retrievePriority(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        int queueId = (Integer) parameters.get(MapKey.QUEUE_ID);
        try {
            Message message = mapper.retrieveMessage(connectionId, queueId);
            responseArray = messageToStringArray(message);
        } catch (SQLException e) {
            LOG.error("Error retrieving message with highest priority for client " + connectionId, e);
            responseArray = Errors.encodeError(Errors.DATABASE_FAILURE);
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer retrieveEarliest(Map<Integer, Object> parameters) {
        int connectionId = (Integer) parameters.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        int queueId = (Integer) parameters.get(MapKey.QUEUE_ID);
        try {
            Message message = mapper.retrieveEarliestMessage(connectionId, queueId);
            responseArray = messageToStringArray(message);
        } catch (SQLException e) {
            LOG.error("Error retrieving earliest message for client " + connectionId, e);
            responseArray = Errors.encodeError(Errors.DATABASE_FAILURE);
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    private String[] messageToStringArray(Message message) {
        if (message != null) {
            return new String[] {String.valueOf(message.getId()),
                    String.valueOf(message.getSender()),
                    String.valueOf(message.getReceiver()),
                    String.valueOf(message.getPriority()),
                    String.valueOf(message.getContext()),
                    MessageUtils.encodeList(message.getQueue()),
                    String.valueOf(message.getTimestamp().getTime()),
                    message.getContent()};
        } else {
            return new String[0];
        }
    }

    private String[] messagesToStringArray(List<Message> messages) {

        List<String> tokens = new LinkedList<>(); //faster than array list for appends

        //format: <message_nr>, <message_1_fields> , <message_2_fields>
        tokens.add(String.valueOf(messages.size()));
        for (Message message : messages) {
            tokens.add(String.valueOf(message.getId()));
            tokens.add(String.valueOf(message.getSender()));
            tokens.add(String.valueOf(message.getReceiver()));
            tokens.add(String.valueOf(message.getPriority()));
            tokens.add(String.valueOf(message.getContext()));
            tokens.add(MessageUtils.encodeList(message.getQueue()));
            tokens.add(String.valueOf(message.getTimestamp().getTime()));
            tokens.add(message.getContent());
        }

        return tokens.toArray(new String[messages.size() * 8 + 1]);
    }

}
