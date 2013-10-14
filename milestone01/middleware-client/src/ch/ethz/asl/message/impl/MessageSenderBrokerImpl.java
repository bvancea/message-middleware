package ch.ethz.asl.message.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.MessageSenderBroker;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.Communicator;
import ch.ethz.asl.util.MessageUtils;

public class MessageSenderBrokerImpl implements MessageSenderBroker {
	
	private Log log = LogFactory.getLog(getClass());
	private String username;
	private String password;
	private Communicator communicator = null;
	private int connectionID = -1;

	public MessageSenderBrokerImpl(String username, String password,
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
	public int send(Message message, int queueId) throws ServerConnectionException, 
			InvalidAuthenticationException, WrongResponseException, SendMessageException {
		
		int status = -1;
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[6];
			params[0] = Integer.toString(message.getPriority());
			params[1] = Integer.toString(message.getSender());
			params[2] = Integer.toString(message.getReceiver());
			params[3] = Integer.toString(message.getContext());
			params[4] = "[" + queueId + "]";
			params[5] = message.getContent();
			
			String sendMsg = MessageUtils.encodeMessage(CommandType.SEND_MESSAGE, params, connectionID);
			try {
				String received = communicator.sendMessage(sendMsg);
				status = (Integer)MessageUtils.decodeResponseMessage(CommandType.SEND_MESSAGE, received);
			} catch (ExecutionException | InterruptedException e) {
				log.error("Communicator failed to send message.", e);
				throw new SendMessageException();
			}
		}
		
		return status;
	}

	@Override
	public int send(Message message, List<Integer> queues) throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException {
		int status = -1;
		if (communicator == null) {
			throw new ServerConnectionException();
		} else if (connectionID == -1) {
			throw new InvalidAuthenticationException();
		} else {
			String[] params = new String[6];
			params[0] = Integer.toString(message.getPriority());
			params[1] = Integer.toString(message.getSender());
			params[2] = Integer.toString(message.getReceiver());
			params[3] = Integer.toString(message.getContext());
			params[4] = MessageUtils.encodeList(queues);
			params[5] = message.getContent();
			
			String sendMsg = MessageUtils.encodeMessage(CommandType.SEND_MESSAGE, params, connectionID);
			try {
				String received = communicator.sendMessage(sendMsg);
				status = (Integer)MessageUtils.decodeResponseMessage(CommandType.SEND_MESSAGE, received);
			} catch (ExecutionException | InterruptedException e) {
				log.error("Communicator failed to send message.", e);
				throw new SendMessageException();
			}
		}
		
		return status;

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
