package ch.ethz.asl.client;

import java.util.List;

import ch.ethz.asl.exceptions.InvalidAuthenticationException;
import ch.ethz.asl.exceptions.SendMessageException;
import ch.ethz.asl.exceptions.ServerConnectionException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.MessageReceiverBroker;
import ch.ethz.asl.message.domain.Message;

public class RequestResponseClient extends Client {
	
	private MessageReceiverBroker messageReceiverBroker;

	public RequestResponseClient(String username, String password) {
		super(username, password);
	}

	public RequestResponseClient(String username, String password, String url,
			int port) {
		super(username, password, url, port);
	}
	
	@Override
	public Message retrievePriorityMessage(int queue) {
		
		Message message = null;
		
		if (messageBroker == null) {
			log.error("There is no broker connecting to server");
		} else {
			if (messageReceiverBroker == null) {
				messageReceiverBroker = messageBroker.createMessageReceiver();
			}
			
			try {
				message = messageReceiverBroker.retrieve(queue);
				
			} catch (ServerConnectionException e) {
				log.error("No connection to server", e);
			} catch (InvalidAuthenticationException e) {
				log.error("Client not authenticated", e);
			} catch (WrongResponseException e) {
				log.error("Received wrong response from server.", e);
			} catch (SendMessageException e) {
				log.error("Could not send command to server.", e);
			}
		}
		
		return message;
		
	}
	
	@Override
	public Message retrieveEarliestMessage(int queue){
		
		Message message = null;
		
		if (messageBroker == null) {
			log.error("There is no broker connecting to server");
		} else {
			if (messageReceiverBroker == null) {
				messageReceiverBroker = messageBroker.createMessageReceiver();
			}
			
			try {
				message = messageReceiverBroker.retrieveEarliest(queue);
				
			} catch (ServerConnectionException e) {
				log.error("No connection to server", e);
			} catch (InvalidAuthenticationException e) {
				log.error("Client not authenticated", e);
			} catch (WrongResponseException e) {
				log.error("Received wrong response from server.", e);
			} catch (SendMessageException e) {
				log.error("Could not send command to server.", e);
			}
		}
		
		return message;
	}
	
	@Override
	public Message readPriorityMessage (int queue) {
		
		Message message = null;
		
		if (messageBroker == null) {
			log.error("There is no broker connecting to server");
		} else {
			if (messageReceiverBroker == null) {
				messageReceiverBroker = messageBroker.createMessageReceiver();
			}
			
			try {
				message = messageReceiverBroker.read(queue);
				
			} catch (ServerConnectionException e) {
				log.error("No connection to server", e);
			} catch (InvalidAuthenticationException e) {
				log.error("Client not authenticated", e);
			} catch (WrongResponseException e) {
				log.error("Received wrong response from server.", e);
			} catch (SendMessageException e) {
				log.error("Could not send command to server.", e);
			}
		}
		
		return message;
	}
	
	@Override
	public Message readEarliestMessage (int queue) {
		
		Message message = null;
		
		if (messageBroker == null) {
			log.error("There is no broker connecting to server");
		} else {
			if (messageReceiverBroker == null) {
				messageReceiverBroker = messageBroker.createMessageReceiver();
			}
			
			try {
				message = messageReceiverBroker.readEarliest(queue);
				
			} catch (ServerConnectionException e) {
				log.error("No connection to server", e);
			} catch (InvalidAuthenticationException e) {
				log.error("Client not authenticated", e);
			} catch (WrongResponseException e) {
				log.error("Received wrong response from server.", e);
			} catch (SendMessageException e) {
				log.error("Could not send command to server.", e);
			}
		}
		
		return message;
	}
	
	@Override
	public List<Message> retrieveMyMessages (List<Integer> queues) throws UnsupportedOperationException {
		
		List<Message> messages = null;
		
		if (messageBroker == null) {
			log.error("There is no broker connecting to server");
		} else {
			if (messageReceiverBroker == null) {
				messageReceiverBroker = messageBroker.createMessageReceiver();
			}
			
			try {
				messages = messageReceiverBroker.receiveForMe(queues);
				
			} catch (ServerConnectionException e) {
				log.error("No connection to server", e);
			} catch (InvalidAuthenticationException e) {
				log.error("Client not authenticated", e);
			} catch (WrongResponseException e) {
				log.error("Received wrong response from server.", e);
			} catch (SendMessageException e) {
				log.error("Could not send command to server.", e);
			}
		}
		
		return messages;
	}
	
	@Override
	public List<Message> readFromSender (int senderId) throws UnsupportedOperationException {

        List<Message> messages = null;

        if (messageBroker == null) {
            log.error("There is no broker connecting to server");
        } else {
            if (messageReceiverBroker == null) {
                messageReceiverBroker = messageBroker.createMessageReceiver();
            }

            try {
                messages = messageReceiverBroker.readFromSender(senderId);

            } catch (ServerConnectionException e) {
                log.error("No connection to server", e);
            } catch (InvalidAuthenticationException e) {
                log.error("Client not authenticated", e);
            } catch (WrongResponseException e) {
                log.error("Received wrong response from server.", e);
            } catch (SendMessageException e) {
                log.error("Could not send command to server.", e);
            }
        }

        return messages;
	}

}
