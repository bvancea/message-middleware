package ch.ethz.asl.message;

import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;

public interface MessageReceiverBroker {

	/**
	 * Retrieve the message with highest priority
	 * @param queue
	 * @return
	 */
	public Message receive(Queue queue);
	
	/**
	 * Retrieve the earliest message.
	 * @param queue
	 * @return
	 */
	public Message receiveEarliest(Queue queue);
	
}
