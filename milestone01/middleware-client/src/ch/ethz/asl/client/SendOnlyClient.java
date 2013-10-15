package ch.ethz.asl.client;


public class SendOnlyClient extends Client {

	public SendOnlyClient(String username, String password, String url, int port) {
		super(username, password, url, port);
	}

	public SendOnlyClient(String username, String password) {
		super(username, password);
	}
	
}
