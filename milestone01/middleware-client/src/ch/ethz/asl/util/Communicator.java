package ch.ethz.asl.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class Communicator {
	
	AsynchronousSocketChannel channel;

    public Communicator(String url, int port) throws IOException, ExecutionException, InterruptedException {
        channel = AsynchronousSocketChannel.open();
        channel.connect(new InetSocketAddress(url, port)).get();
    }

    public String sendMessage(String message) throws ExecutionException, InterruptedException {

        ByteBuffer sendMessage = ByteBuffer.wrap(message.getBytes());
        ByteBuffer receivingMessage = ByteBuffer.allocateDirect(1024);
        String returnMessage = null;
        
        channel.write(sendMessage).get();
        Integer status = channel.read(receivingMessage).get();
        if (status > 0) {
        	returnMessage = Charset.defaultCharset().decode(receivingMessage).toString();
        }      
       
        return returnMessage;

    }
}
