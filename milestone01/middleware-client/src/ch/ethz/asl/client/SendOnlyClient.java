package ch.ethz.asl.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SendOnlyClient extends Client {

	private String username;
	private String password;
	
	public SendOnlyClient() throws IOException, ExecutionException,
			InterruptedException {
		super();
		// TODO Auto-generated constructor stub
	}

	public SendOnlyClient(String username, String password) throws IOException,
			ExecutionException, InterruptedException {
		super();
		this.username = username;
		this.password = password;
	}
	
	
	
	

}
