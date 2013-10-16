package ch.ethz.asl.message;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.domain.Queue;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class MessageBrokerTest {

    private static final String URL = "localhost";
    private static final int PORT = 9000;
    private static final String USERNAME = "bogdan";
    private static final String PASSWORD = "bogdan";

    private static final String TEST_QUEUE_NAME = "Awesome";

    private MessageBroker mb;

    @Before
    public void initialize() throws WrongResponseException, SendMessageException, ServerConnectionException, InvalidAuthenticationException {
        mb = MessageBrokerFactory.createMessageBroker(URL, PORT, USERNAME, PASSWORD);

        deleteQueueByName(TEST_QUEUE_NAME);
    }

    @Test
    public void testCreateQueue() throws WrongResponseException, SendMessageException, ServerConnectionException, InvalidAuthenticationException {

        Queue queue = mb.createQueue(TEST_QUEUE_NAME);
        Queue second = mb.findQueue(TEST_QUEUE_NAME);
        Assert.assertEquals(second.getId(), queue.getId());
    }

    @Test
    public void testGetQueue() throws ServerConnectionException, SendMessageException, WrongResponseException, InvalidAuthenticationException {
        Queue second = mb.findQueue(TEST_QUEUE_NAME);
        Assert.assertNotNull(second);
    }

    @Test
    public void testDeleteQueue() throws InvalidAuthenticationException, ServerConnectionException, WrongResponseException, SendMessageException {
        deleteQueueByName(TEST_QUEUE_NAME);

        Queue queue = mb.findQueue(TEST_QUEUE_NAME);
        Assert.assertNull(queue);
    }

    private void deleteQueueByName(String queueName) throws ServerConnectionException, SendMessageException, WrongResponseException, InvalidAuthenticationException {
        Queue second = mb.findQueue(queueName);
        if (second != null) mb.deleteQueue(second.getId());
    }
}
