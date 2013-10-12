package ch.ethz.asl.message.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class Client {

    AsynchronousSocketChannel channel;

    public Client() throws IOException, ExecutionException, InterruptedException {
        channel = AsynchronousSocketChannel.open();
        channel.connect(new InetSocketAddress(9000)).get();
    }

    public void sentMessage(String message) throws ExecutionException, InterruptedException {

        ByteBuffer sendMessage = ByteBuffer.wrap(message.getBytes());
        ByteBuffer receivingMessage = ByteBuffer.allocateDirect(1024);

        channel.write(sendMessage).get();
        channel.read(receivingMessage, receivingMessage, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                String msgReceived = Charset.defaultCharset().decode(attachment).toString();
                System.out.println("Msg received from server : " + msgReceived);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("Something went wrong");
            }
        });

        Thread.sleep(5000);

    }
}
