package ch.ethz.asl.message.client;

import ch.ethz.asl.client.Client;
import ch.ethz.asl.client.SendOnlyClient;
import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.MessageBroker;
import ch.ethz.asl.message.MessageBrokerFactory;
import ch.ethz.asl.message.domain.Queue;
import junit.framework.Assert;
import org.junit.Test;

public class SendOnlyClientTest {

    private static final String URL = "localhost";
    private static final int PORT = 9000;
    private static final String USERNAME = "bogdan";
    private static final String PASSWORD = "bogdan";

    private static final String TEST_QUEUE_NAME = "Awesome";

	@Test
	public void sendMessageTest() throws Exception {
		String username = "sandi";
		String password = "cuta";
		Client client = new SendOnlyClient(username, password);
		Queue queue = client.findQueue("what");
		client.sendMessage(10, -1, 1, "Hello. This is a message", queue.getId());
	}

    @Test
    public void misc() throws WrongResponseException, SendMessageException, ServerConnectionException, InvalidAuthenticationException {

        MessageBroker mb = MessageBrokerFactory.createMessageBroker(URL, PORT, USERNAME, PASSWORD);

        Queue queue = mb.createQueue(TEST_QUEUE_NAME);
        Queue second = mb.findQueue(TEST_QUEUE_NAME);
        Assert.assertEquals(second, queue);

    }

    @Test
    public void testGetQueue() throws ServerConnectionException, SendMessageException, WrongResponseException, InvalidAuthenticationException {
        MessageBroker mb = MessageBrokerFactory.createMessageBroker(URL, PORT, USERNAME, PASSWORD);
        Queue second = mb.findQueue(TEST_QUEUE_NAME);
        Assert.assertNotNull(second);
    }
}
