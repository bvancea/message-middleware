package ch.ethz.asl.message.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.MessageReceiverBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.Communicator;
import ch.ethz.asl.util.MessageUtils;

public class MessageReceiverBrokerImpl implements MessageReceiverBroker {
	
	private Log log = LogFactory.getLog(getClass());
	private String username;
	private String password;
	private Communicator communicator = null;
	private int connectionID = -1;

	public MessageReceiverBrokerImpl(String username, String password,
			Communicator communicator, int connectionID) {
		this.username = username;
		this.password = password;
		this.communicator = communicator;
		this.connectionID = connectionID;
	}

	/*
	* Methods
	*/
	
	@Override
	public Message retrieve(int queue) 
			throws ServerConnectionException, InvalidAuthenticationException, 
			SendMessageException, WrongResponseException {
		
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[1];
			params[0] = Integer.toString(queue);
			String sendCommand = MessageUtils.encodeMessage(CommandType.RETRIEVE_MESSAGE_PRIORITY, 
					params, connectionID);
			try {
				String receivedMessage = communicator.sendMessage(sendCommand);
				Message message = (Message) MessageUtils.decodeResponseMessage(CommandType.RETRIEVE_MESSAGE_PRIORITY, receivedMessage);
				return message;
			} catch (ExecutionException | InterruptedException e) {
				log.error("Could not send retrieve message command.", e);
				throw new SendMessageException();
			}
			
		}
		
	}

	@Override
	public Message read(int queue) throws ServerConnectionException,
			InvalidAuthenticationException, WrongResponseException, SendMessageException {
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[1];
			params[0] = Integer.toString(queue);
			String sendCommand = MessageUtils.encodeMessage(CommandType.READ_MESSAGE_PRIORITY, 
					params, connectionID);
			try {
				String receivedMessage = communicator.sendMessage(sendCommand);
				Message message = (Message) MessageUtils.decodeResponseMessage(CommandType.READ_MESSAGE_PRIORITY, receivedMessage);
				return message;
			} catch (ExecutionException | InterruptedException e) {
				log.error("Could not send retrieve message command.", e);
				throw new SendMessageException();
			}
			
		}
	}

	@Override
	public Message readEarliest(int queue) throws ServerConnectionException,
			InvalidAuthenticationException, WrongResponseException, SendMessageException {
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[1];
			params[0] = Integer.toString(queue);
			String sendCommand = MessageUtils.encodeMessage(CommandType.READ_MESSAGE_EARLIEST, 
					params, connectionID);
			try {
				String receivedMessage = communicator.sendMessage(sendCommand);
				Message message = (Message) MessageUtils.decodeResponseMessage(CommandType.READ_MESSAGE_EARLIEST, receivedMessage);
				return message;
			} catch (ExecutionException | InterruptedException e) {
				log.error("Could not send retrieve message command.", e);
				throw new SendMessageException();
			}
			
		}
	}

	@Override
	public Message retrieveEarliest(int queue)
			throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException {
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[1];
			params[0] = Integer.toString(queue);
			String sendCommand = MessageUtils.encodeMessage(CommandType.RETRIEVE_MESSAGE_EARLIEST, 
					params, connectionID);
			try {
				String receivedMessage = communicator.sendMessage(sendCommand);
				Message message = (Message) MessageUtils.decodeResponseMessage(CommandType.RETRIEVE_MESSAGE_EARLIEST, receivedMessage);
				return message;
			} catch (ExecutionException | InterruptedException e) {
				log.error("Could not send retrieve message command.", e);
				throw new SendMessageException();
			}
			
		}
	}

	@Override
	public List<Message> receiveForMe(List<Integer> queues)
			throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException {
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[1];
			params[0] = MessageUtils.encodeList(queues);
			String sendCommand = MessageUtils.encodeMessage(CommandType.RECEIVE_MESSAGE_FOR_RECEIVER, 
					params, connectionID);
			try {
				String receivedMessage = communicator.sendMessage(sendCommand);
				List<Message> messages = (List<Message>) MessageUtils.decodeResponseMessage(CommandType.RECEIVE_MESSAGE_FOR_RECEIVER, receivedMessage);
				return messages;
			} catch (ExecutionException | InterruptedException e) {
				log.error("Could not send retrieve message command.", e);
				throw new SendMessageException();
			}
			
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

	public Communicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	
	

}
