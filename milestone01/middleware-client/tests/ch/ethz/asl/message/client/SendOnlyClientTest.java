package ch.ethz.asl.message.client;

import ch.ethz.asl.client.Client;
import ch.ethz.asl.client.RequestResponseClient;
import ch.ethz.asl.client.SendOnlyClient;
import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.MessageBroker;
import ch.ethz.asl.message.MessageBrokerFactory;
import ch.ethz.asl.message.domain.Message;
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
		Client client = new SendOnlyClient(USERNAME, PASSWORD, URL, PORT);
		Queue queue = client.findQueue(TEST_QUEUE_NAME);
        int status = client.sendMessage(1,4,-1,"SOMETHING ELSE", queue.getId());
		Assert.assertEquals(true, status > 0);
	}


}
