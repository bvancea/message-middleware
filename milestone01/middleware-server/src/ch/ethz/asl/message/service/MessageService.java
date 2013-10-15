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
import java.util.Map;

/**
 * Message service.
 */
public class MessageService {

    private static final Log LOG = LogFactory.getLog(MessageService.class);

    private MessageMapper mapper = (MessageMapper) MapperRegistry.lookup(Message.class);

    public ByteBuffer addMessage(Map<Integer, Object> parameters) {
        int connectionId = Integer.parseInt((String) parameters.get(MapKey.CONNECTION_ID));
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

    public ByteBuffer receiveMessageForClient(Map<Integer, Object> parameters) {
        int connectionId = Integer.parseInt((String) parameters.get(MapKey.CONNECTION_ID));

        //ToDo add stored procedure
        String[] responseArray = new String[0];


        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer readPriority(Map<Integer, Object> parameters) {
        int connectionId = Integer.parseInt((String) parameters.get(MapKey.CONNECTION_ID));
        String[] responseArray;

        int queueId = Integer.parseInt(String.valueOf(parameters.get(MapKey.QUEUE_ID)));
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
        int connectionId = Integer.parseInt((String) parameters.get(MapKey.CONNECTION_ID));
        String[] responseArray;

        int queueId = Integer.parseInt(String.valueOf(parameters.get(MapKey.QUEUE_ID)));
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
        int connectionId = Integer.parseInt((String) parameters.get(MapKey.CONNECTION_ID));
        String[] responseArray;

        int queueId = Integer.parseInt(String.valueOf(parameters.get(MapKey.QUEUE_ID)));
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
        int connectionId = Integer.parseInt((String) parameters.get(MapKey.CONNECTION_ID));
        String[] responseArray;

        int queueId = Integer.parseInt(String.valueOf(parameters.get(MapKey.QUEUE_ID)));
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
        return new String[] {String.valueOf(message.getId()),
                String.valueOf(message.getSender()),
                String.valueOf(message.getReceiver()),
                String.valueOf(message.getPriority()),
                String.valueOf(message.getContext()),
                MessageUtils.encodeList(message.getQueue()),
                String.valueOf(message.getTimestamp().getTime()),
                message.getContent()};
    }

}
