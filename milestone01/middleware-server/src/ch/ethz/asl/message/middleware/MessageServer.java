package ch.ethz.asl.message.middleware;

import java.util.Map;

public class MessageServer extends Thread {

	/**
	 * Map of active connections. 
	 */
	private Map<String, Connection> connections;
	
	public MessageServer() { }	
	
	/**
	 * Create 
	 * @param connection
	 */
	private void createWorkerForConnection(Connection connection) {
		ConnectionWorker worker = new ConnectionWorker();
	}

}
