package ch.ethz.asl.message.client;


import ch.ethz.asl.client.Client;
import ch.ethz.asl.client.RequestResponseClient;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

public class RequestResponseClientTest {

    private static final String URL = "localhost";
    private static final int PORT = 9000;
    private static final String USERNAME = "bogdan";
    private static final String PASSWORD = "bogdan";

    private static final String TEST_QUEUE_NAME = "Awesome";

    @Test
    public void retrieveMessageTest() throws Exception {

        Client client = new RequestResponseClient("sandi", "cuta", URL, PORT);
        Queue queue = client.findQueue(TEST_QUEUE_NAME);
        Message message = client.retrievePriorityMessage(2);
        Assert.assertNotNull(message);
    }

    @Test
    public void messageFromQueues() {

        Client client = new RequestResponseClient("sandi", "cuta", URL, PORT);
        List<Message> messageList = client.readFromSender(5);

        Assert.assertNotNull(messageList);
    }


}
