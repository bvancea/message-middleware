package ch.ethz.asl.message;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;


public interface MessageBroker {
	
	public MessageSenderBroker createMessageSender();

	public MessageReceiverBroker createMessageReceiver();
	
	public Queue createQueue(String name) throws InvalidAuthenticationException, ServerConnectionException, 
											WrongResponseException, SendMessageException;
	
	public Queue findQueue(String name) throws InvalidAuthenticationException, ServerConnectionException,
										WrongResponseException, SendMessageException;
	
	public int deleteQueue(int id) throws InvalidAuthenticationException, ServerConnectionException,
										WrongResponseException, SendMessageException;
	
	public Message createMessage(int priority, int receiver, int context, String content);
	
	public int authenticateClient() throws ServerConnectionException, WrongResponseException, SendMessageException;
}