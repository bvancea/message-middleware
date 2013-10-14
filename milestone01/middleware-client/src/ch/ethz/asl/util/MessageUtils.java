package ch.ethz.asl.util;

import java.text.ParseException;
import java.util.List;

import exception.UnknownRequestFormatException;
import exception.WrongResponseException;

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
		
		switch(type) {
		case CommandType.AUTHENTICATE: break;
		case CommandType.CREATE_QUEUE: break;
		case CommandType.DELETE_QUEUE: break;
		case CommandType.FIND_QUEUE: break;
		case CommandType.READ_MESSAGE_EARLIEST: 
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.READ_MESSAGE_PRIORITY: 
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.RECEIVE_MESSAGE_FOR_RECEIVER: break;
		case CommandType.RETRIEVE_MESSAGE_EARLIEST: 
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.RETRIEVE_MESSAGE_PRIORITY: 
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.SEND_MESSAGE: break;
		default: throw new WrongResponseException();
		}
		
		return null;
	}
	
	public static Object decodeRequestMessage(int type, String message) throws UnknownRequestFormatException {
		
		switch(type) {
		case CommandType.AUTHENTICATE: 
			//format: command_type,connectionID,username,password
			
			break;
		case CommandType.CREATE_QUEUE: 
			//format: command_type,connectionID,queue_name
			
			break;
		case CommandType.DELETE_QUEUE: 
			//format: command_type,connectionID,queue_id
			
			break;
		case CommandType.FIND_QUEUE: 
			//format: command_type,connectionID,queue_name
			
			break;
		case CommandType.READ_MESSAGE_EARLIEST:
			//format: command_type,connectionID,queue_id
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.READ_MESSAGE_PRIORITY: 
			//format: command_type,connectionID,queue_id
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.RECEIVE_MESSAGE_FOR_RECEIVER: 
			//format: command_type,connectionID,[queue_id1 queue_id2 ...]
			break;
		case CommandType.RETRIEVE_MESSAGE_EARLIEST: 
			//format: command_type,connectionID,queue_id
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.RETRIEVE_MESSAGE_PRIORITY: 
			//format: command_type,connectionID,queue_id
			decodeReceiveOneMessage(type, message);
			break;
		case CommandType.SEND_MESSAGE: 
			//format: command_type,connectionID,msgId,msgPriority,
			//		  senderId,receiverId,contextId,[qId1 qId2 ...],content
			
			break;
		default: throw new UnknownRequestFormatException();
		}
		
		return null;
	}
	
	private static Object decodeReceiveOneMessage(int type, String message) {
		return null;
	}
	
	public static String encodeList(List<Integer> list) {
		String ret = "[";
		for(int i = 0; i < list.size(); i++) {
			if (i>0) {
				ret += " ";
			}
			ret += Integer.toString(list.get(i));
		}
		
		ret += "]";
		
		return ret;
	}

}
