package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MessageMapper extends AbstractMapper<Message> {

    private static final Log LOG = LogFactory.getLog(MessageMapper.class);

    private static final String POP_HIGHEST_PRIORITY = "{call retrieve_message(?,?)}";
    private static final String PEEK_HIGHEST_PRIORITY = "{call get_message(?,?)}";
    private static final String POP_EARLIEST = "{call retrieve_earliest_message(?,?)}";
    private static final String PEEK_EARLIEST = "{call get_earliest_message(?,?)}";
    private static final String POP_MESSAGES_OF_CLIENT_IN_QUEUES = "{call retrieve_messages_of_client_in_queues(?,?)}";
    private static final String PEEK_MESSAGES_IN_QUEUE = "{call find_messages_from_queue(?)}";
    private static final String PEEK_MESSAGE_FROM_SENDER = "{call get_messages_sender(?,?)}";

    @Override
    protected java.lang.String persistStatement() {
        return "{call add_message(?, ?, ?, ?, ?, ?, ?)}";
    }

    @Override
    protected java.lang.String findStatement() {
        return "{call find_message(?)}";
    }

    @Override
    protected java.lang.String findAllStatement() {
        return "{call get_messages()}";
    }

    @Override
    protected java.lang.String deleteStatement() {
        return "{call delete_message(?)}";
    }

    @Override
    public Map<Object, Integer> toDatabaseParams(Message entity) throws SQLException {
        Map<Object, Integer> map = new LinkedHashMap<>();
        map.put(entity.getId(), Types.BIGINT);
        map.put(entity.getSender(), Types.BIGINT);
        map.put(entity.getReceiver(), Types.BIGINT);
        map.put(getDataSource().getConnection().createArrayOf("bigint", entity.getQueue().toArray()), Types.ARRAY);
        map.put(entity.getPriority(), Types.BIGINT);
        map.put(entity.getContext(), Types.BIGINT);
        map.put(entity.getTimestamp(), Types.TIME);
        map.put(entity.getContent(), Types.VARCHAR);
        return map;
    }

    @Override
    public Message persist(Message entity) throws SQLException {
        CallableStatement statement = getDataSource().getConnection().prepareCall(persistStatement());
        statement.setLong(1, entity.getSender());
        statement.setLong(2, entity.getReceiver());
        statement.setArray(3, getDataSource().getConnection().createArrayOf("bigint", entity.getQueue().toArray()));
        statement.setLong(4, entity.getPriority());
        statement.setLong(5, entity.getContext());
        statement.setTimestamp(6, entity.getTimestamp());
        statement.setString(7, entity.getContent());
        return load(statement.executeQuery());
    }

    public Message retrieveEarliestMessage(int clientId, int queueId) throws SQLException {
        String message = "Error retrieving earliest message for client " + clientId + " from queue " + queueId;
        return getMessageUsingProcedure(clientId, queueId, POP_EARLIEST, message);
    }

    public Message getEarliestMessage(int clientId, int queueId) throws SQLException {
        String message = "Error reading earliest message for client " + clientId + " from queue " + queueId;
        return getMessageUsingProcedure(clientId, queueId, PEEK_EARLIEST, message);
    }

    public Message retrieveMessage(int clientId, int queueId) throws SQLException {
        String message = "Error retrieving highest priority message for client " + clientId + " from queue " + queueId;
        return getMessageUsingProcedure(clientId, queueId, POP_HIGHEST_PRIORITY, message);
    }

    public Message getMessage(int clientId, int queueId) throws SQLException {
        String message = "Error reading highest priority message for client " + clientId + " from queue " + queueId;
        return getMessageUsingProcedure(clientId, queueId, PEEK_HIGHEST_PRIORITY, message);
    }

    private Message getMessageUsingProcedure(int clientId, int queueId, String storedProcedure, String erorrMessage) throws SQLException {
        Message message = null;

        final Connection connection = getConnection();
        CallableStatement statement = connection.prepareCall(storedProcedure);
        statement.setLong(1, clientId);
        statement.setLong(2, queueId);
        message = load(statement.executeQuery());

        connection.close();
        return message;
    }

    public List<Message> getMessagesFromQueue(int queueId) throws SQLException {
        final Connection connection = getConnection();
        CallableStatement statement = connection.prepareCall(PEEK_MESSAGES_IN_QUEUE);
        statement.setInt(1,queueId);

        List<Message> returnedList = loadAll(statement.executeQuery());
        connection.close();
        return returnedList;
    }

    public List<Message> getMessagesFromSender(int receiverId, int senderId) throws SQLException {
        Connection connection = null;
        List<Message> returnedMessages = null;
        try {
            connection = getConnection();
            CallableStatement statement = connection.prepareCall(PEEK_MESSAGE_FROM_SENDER);
            statement.setInt(1, receiverId);
            statement.setInt(2, senderId);

            returnedMessages = loadAll(statement.executeQuery());
        } catch (SQLException e) {
            LOG.error("Database failure. Aborting.", e);
        } finally {
            nullSafeCloseConnection(connection);
        }
        return returnedMessages;
    }

    public List<Message> receiveMessagesFromQueues(int receiverId, List<Long> queueIds) throws SQLException {
        Connection connection = null;
        List<Message> returnedMessages = null;
        try {
            connection = getConnection();
            CallableStatement statement = connection.prepareCall(POP_MESSAGES_OF_CLIENT_IN_QUEUES);
            statement.setInt(1, receiverId);
            statement.setArray(2, connection.createArrayOf("bigint", queueIds.toArray()));

            returnedMessages = loadAll(statement.executeQuery());
        } catch (SQLException e) {
            LOG.error("Database failure. Aborting.", e);
        } finally {
            nullSafeCloseConnection(connection);
        }
        return returnedMessages;
    }


    @Override
    public Message loadOne(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt(1));
        message.setSender(rs.getInt(2));
        message.setReceiver(rs.getInt(3));
        message.setQueue(Arrays.asList((Long[]) rs.getArray(4).getArray()));
        message.setPriority(rs.getInt(5));
        message.setContext(rs.getInt(8));
        message.setTimestamp(rs.getTimestamp(6));
        message.setContent(rs.getString(7));
        return message;
    }

    private void nullSafeCloseConnection(Connection connection) throws SQLException {
        if (connection != null) connection.close();
    }
}
