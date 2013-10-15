package ch.ethz.asl.message.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import org.junit.Test;

import ch.ethz.asl.exceptions.EmptyMessageException;
import ch.ethz.asl.exceptions.UnknownRequestFormatException;
import ch.ethz.asl.util.MapKey;
import ch.ethz.asl.util.MessageUtils;

public class DecodeMessageTest {

	@Test
	public void test() throws EmptyMessageException, UnknownRequestFormatException {
		String message = "1,3,[1 2 3]";
		Map<Integer, Object> map = MessageUtils.decodeRequestMessage(message);

        Assert.assertEquals(1, map.get(MapKey.COMMAND_TYPE));
        Assert.assertEquals(3, map.get(MapKey.CONNECTION_ID));
        Assert.assertEquals( new ArrayList<Integer>() {{ add(1); add(2); add(3);}}, map.get(MapKey.QUEUE_ID_LIST));
	}

}
