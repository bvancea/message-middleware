package ch.ethz.asl.middleware;

import org.junit.Test;

import ch.ethz.asl.message.middleware.ThreadPooledServer;

public class ThreadPooledServerTest {

	@Test
	public void TestServer() {
		
		ThreadPooledServer server = new ThreadPooledServer(9000);
		new Thread(server).start();
		
		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Stopping server");
		server.stop();
	}
}
