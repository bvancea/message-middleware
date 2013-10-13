package ch.ethz.asl.message;

import java.util.List;

import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import exception.InvalidAuthenticationException;
import exception.SendMessageException;
import exception.ServerConnectionException;
import exception.WrongResponseException;

public interface MessageSenderBroker {
	
	public int send(Message message, Queue queue) throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException;

	public int send(Message message, List<Queue> queues) throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException;
}
