package ch.ethz.asl.message.domain;

import java.sql.Timestamp;
import java.util.List;

public class Message {

	private int id;
	private int sender;
	private int receiver;
	private java.lang.String content;
	private List<Integer> queue;
	private int priority;
	private Timestamp timestamp;
	private int context;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getReceiver() {
		return receiver;
	}
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	public int getContext() {
		return context;
	}
	public void setContext(int context) {
		this.context = context;
	}
	public java.lang.String getContent() {
		return content;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public List<Integer> getQueue() {
		return queue;
	}
	public void setQueue(List<Integer> queue) {
		this.queue = queue;
	}
	
	
}
