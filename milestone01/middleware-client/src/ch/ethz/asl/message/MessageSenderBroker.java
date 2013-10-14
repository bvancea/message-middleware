package ch.ethz.asl.message;

import java.util.List;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.domain.Message;

public interface MessageSenderBroker {
	
	public int send(Message message, int queue) throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException;

	public int send(Message message, List<Integer> queues) throws ServerConnectionException, InvalidAuthenticationException, WrongResponseException, SendMessageException;
}
