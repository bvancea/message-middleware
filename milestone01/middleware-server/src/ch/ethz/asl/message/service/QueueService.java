package ch.ethz.asl.message.service;

import ch.ethz.asl.message.Errors;
import ch.ethz.asl.message.domain.Queue;
import ch.ethz.asl.message.persistence.MapperRegistry;
import ch.ethz.asl.message.persistence.QueueMapper;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.MapKey;
import ch.ethz.asl.util.MessageUtils;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Map;

/**
 * Queue service
 */
public class QueueService {

    private static final Log LOG = LogFactory.getLog(QueueService.class);

    QueueMapper mapper = (QueueMapper) MapperRegistry.lookup(Queue.class);

    public ByteBuffer createQueue(Map<Integer, Object> params) {

        String name = (String) params.get(MapKey.QUEUE_NAME);
        int connectionId = (Integer) params.get(MapKey.CONNECTION_ID);
        String[] responseArray;

        try {
            Queue queue = new Queue(name,connectionId);
            queue = mapper.persist(queue);
            responseArray = queueToResponseArray(queue);
        } catch (SQLException e) {
            LOG.error("Error persisting queue",e);
            responseArray = new String[] {String.valueOf(Errors.DATABASE_FAILURE)};
        }

        String responseString = MessageUtils.encodeMessage(CommandType.CREATE_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer deleteQueue(Map<Integer, Object> params) {
        int connectionId = (Integer) params.get(MapKey.CONNECTION_ID);
        String[] responseArray;
        int queueId =(Integer) params.get(MapKey.QUEUE_ID);

        try {
            mapper.delete(queueId);
            responseArray = new String[] { String.valueOf(queueId)};
        } catch (SQLException e) {
            LOG.error("Error deleting queue");
            responseArray = new String[] {String.valueOf(Errors.DATABASE_FAILURE) };
        }

        String responseString = MessageUtils.encodeMessage(CommandType.DELETE_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    public ByteBuffer findQueue(Map<Integer, Object> params) {
        int connectionId = (Integer) params.get(MapKey.CONNECTION_ID);
        String[] responseArray;
        String name = (String) params.get(MapKey.QUEUE_NAME);

        try {
            Queue queue = mapper.findByName(name);
            responseArray = queueToResponseArray(queue);
        } catch (SQLException e) {
            LOG.error("Error retrieving queue.",e);
            responseArray = new String[] {String.valueOf(Errors.DATABASE_FAILURE)};
        }

        String responseString = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, responseArray, connectionId);
        return ByteBuffer.wrap(responseString.getBytes());
    }

    private String[] queueToResponseArray(Queue queue) {
        if (queue != null) {
            return new String[] {String.valueOf(queue.getId()), queue.getName(), String.valueOf(queue.getId())};
        } else {
            return new String[0];
        }
    }
}
