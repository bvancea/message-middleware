package ch.ethz.asl.util;

import java.text.ParseException;
import java.util.List;

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
	
	public static Object decodeMessage(int type, String message) throws WrongResponseException {
		
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
