package ch.ethz.asl.message;

import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.impl.MessageBrokerImpl;

public class MessageBrokerFactory {

	/**
	 * Authenticate the client using the given credentials and create a MessageBroker instance to allow the 
	 * client to perform messaging operations.
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws ServerConnectionException 
	 * @throws SendMessageException 
	 * @throws WrongResponseException 
	 */
	public static MessageBroker createMessageBroker(String url, int port, String username, String password) 
			throws WrongResponseException, SendMessageException, ServerConnectionException {
		
		return (new MessageBrokerImpl(username, password, url, port));
	}
	
}
