package ch.ethz.asl.message.middleware.reactor;

import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class EventDispatcher implements Runnable {

    private static final Log LOG = LogFactory.getLog(EventDispatcher.class);

    private boolean isStopped;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private final ExecutorService handlerPool;

    public EventDispatcher(int port, int threadPoolSize ) throws IOException {
        handlerPool = Executors.newFixedThreadPool(threadPoolSize);

        initSelector(port);
    }

    /**
     * Initialize a server socket channel on the server port and register it to the selector
     * @throws IOException          In case the selector or the socket channel cannot be opened.
     */
    private void initSelector(int port) throws IOException {

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        LOG.info("Event dispatcher initialized. Listening on port: " + port);
    }

    @Override
    public void run() {

        while (!isStopped()) {
            try {
                while (selector.select() > 0) {
                    Iterator<SelectionKey> events = selector.selectedKeys().iterator();
                    while(events.hasNext()) {
                        final SelectionKey event = events.next();
                        events.remove();
                        dispatchConnection(event);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading keys");
            }
        }
        while (true);
    }

    public boolean isStopped() {
        return isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.selector.close();
            this.serverSocketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void dispatchConnection(final SelectionKey event) throws IOException {
        if (event.isValid()) {
            if (event.isAcceptable()) {
                acceptAndHandleConnection(event);
            }
        }
    }

    private void acceptAndHandleConnection(final SelectionKey event) throws IOException {
        final SocketChannel clientSocketChannel = acceptConnection(event);
        clientSocketChannel.configureBlocking(false);
        dispatchConnection(clientSocketChannel);
    }

    private SocketChannel acceptConnection(final SelectionKey event) throws IOException {
        final ServerSocketChannel currentServerSocketChannel = (ServerSocketChannel) event.channel();
        return currentServerSocketChannel.accept();
    }

    private void dispatchConnection(final SocketChannel socketChannel) throws IOException {
        this.handlerPool.execute(new EventHandler(socketChannel));
    }

}