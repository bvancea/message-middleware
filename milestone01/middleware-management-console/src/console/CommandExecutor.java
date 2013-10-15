package console;

import java.sql.SQLException;
import java.util.List;

import ch.ethz.asl.message.domain.Client;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import ch.ethz.asl.message.persistence.ClientMapper;
import ch.ethz.asl.message.persistence.MessageMapper;
import ch.ethz.asl.message.persistence.QueueMapper;

public class CommandExecutor {
	
	private QueueMapper queueMapper;
	private MessageMapper messageMapper;
	private ClientMapper clientMapper;
	
	public CommandExecutor() {
		queueMapper = new QueueMapper();
		messageMapper = new MessageMapper();
		clientMapper = new ClientMapper();
	}
	
	public void showQueues() {
		
		try {
			List<Queue> queues = queueMapper.findAll();
			for (Queue q : queues) {
				System.out.println("Queue ID: " + q.getId());
				System.out.println("Queue name: " + q.getName());
				
				Client c = clientMapper.findOne(q.getCreator());
				
				System.out.println("Created by: " + c.getUsername() + "(Id: " + c.getId() + ")");
				
				List<Message> msg = messageMapper.getMessagesFromQueue(q.getId());
				System.out.println("Messages in queue: " + msg.size());		
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void showMessages(int queueId) {
		
		try {
			List<Message> msg = messageMapper.getMessagesFromQueue(queueId);
			for(Message m : msg) {
				System.out.println("Message ID: " + m.getId());
				System.out.println("Priority: " + m.getPriority());
				System.out.println("Context: " + m.getContext());
				System.out.println("Sender: " + m.getSender());
				System.out.println("Receiver: " + m.getReceiver());
				System.out.println("Timestamp: " + m.getTimestamp());
				System.out.println("Content: " + m.getContent());
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void showAllMessages() {
		try {
			List<Message> msg = messageMapper.findAll();
			for(Message m : msg) {
				System.out.println("Message ID: " + m.getId());
				System.out.println("Priority: " + m.getPriority());
				System.out.println("Context: " + m.getContext());
				System.out.println("Sender: " + m.getSender());
				System.out.println("Receiver: " + m.getReceiver());
				System.out.println("Timestamp: " + m.getTimestamp());
				List<Long> queues = m.getQueue();
				System.out.print("In queues: ");
				for(Long l : queues) {
					System.out.print(l + " ");
				}
				System.out.println();
				System.out.println("Content: " + m.getContent());
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showClients() {
		try {
			List<Client> clients = clientMapper.findAll();
			for(Client c : clients) {
				System.out.println("Client id: " + c.getId());
				System.out.println("Client name: " + c.getName());
				System.out.println("Username: " + c.getUsername());
				System.out.println();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
