package ch.ethz.asl.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ethz.asl.exceptions.EmptyMessageException;
import ch.ethz.asl.exceptions.UnknownRequestFormatException;
import ch.ethz.asl.exceptions.WrongResponseException;
import ch.ethz.asl.message.Errors;
import ch.ethz.asl.message.domain.Message;
import ch.ethz.asl.message.domain.Queue;

public class MessageUtils {
	
	public static String encodeMessage(int type, String[] params, int id) {
		
		String encoded = Integer.toString(type);
		String idString = Integer.toString(id);
		encoded += "," + idString;
		for(int i = 0; i < params.length; i++) {
			encoded += "," + params[i];
		}
		
		return encoded;
	}
	
	public static Object decodeResponseMessage(int type, String message) throws WrongResponseException {
        String[] tokens = message.split(",");
        Object response = null;

        //awesome programming
        if (Errors.DATABASE_FAILURE == Integer.parseInt(tokens[1])) throw new RuntimeException("Database Error");

        switch(type) {
            case CommandType.AUTHENTICATE:
                if (tokens.length < 2) throw new WrongResponseException();
                response = Integer.parseInt(tokens[1]);
                break;
            case CommandType.CREATE_QUEUE:
                if (tokens.length < 5 ) throw new WrongResponseException();
                response = new Queue(Integer.parseInt(tokens[2]), tokens[3], Integer.parseInt(tokens[4]));
                break;
            case CommandType.DELETE_QUEUE:
                if (tokens.length < 2) throw new WrongResponseException();
                response = Integer.parseInt(tokens[1]);
                break;
            case CommandType.FIND_QUEUE:
                if (tokens.length < 5 ) throw new WrongResponseException();
                response = new Queue(Integer.parseInt(tokens[2]), tokens[3], Integer.parseInt(tokens[4]));
                break;
            case CommandType.READ_MESSAGE_EARLIEST:

                response = new Message(
                        Integer.parseInt(tokens[2]),    //sender
                        Integer.parseInt(tokens[3]),    //receiver
                        tokens[8],                      //content
                        decodeList(tokens[6]),          //queues
                        Integer.parseInt(tokens[4]),    //priority
                        new Timestamp(Long.parseLong(tokens[7])), //timestamp
                        Integer.parseInt(tokens[5])     //context
                );
                break;
            case CommandType.READ_MESSAGE_PRIORITY:

                break;
            case CommandType.RECEIVE_MESSAGE_FOR_RECEIVER:

                break;
            case CommandType.RETRIEVE_MESSAGE_EARLIEST:

                break;
            case CommandType.RETRIEVE_MESSAGE_PRIORITY:

                break;
            case CommandType.SEND_MESSAGE: break;
            default: throw new WrongResponseException();
		}
		
		return response;
	}
	
