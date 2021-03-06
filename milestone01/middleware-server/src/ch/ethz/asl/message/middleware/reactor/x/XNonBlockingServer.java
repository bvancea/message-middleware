package ch.ethz.asl.message.middleware.reactor.x;

import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

import java.io.IOException;

/**
 *
 */
public class XNonBlockingServer {

    private static final int SERVER_PORT = 9000;
    private static final int POOL_SIZE   = 1;

    private static final Log LOG = LogFactory.getLog(XNonBlockingServer.class);

    public static void main(String[] args) throws IOException {
        try {
            XEventDispatcher dispatcher = new XEventDispatcher(SERVER_PORT, POOL_SIZE);
            final Thread dispatcherThread = new Thread(dispatcher);
            dispatcherThread.start();

            Thread.sleep(5 * 60 * 1000);
            dispatcherThread.join();
        } catch (InterruptedException e) {
            LOG.error("Fatal error. Shutting down the server.");
        }
    }
}
