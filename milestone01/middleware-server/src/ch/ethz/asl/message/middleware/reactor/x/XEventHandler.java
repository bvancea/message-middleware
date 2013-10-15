package ch.ethz.asl.message.middleware.reactor.x;

import ch.ethz.asl.message.service.EventHandlerHelper;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Handle all request sent by one client.
 */
public class XEventHandler extends Thread {

    private Log LOG = LogFactory.getLog(XEventHandler.class);

    private Selector eventSelector;

    private EventHandlerHelper helper;

    private Map<SocketChannel, ByteBuffer> connections = new HashMap<>();

    private boolean connectionOpen;

    private  ByteBuffer message = ByteBuffer.allocateDirect(512);

    public XEventHandler() throws IOException {
        this.eventSelector = Selector.open();
        this.helper = new EventHandlerHelper();
        this.connectionOpen = true;
    }

    public void acceptConnection(SocketChannel channel) throws ClosedChannelException {
        connections.put(channel, null);
        registerForReadEvents(channel);
    }

    @Override
    public void run() {
        LOG.info("Starting worker");
        try {

            while (isConnectionOpen()) {
                listenForEvents();
            }

        } catch (ClosedChannelException e) {
            LOG.error("Channel to client already closed.", e);
        } catch (IOException e) {
            LOG.error("Error retrieving events.", e);
        }
        LOG.info("Stopping worker");
    }

    private void listenForEvents() throws IOException {
        eventSelector.selectNow();

        final Iterator<SelectionKey> events = eventSelector.selectedKeys().iterator();

        while (events.hasNext()) {
            final SelectionKey event = events.next();
            events.remove();
            if (event.isValid()) {
                if (event.isReadable()) {
                    processReadEvent(event);
                } else if (event.isWritable()) {
                    processWriteEvent(event);
                }
            }
        }
    }

    private synchronized void processReadEvent(SelectionKey event) throws IOException {
        final SocketChannel channel = getChannel(event);

        //read messages
        try {
            channel.read(message);

            message.flip();
            LOG.info("Read " + message + " from client " + channel.getRemoteAddress());

            //process request and computer answer
            ByteBuffer reply = processClientRequest(message);
            connections.put(channel, reply);
            message.clear();

            //register to write the response
            registerForWriteEvents(channel);
        } catch (IOException e) {
            LOG.error("There was an error reading the request from " + channel.getRemoteAddress());

            //ToDo maybe we want to try to send an error message instead of closing the channel
            channel.close();
            connections.remove(channel);
        }

    }

    private void processWriteEvent(SelectionKey event) throws IOException {
        final SocketChannel channel = getChannel(event);

        try {
            //get the previously computed reply and write to the client
            ByteBuffer reply = connections.get(channel);
            channel.write(reply);
            LOG.info("Wrote " + reply.flip().toString() + " to client " + channel.getRemoteAddress());

            //register for read again, we might accept something from this client again
            registerForReadEvents(channel);
        } catch (IOException e) {
            LOG.error("Error writing response back to client. Closing channel.");
            channel.close();
            connections.remove(channel);
        }
    }

    private ByteBuffer processClientRequest(ByteBuffer messageFromClient) {
        return helper.processClientRequest(messageFromClient);
    }

    private void registerForReadEvents(final SocketChannel channel) throws ClosedChannelException {
        channel.register(eventSelector.wakeup(), SelectionKey.OP_READ);
    }

    private void registerForWriteEvents(final SocketChannel channel) throws ClosedChannelException {
        channel.register(eventSelector.wakeup(), SelectionKey.OP_WRITE);
    }

    public boolean isConnectionOpen() {
        return connectionOpen;
    }

    private SocketChannel getChannel(SelectionKey event) {
        return (SocketChannel) event.channel();
    }



}
