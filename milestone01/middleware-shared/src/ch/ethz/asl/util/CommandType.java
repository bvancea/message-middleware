package ch.ethz.asl.util;

public class CommandType {
	/**
	 * Send a message to a list of queues (one or more queues)
	 */
	public final static int SEND_MESSAGE = 0; 	
	
	/**
	 * query for messages in a list of queues, waiting for the particular receiver
	 */
	public final static int RECEIVE_MESSAGE_FOR_RECEIVER = 1; 
	
	/**
	 * create a new queue
	 */
	public static final int CREATE_QUEUE = 2;

	/**
	 * authenticate client
	 */
	public static final int AUTHENTICATE = 3;
	
	/**
	 * delete a specific queue
	 */
	public static final int DELETE_QUEUE = 4; 
	
	/**
	 * find a certain queue
	 */
	public static final int FIND_QUEUE = 5; 	
	
	/**
	 * read earliest message from a queue, without removing it
	 */
	public final static int READ_MESSAGE_EARLIEST = 6; 		
	
	/**
	 * pop earliest message from a queue
	 */
	public final static int RETRIEVE_MESSAGE_EARLIEST = 7; 	
	
	/**
	 * read highest priority message from a queue, without removing it
	 */
	public final static int READ_MESSAGE_PRIORITY = 8; 	
	
	/**
	 * pop highest priority message from a queue
	 */
	public final static int RETRIEVE_MESSAGE_PRIORITY = 9; 
	
}