package ch.ethz.asl.message.client;

import ch.ethz.asl.client.Client;
import ch.ethz.asl.client.SendOnlyClient;
import ch.ethz.asl.message.MessageBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import ch.ethz.asl.message.impl.MessageBrokerImpl;
import org.junit.Test;

public class SendOnlyClientTest {

	@Test
	public void sendMessageTest() throws Exception {
		String username = "sandi";
		String password = "cuta";
		Client client = new SendOnlyClient(username, password);
		Queue queue = client.findQueue("what");
		client.sendMessage(10, -1, 1, "Hello. This is a message", queue.getId());
	}
}