	public static Map<Integer, Object> decodeRequestMessage(String message) throws UnknownRequestFormatException, EmptyMessageException {
		
		try {
			if (message == null || message.equals("")) throw new EmptyMessageException();
			Map<Integer, Object> returnMap = new HashMap<Integer, Object>();
			String[] tokens;
			tokens = message.split(",");
			
			if (tokens.length < 2) throw new UnknownRequestFormatException();
			
			Integer commandType = Integer.parseInt(tokens[0]);
			returnMap.put(MapKey.COMMAND_TYPE, commandType);
			
			Integer connectionId = Integer.parseInt(tokens[1]);
			returnMap.put(MapKey.CONNECTION_ID, connectionId);
			
			switch(commandType) {
			case CommandType.AUTHENTICATE: 
				//format: command_type,connectionID,username,password
				if (tokens.length < 4) {
					throw new UnknownRequestFormatException();
				} else {				
					String username = tokens[2];
					String password = tokens[3];				
					
					returnMap.put(MapKey.USERNAME, username);
					returnMap.put(MapKey.PASSWORD, password);
									
					return returnMap;
				}
			case CommandType.CREATE_QUEUE: 
				//format: command_type,connectionID,queue_name
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {
					String queueName = tokens[2];		
					returnMap.put(MapKey.QUEUE_NAME, queueName);
					
					return returnMap;
				}
			case CommandType.DELETE_QUEUE: 
				//format: command_type,connectionID,queue_id
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {
					Integer queueId = Integer.parseInt(tokens[2]);		
					returnMap.put(MapKey.QUEUE_ID, queueId);
					
					return returnMap;
				}
			case CommandType.FIND_QUEUE: 
				//format: command_type,connectionID,queue_name
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {			
					String queueName = tokens[2];		
					returnMap.put(MapKey.QUEUE_NAME, queueName);
					
					return returnMap;
				}
			case CommandType.READ_MESSAGE_EARLIEST:
				//format: command_type,connectionID,queue_id
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {
					returnMap.putAll(decodeReceiveOneMessage(tokens));	
					
					return returnMap;
				}
			case CommandType.READ_MESSAGE_PRIORITY: 
				//format: command_type,connectionID,queue_id
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {
					returnMap.putAll(decodeReceiveOneMessage(tokens));	
					
					return returnMap;
				}
			case CommandType.RECEIVE_MESSAGE_FOR_RECEIVER: 
				//format: command_type,connectionID,[queue_id1 queue_id2 ...]				
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {				
					String queueList = tokens[2].substring(1, tokens[2].length() - 1);
					String[] queues = queueList.split(" ");
					List<Integer> qList = new ArrayList<Integer>();
					for(String q : queues) {
						qList.add(Integer.parseInt(q));
					}
					
					returnMap.put(MapKey.QUEUE_ID_LIST, qList);
					
					return returnMap;
				}
			case CommandType.RETRIEVE_MESSAGE_EARLIEST: 
				//format: command_type,connectionID,queue_id
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {
					returnMap.putAll(decodeReceiveOneMessage(tokens));	
					
					return returnMap;
				}
			case CommandType.RETRIEVE_MESSAGE_PRIORITY: 
				//format: command_type,connectionID,queue_id
				if (tokens.length < 3) {
					throw new UnknownRequestFormatException();
				} else {
					returnMap.putAll(decodeReceiveOneMessage(tokens));	
					
					return returnMap;
				}
			case CommandType.SEND_MESSAGE: 
				//format: command_type,connectionID,msgPriority,
				//		  senderId,receiverId,contextId,[qId1 qId2 ...],content
				if (tokens.length < 8) {
					throw new UnknownRequestFormatException();
				} else {
					Integer msgPriority = Integer.parseInt(tokens[2]);
					Integer senderId = Integer.parseInt(tokens[3]);
					Integer receiverId = Integer.parseInt(tokens[4]);
					Integer contextId = Integer.parseInt(tokens[5]);
					String queueList = tokens[6].substring(1, tokens[6].length() - 1);
					String content = tokens[7];
					
					String[] queues = queueList.split(" ");
					List<Long> qList = new ArrayList<Long>();
					for(String q : queues) {
						qList.add(Long.parseLong(q));
					}
					
					Message m = new Message();
					m.setPriority(msgPriority);
					m.setSender(senderId);
					m.setReceiver(receiverId);
					m.setContext(contextId);
					m.setQueue(qList);
					m.setContent(content);
					Timestamp time = new Timestamp(System.currentTimeMillis());
					m.setTimestamp(time);
					
					returnMap.put(MapKey.MESSAGE, m);
					returnMap.put(MapKey.QUEUE_ID_LIST, qList);
					
					return returnMap;
				}
			default: throw new UnknownRequestFormatException();
			}
		} catch (NumberFormatException e) {
			throw new UnknownRequestFormatException();
		}
	}
	
	private static Map<Integer, Object> decodeReceiveOneMessage(String[] tokens) {
		//format: command_type,connectionID,queue_id
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		
		Integer queueId = Integer.parseInt(tokens[2]);
		map.put(MapKey.QUEUE_ID, queueId);
		
		return map;
	}

	public static String encodeList(List<? extends Object> list) {
		String ret = "[";
		for(int i = 0; i < list.size(); i++) {
			if (i>0) {
				ret += " ";
			}
			ret += list.get(i).toString();
		}
		
		ret += "]";
		
		return ret;
	}

    public static List<Long> decodeList(String listString) {
        listString = listString.substring(1, listString.length() - 1);
        String[] objectArr = listString.split(" ");
        List<Long> objectList = new ArrayList<>();
        for(String q : objectArr) {
            objectList.add(Long.parseLong(q));
        }
        return objectList;
    }

}
