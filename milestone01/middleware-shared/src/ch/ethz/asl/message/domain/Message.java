package ch.ethz.asl.message.domain;

import java.sql.Timestamp;
import java.util.List;

public class Message {

	private int id;
	private int sender;
	private int receiver;
	private int priority;
	private int context;
    private List<Long> queue;
    private Timestamp timestamp;
    private java.lang.String content;


    public Message() { }

    public Message(int sender, int receiver, int priority, int context, List<Long> queue, Timestamp timestamp, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.priority = priority;
        this.context = context;
        this.queue = queue;
        this.timestamp = timestamp;
        this.content = content;
    }

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
	public List<Long> getQueue() {
		return queue;
	}
	public void setQueue(List<Long> queue) {
		this.queue = queue;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (context != message.context) return false;
        if (id != message.id) return false;
        if (priority != message.priority) return false;
        if (receiver != message.receiver) return false;
        if (sender != message.sender) return false;
        if (content != null ? !content.equals(message.content) : message.content != null) return false;
        if (queue != null ? !queue.equals(message.queue) : message.queue != null) return false;
        if (timestamp != null ? !timestamp.equals(message.timestamp) : message.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + sender;
        result = 31 * result + receiver;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (queue != null ? queue.hashCode() : 0);
        result = 31 * result + priority;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + context;
        return result;
    }
}
