package ch.ethz.asl.persistence;

import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.persistence.MapperRegistry;
import ch.ethz.asl.message.persistence.MessageMapper;
import junit.framework.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Tests for message operations.
 */
public class MessageMapperTest {

    private static MessageMapper messageMapper = (MessageMapper) MapperRegistry.lookup(Message.class);

    private static final int TEST_SENDER_ID = 1;
    private static final int TEST_RECEIVER_ID = 4;
    private static final List<Long> TEST_QUEUE_IDS = new ArrayList<Long>() { { add(1L); add(2L); add(3L); add(4L); add(5L); add(6L); }};
    private static final int TEST_PRIORITY = 4;
    private static final int TEST_CONTEXT = 1;
    private static final Timestamp TEST_TIMESTAMP = new Timestamp(System.currentTimeMillis());
    private static final String TEST_CONTENT = "Test message to be saved in the database";

    @Test
    public void addMessageTest() throws SQLException {

        Message message = new Message(TEST_SENDER_ID, TEST_RECEIVER_ID, TEST_PRIORITY, TEST_CONTEXT, TEST_QUEUE_IDS, TEST_TIMESTAMP, TEST_CONTENT);
        Message returned = messageMapper.persist(message);
        message.setId(returned.getId());
        Assert.assertEquals(message, returned);

        //cleanup
        deleteMessage(returned.getId());
    }

    @Test
    public void testDeleteMessage() throws SQLException {
        Message message = new Message(TEST_SENDER_ID, TEST_RECEIVER_ID, TEST_PRIORITY, TEST_CONTEXT, TEST_QUEUE_IDS, TEST_TIMESTAMP, TEST_CONTENT);
        Message returned = messageMapper.persist(message);

        int returnedId = returned.getId();
        deleteMessage(returnedId);
        message = messageMapper.findOne(returnedId);
        Assert.assertEquals(null, message);
    }

    @Test
    public void testMessagesFromQueue() throws SQLException {
        List<Message> messages = messageMapper.getMessagesFromQueue(1);
        System.out.println(messages);
        Assert.assertNotNull(messages);
    }

    private void deleteMessage(int id) throws SQLException {
        messageMapper.delete(id);
    }
}
