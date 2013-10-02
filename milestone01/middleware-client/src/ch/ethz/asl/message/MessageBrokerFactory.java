package ch.ethz.asl.message;

public interface MessageBrokerFactory {

	/**
	 * Authenticate the client using the given credentials and create a MessageBroker instance to allow the 
	 * client to perform messaging operations.
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public MessageBroker createMessageBroker(String url, int port, String username, String password);
	
}
