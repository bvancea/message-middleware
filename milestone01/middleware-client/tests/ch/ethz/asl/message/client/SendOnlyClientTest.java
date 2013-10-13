package ch.ethz.asl.message.client;

import org.junit.Test;

import ch.ethz.asl.message.MessageBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;

public class SendOnlyClientTest {

	@Test
	public void sendMessageTest() {
		String username = "sandi";
		String password = "cuta";
		Client client = new SendOnlyClient(username, password);
		Message message = new Message();
		MessageBroker mb = new MessageBrokerImpl();
		Queue queue = mb.findQueue("what");
		client.sendMessage(message, queue);
	}
}
