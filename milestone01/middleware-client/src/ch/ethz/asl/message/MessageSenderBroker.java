package ch.ethz.asl.message;

import java.util.List;

import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;

public interface MessageSenderBroker {
	
	public void send(Message message, Queue queue);

	public void send(Message message, List<Queue> queues);
}
