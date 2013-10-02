package ch.ethz.asl.message.middleware;

import java.util.List;

import ch.ethz.asl.message.domain.Queue;
import ch.ethz.asl.message.persistence.ClientMapper;
import ch.ethz.asl.message.persistence.MessageMapper;
import ch.ethz.asl.message.persistence.QueueMapper;

public class ConnectionWorker extends Thread {

	private ClientMapper clientMapper;
	private MessageMapper messageMapper;
	private QueueMapper queueMapper;
	
	public void createQueue(Queue queue) {
		queueMapper.persist(queue);
	}
	
	public void deleteQueue(int queueId) {
		queueMapper.delete(queueId);
	}
	
	public List<Queue> getAllQueues() {
		return queueMapper.findAll();
	}

}
