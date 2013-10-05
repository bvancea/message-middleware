package ch.ethz.asl.message.middleware.reactor;

import ch.ethz.asl.message.domain.log.Log;
import ch.ethz.asl.message.domain.log.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Handle all request sent by one client.
 */
public class EventHandler implements Runnable {

    private Log LOG = LogFactory.getLog(EventHandler.class);

    private SocketChannel clientSocketChannel;

    private Selector eventSelector;

    private boolean connectionOpen;

    private ByteBuffer response;

    public EventHandler(final SocketChannel socketChannel) throws IOException {
        this.clientSocketChannel = socketChannel;
        this.eventSelector = Selector.open();
        this.connectionOpen = true;

        response = ByteBuffer.wrap(new String("HTTP/1.1 200 OK\n").getBytes());
    }

    @Override
    public void run() {
        try {
            registerForReadEvents();

            while (isConnectionOpen()) {
                listenForEvents();
            }
        } catch (ClosedChannelException e) {
            LOG.error("Channel to client already closed.", e);
        } catch (IOException e) {
            LOG.error("Error retrieving events.", e);
        }
    }

    private void listenForEvents() throws IOException {
        eventSelector.select();

        final Iterator<SelectionKey> events = eventSelector.selectedKeys().iterator();
        while (events.hasNext()) {

            final SelectionKey event = events.next();
            if (event.isValid()) {
                if (event.isReadable()) {
                    processReadEvent(event);
                } else if (event.isWritable()) {
                    processWriteEvent(event);
                }
            }
        }
    }

    private void processWriteEvent(SelectionKey event) throws IOException {
        final SocketChannel channel = getChannel(event);

        channel.write(response);

        LOG.info("Writing " + response + " to client " + channel.getRemoteAddress());

        registerForReadEvents();
    }

    private void processReadEvent(SelectionKey event) throws IOException {
        final SocketChannel channel = getChannel(event);

        ByteBuffer message = ByteBuffer.allocateDirect(64 * 1024);
        channel.read(message);
        message.flip();

        LOG.info("Read " + message + " from client " + channel.getRemoteAddress());

        registerForWriteEvents();
    }

    private void registerForReadEvents() throws ClosedChannelException {
        clientSocketChannel.register(eventSelector, SelectionKey.OP_READ);
    }

    private void registerForWriteEvents() throws ClosedChannelException {
        clientSocketChannel.register(eventSelector, SelectionKey.OP_WRITE);
    }

    public boolean isConnectionOpen() {
        return connectionOpen;
    }

    private SocketChannel getChannel(SelectionKey event) {
        return (SocketChannel) event.channel();
    }



}
