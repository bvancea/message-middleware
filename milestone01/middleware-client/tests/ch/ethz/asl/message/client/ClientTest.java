package ch.ethz.asl.message.client;

import org.junit.Test;

import ch.ethz.asl.client.Client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ClientTest {

    final static int ITERATIONS = 2;
    @Test
    public void testOneConnection() throws IOException, ExecutionException, InterruptedException {

        Client client = new Client();
        for (int i = 0; i < ITERATIONS; i++) {
            client.sendMessage("Someething something dark side");
        }
    }
}
