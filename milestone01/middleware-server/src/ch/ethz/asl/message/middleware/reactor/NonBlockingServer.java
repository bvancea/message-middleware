package ch.ethz.asl.message.middleware.reactor;

import ch.ethz.asl.message.domain.log.Log;
import ch.ethz.asl.message.domain.log.LogFactory;

import java.io.IOException;

/**
 *
 */
public class NonBlockingServer {

    private static final int SERVER_PORT = 9000;
    private static final int POOL_SIZE   = 10;

    private static final Log LOG = LogFactory.getLog(NonBlockingServer.class);

    public static void main(String[] args) throws IOException {
        try {
            EventDispatcher dispatcher = new EventDispatcher(SERVER_PORT, POOL_SIZE);
            final Thread dispatcherThread = new Thread(dispatcher);
            dispatcherThread.start();

            Thread.sleep(5 * 60 * 1000);
            dispatcherThread.join();
        } catch (InterruptedException e) {
            LOG.error("Fatal error. Shutting down the server.");
        }
    }
}
