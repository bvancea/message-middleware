package ch.ethz.asl.message.domain;

public class Queue {
	
	private int id;
	private String name;
	private int creator;

	public Queue(String name, int creator) {
		this.name = name;
		this.creator = creator;
	}

    public Queue(int id, String name, int creator) {
        this.id = id;
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

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}
	
	
	
	
}
