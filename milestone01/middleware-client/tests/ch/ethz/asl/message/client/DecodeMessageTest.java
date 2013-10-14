package ch.ethz.asl.message.client;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.ethz.asl.exceptions.EmptyMessageException;
import ch.ethz.asl.exceptions.UnknownRequestFormatException;
import ch.ethz.asl.util.MapKey;
import ch.ethz.asl.util.MessageUtils;

public class DecodeMessageTest {

	@Test
	public void test() {
		
		String message = "1,3,[1 2 3]";
		try {
			Map<Integer, Object> map = MessageUtils.decodeRequestMessage(message);
			
			System.out.println(map.get(MapKey.COMMAND_TYPE));
			System.out.println(map.get(MapKey.CONNECTION_ID));
			List<Integer> list = (List<Integer>) map.get(MapKey.QUEUE_ID_LIST);
			for (Integer i : list) {
				System.out.println(i);
			}
			
		} catch (UnknownRequestFormatException | EmptyMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
