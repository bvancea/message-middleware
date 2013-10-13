package ch.ethz.asl.message;

import ch.ethz.asl.message.MessageSenderBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import exception.InvalidAuthenticationException;
import exception.SendMessageException;
import exception.ServerConnectionException;
import exception.WrongResponseException;


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