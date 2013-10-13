package ch.ethz.asl.message.domain;

public class Queue {
	
	private int id;
	private String name;
	private Client creator;
	
	public Queue(String name, Client creator) {
		this.name = name;
		this.creator = creator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Client getCreator() {
		return creator;
	}

	public void setCreator(Client creator) {
		this.creator = creator;
	}
	
	
	
	
}
