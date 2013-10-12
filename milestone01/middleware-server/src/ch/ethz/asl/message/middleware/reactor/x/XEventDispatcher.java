package ch.ethz.asl.message.middleware.reactor.x;

import ch.ethz.asl.message.middleware.pool.WorkerPool;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 *
 */
public class XEventDispatcher implements Runnable {

    private static final Log LOG = LogFactory.getLog(XEventDispatcher.class);

    private boolean isStopped;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private final WorkerPool pool;

    public XEventDispatcher(int port, int threadPoolSize ) throws IOException {
        pool = new WorkerPool(threadPoolSize);

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

        startWorkers();

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

    private void startWorkers() {
        pool.star();
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

    /**
     * Accept a new connection and return a Socket Channel corresponding to the connection.
     * @param event
     * @return
     * @throws IOException
     */
    private SocketChannel acceptConnection(final SelectionKey event) throws IOException {
        final ServerSocketChannel currentServerSocketChannel = (ServerSocketChannel) event.channel();
        return currentServerSocketChannel.accept();
    }

    /**
     * Pick one of the workers and assign the new client to it.
     * @param socketChannel                 The channel corresponding to the new connection
     * @throws IOException                  In case the connection cannot be accepted
     */
    private void dispatchConnection(final SocketChannel socketChannel) throws IOException {
        pool.getWorker().acceptConnection(socketChannel);
    }

}
