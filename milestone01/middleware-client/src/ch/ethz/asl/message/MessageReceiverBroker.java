package ch.ethz.asl.message;

import java.util.List;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.domain.Message;

public interface MessageReceiverBroker {

	/**
	 * Read the message with highest priority (without removing)
	 * @param queue
	 * @return
	 * @throws ServerConnectionException 
	 * @throws InvalidAuthenticationException 
	 * @throws WrongResponseException 
	 * @throws SendMessageException 
	 */
	public Message read(int queue) throws ServerConnectionException, InvalidAuthenticationException, 
					WrongResponseException, SendMessageException;
	
	/**
	 * Read the earliest message (without removing)
	 * @param queue
	 * @return
	 * @throws ServerConnectionException 
	 * @throws InvalidAuthenticationException 
	 */
	public Message readEarliest(int queue) throws ServerConnectionException, InvalidAuthenticationException, 
								WrongResponseException, SendMessageException;
	
	/**
	 * Retrieve the earliest message (removing)
	 * @param queue
	 * @return
	 * @throws ServerConnectionException 
	 * @throws InvalidAuthenticationException 
	 */
	public Message retrieveEarliest(int queue) 
			throws ServerConnectionException, InvalidAuthenticationException,
			WrongResponseException, SendMessageException;

	/**
	 * Retrieve the message with highest priority (removing)
	 * @param queue
	 * @return
	 * @throws ServerConnectionException 
	 * @throws InvalidAuthenticationException 
	 * @throws SendMessageException 
	 * @throws WrongResponseException 
	 */
	public Message retrieve(int queue) 
			throws ServerConnectionException, InvalidAuthenticationException, 
			SendMessageException, WrongResponseException;
	
	/**
	 * Receive messages waiting for me in the specified queues.
	 * @param queue
	 * @return
	 * @throws ServerConnectionException 
	 * @throws InvalidAuthenticationException 
	 */
	public List<Message> receiveForMe(List<Integer> queues) 
			throws ServerConnectionException, InvalidAuthenticationException,
			WrongResponseException, SendMessageException;
	
}
