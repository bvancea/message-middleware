package ch.ethz.asl.message;

import ch.ethz.asl.message.MessageSenderBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;


public interface MessageBroker {
	
	public MessageSenderBroker createMessageSender();

	public MessageReceiverBroker createMessageReceiver();
	
	public Queue createQueue(String name);
	
	public Queue findQueue(String name);
	
	public void deleteQueue(int id);
	
	public Message createMessage();
	
}