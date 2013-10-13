package ch.ethz.asl.message.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import ch.ethz.asl.message.MessageBroker;
import ch.ethz.asl.message.MessageReceiverBroker;
import ch.ethz.asl.message.MessageSenderBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.Communicator;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.MessageUtils;
import exception.InvalidAuthenticationException;
import exception.SendMessageException;
import exception.ServerConnectionException;
import exception.WrongResponseException;


public class MessageBrokerImpl implements MessageBroker {
	
	private Log log = LogFactory.getLog(getClass());
	private String username;
	private String password;
	private String connectionURL;
	private int connectionPort;
	private Communicator communicator = null;
	private int connectionID = -1;
	
	/*
	 * Constructor
	 */
	
	public MessageBrokerImpl(String username, String password, String connectionURL, int connectionPort) 
			throws WrongResponseException, SendMessageException, ServerConnectionException {
		super();
		this.username = username;
		this.password = password;
		this.connectionURL = connectionURL;
		this.connectionPort = connectionPort;
		
		try {
			this.communicator = new Communicator(connectionURL, connectionPort);
			connectionID = authenticateClient();
		} catch (IOException | ExecutionException | InterruptedException e) {
			log.error("Communicator could not connect to server.", e);
			throw new ServerConnectionException();
		}
	}
	
	/*
	 * Methods
	 */

	@Override
	public MessageSenderBroker createMessageSender() {
		MessageSenderBroker msb = new MessageSenderBrokerImpl(username, password, communicator, connectionID);
		return msb;
	}

	@Override
	public MessageReceiverBroker createMessageReceiver() {
		MessageReceiverBroker mrb = new MessageReceiverBrokerImpl(username, password, communicator, connectionID);
		return mrb;
	}

	@Override
	public Queue createQueue(String name) throws InvalidAuthenticationException, 
					ServerConnectionException, WrongResponseException, SendMessageException {
		if (communicator != null) {
			if (connectionID != -1) {
				String[] params = new String[2];
				params[0] = name;
				params[1] = username;
				String message = MessageUtils.encodeMessage(CommandType.CREATE_QUEUE, params, connectionID);
				try {
					String response = communicator.sendMessage(message);
					Queue queue = (Queue)MessageUtils.decodeMessage(CommandType.CREATE_QUEUE, response);
					return queue;
				} catch (ExecutionException | InterruptedException e) {
					log.error("Exception when sending create queue command.", e);
					throw new SendMessageException();
				} 
			} else {
				throw new InvalidAuthenticationException();
			}
		} else {
			throw new ServerConnectionException();
		}
		
	}

	@Override
	public Queue findQueue(String name) throws InvalidAuthenticationException, ServerConnectionException, 
								WrongResponseException, SendMessageException {
		if (communicator != null) {
			if (connectionID != -1) {
				String[] params = new String[1];
				params[0] = name;
				String message = MessageUtils.encodeMessage(CommandType.FIND_QUEUE, params, connectionID);
				try {
					String response = communicator.sendMessage(message);
					Queue queue = (Queue)MessageUtils.decodeMessage(CommandType.FIND_QUEUE, response);
					return queue;
				} catch (ExecutionException | InterruptedException e) {
					log.error("Exception when sending find queue command.", e);
					throw new SendMessageException();
				}
			} else {
				throw new InvalidAuthenticationException();
			}
		} else {
			throw new ServerConnectionException();
		}
		
	}

	@Override
	public int deleteQueue(int id) throws InvalidAuthenticationException, ServerConnectionException, 
					WrongResponseException, SendMessageException {
		if (communicator != null) {
			if (connectionID != -1) {
				String[] params = new String[1];
				params[0] = Integer.toString(id);
				String message = MessageUtils.encodeMessage(CommandType.DELETE_QUEUE, params, connectionID);
				try {
					String response = communicator.sendMessage(message);
					int status = (Integer)MessageUtils.decodeMessage(CommandType.DELETE_QUEUE, response);
					return status;
				} catch (ExecutionException | InterruptedException e) {
					log.error("Exception when sending delete queue command.", e);
					throw new SendMessageException();
				}
			} else {
				throw new InvalidAuthenticationException();
			}
		} else {
			throw new ServerConnectionException();
		}
	}

	@Override
	public Message createMessage(int priority, int receiver, int context, String content) {
		
		Message message = new Message();
		message.setSender(connectionID);
		message.setPriority(priority);
		message.setReceiver(receiver);
		message.setContext(context);
		message.setContent(content);
		
		return message;
		
	}
	
	@Override
	public int authenticateClient() throws ServerConnectionException, WrongResponseException, SendMessageException {
		if (communicator != null) {
			String[] params = new String[2];
			params[0] = username;
			params[1] = password;
			String message = MessageUtils.encodeMessage(CommandType.AUTHENTICATE, params, -2);
			try {
				String received = communicator.sendMessage(message);
				int id = (Integer)MessageUtils.decodeMessage(CommandType.AUTHENTICATE, received);
				return id;
			} catch (ExecutionException | InterruptedException e) {
				log.error("Could not send authentication message", e);
				throw new SendMessageException();
			}
		} else {
			throw new ServerConnectionException();
		}
		
	}

	/* 
	 * Getters and setters
	 */
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public int getConnectionPort() {
		return connectionPort;
	}

	public void setConnectionPort(int connectionPort) {
		this.connectionPort = connectionPort;
	}
	
}
