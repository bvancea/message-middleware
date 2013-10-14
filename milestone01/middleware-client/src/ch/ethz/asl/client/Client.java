package ch.ethz.asl.client;

import java.util.List;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.MessageBroker;
import ch.ethz.asl.message.MessageBrokerFactory;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

public abstract class Client {  
	
	protected Log log = LogFactory.getLog(getClass());
	protected MessageBroker messageBroker;
	protected String username;
	protected String password;
	protected String url = "localhost";
	protected int port = 9000;
	
	public Client() {
		
	}
	
	public Client(String username, String password) {
		this.username = username;
		this.password = password;
		
		try {
			messageBroker = MessageBrokerFactory.createMessageBroker(url, port, username, password);
		} catch (WrongResponseException e) {
			log.error("Authentication failed", e);
		} catch (SendMessageException e) {
			log.error("Could not send authentication command to server", e);
		} catch (ServerConnectionException e) {
			log.error("Communication with server failed or does not exist.", e);
		}
	}
	
	public Client(String username, String password, String url, int port) {
		this.username = username;
		this.password = password;
		this.url = url;
		this.port = port;
		
		try {
			messageBroker = MessageBrokerFactory.createMessageBroker(url, port, username, password);
		} catch (WrongResponseException e) {
			log.error("Authentication failed", e);
		} catch (SendMessageException e) {
			log.error("Could not send authentication command to server", e);
		} catch (ServerConnectionException e) {
			log.error("Communication with server failed or does not exist.", e);
		}
	}
	

	public void sendMessage(Message message, Queue queue) throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}
	
	public void sendMultipleQueueMessage(Message message, List<Queue> queue) throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}
	
	public void retrieveMessage(Queue queue) throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}
	
	public void readMessage (Queue queue) throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}
	
	public void createQueue(String queueName) {	
		Queue queue = null;
		try {
			queue = messageBroker.createQueue(queueName);
		} catch (InvalidAuthenticationException e) {
			log.error("Client not authenticated", e);
		} catch (ServerConnectionException e) {
			log.error("No connection to server", e);
		} catch (WrongResponseException e) {
			log.error("Received wrong response from server.", e);
		} catch (SendMessageException e) {
			log.error("Could not send command to server.", e);
		} 
		if (queue == null) {
			log.error("Queue " + queueName + " was not created.");
		} else {
			log.info("Queue " + queue.getName() + " was successfully created.");
		}
		
	}
	
	public int deleteQueue(Queue queue) {
		int status = -1;
		try {
			status = messageBroker.deleteQueue(queue.getId());
			if (status == -1) {
				log.error("Queue " + queue.getName() + " was not deleted.");
			} else {
				log.info("Queue " + queue.getName() + " was successfully deleted.");
			}
		} catch (InvalidAuthenticationException e) {
			log.error("Client not authenticated", e);
		} catch (ServerConnectionException e) {
			log.error("No connection to server", e);
		} catch (WrongResponseException e) {
			log.error("Received wrong response from server.", e);
		} catch (SendMessageException e) {
			log.error("Could not send command to server.", e);
		} 
		
		return status;
	}
	
	public Queue findQueue(String queueName) {
		Queue queue = null;
		try {
			queue = messageBroker.findQueue(queueName);
		} catch (InvalidAuthenticationException e) {
			log.error("Client not authenticated", e);
		} catch (ServerConnectionException e) {
			log.error("No connection to server", e);
		} catch (WrongResponseException e) {
			log.error("Received wrong response from server.", e);
		} catch (SendMessageException e) {
			log.error("Could not send command to server.", e);
		} 
		
		if (queue == null) {
			log.error("Queue " + queueName + " was not found.");
		} else {
			log.info("Queue " + queue.getName() + " was returned successfully.");
		}
		
		return queue;
	}
	
	
}


