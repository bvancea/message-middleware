package ch.ethz.asl.message.service;

import ch.ethz.asl.exceptions.EmptyMessageException;
import ch.ethz.asl.exceptions.UnknownRequestFormatException;
import ch.ethz.asl.message.Errors;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.MapKey;
import ch.ethz.asl.util.MessageUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Does the actual work for the even handler.
 */
public class EventHandlerHelper {

    private static final Log LOG = LogFactory.getLog(EventHandlerHelper.class);

    private ClientService clientService;

    private QueueService queueService;

    private MessageService messageService;

    public EventHandlerHelper() {
        clientService = new ClientService();
        queueService = new QueueService();
        messageService = new MessageService();
    }

    public ByteBuffer processClientRequest(ByteBuffer messageFromClient) {
        ByteBuffer returnBuffer = defaultErrorMessage();

        String bufferContent = Charset.defaultCharset().decode(messageFromClient).toString();
        try {
            final Map<Integer, Object> requestParams = MessageUtils.decodeRequestMessage(bufferContent);
            if (requestParams.size() > 1) {
                final Integer command = (Integer) requestParams.get(MapKey.COMMAND_TYPE);
                switch (command) {
                    case CommandType.AUTHENTICATE:
                        returnBuffer = clientService.authenticate(requestParams);
                        break;
                    case CommandType.CREATE_QUEUE:
                        returnBuffer = queueService.createQueue(requestParams);
                        break;
                    case CommandType.FIND_QUEUE:
                        returnBuffer = queueService.findQueue(requestParams);
                        break;
                    case CommandType.DELETE_QUEUE:
                        returnBuffer = queueService.deleteQueue(requestParams);
                        break;
                    case CommandType.SEND_MESSAGE:
                        returnBuffer = messageService.addMessage(requestParams);
                        break;
                    case CommandType.RECEIVE_MESSAGES_FOR_RECEIVER_FROM_QUEUES:
                        returnBuffer = messageService.receiveMessageForClientInQeueus(requestParams);
                        break;
                    case CommandType.READ_MESSAGE_EARLIEST:
                        returnBuffer = messageService.readEarliest(requestParams);
                        break;
                    case CommandType.READ_MESSAGE_PRIORITY:
                        returnBuffer = messageService.readPriority(requestParams);
                        break;
                    case CommandType.RETRIEVE_MESSAGE_EARLIEST:
                        returnBuffer = messageService.retrieveEarliest(requestParams);
                        break;
                    case CommandType.RETRIEVE_MESSAGE_PRIORITY:
                        returnBuffer = messageService.retrievePriority(requestParams);
                        break;
                    case CommandType.READ_FROM_SENDER:
                        returnBuffer = messageService.getMessagesForClientFromSender(requestParams);
                        break;
                    default:
                        break;

                }
            }

        } catch (UnknownRequestFormatException e) {
            LOG.error("The client request had a wrong format.");
        } catch (EmptyMessageException e) {
            LOG.error("The client request was empty");
        } catch (Exception e) {
            LOG.error("Anything else", e);
        }
        //ToDo add handling of invalid request message
        return returnBuffer;
    }

    private ByteBuffer defaultErrorMessage() {
        return ByteBuffer.wrap( ( String.valueOf(Errors.DATABASE_FAILURE) + String.valueOf(Errors.DATABASE_FAILURE)).getBytes());
    }
}
