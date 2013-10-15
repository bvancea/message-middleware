package ch.ethz.asl.message.service;


import ch.ethz.asl.message.Errors;
import ch.ethz.asl.message.domain.Client;
import ch.ethz.asl.message.persistence.ClientMapper;
import ch.ethz.asl.message.persistence.MapperRegistry;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import ch.ethz.asl.util.CommandType;
import ch.ethz.asl.util.MapKey;
import ch.ethz.asl.util.MessageUtils;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Map;

public class ClientService {

    private static final Log LOG = LogFactory.getLog(ClientService.class);

    ClientMapper mapper = (ClientMapper) MapperRegistry.lookup(Client.class);

    public ByteBuffer authenticate(Map<Integer, Object> parameters) {

        String username = (String) parameters.get(MapKey.USERNAME);
        String password = (String) parameters.get(MapKey.PASSWORD);
        int connectionId = authenticate(username, password);

        String responseString = MessageUtils.encodeMessage(CommandType.AUTHENTICATE, new String[0], connectionId);

        return ByteBuffer.wrap(responseString.getBytes());
    }

    public int authenticate(String username, String password) {
        int status = 0;
        try {
            Client client = mapper.findByUsernameAndPassword(username, password);
            status = client.getId();
        } catch (SQLException e) {
            status = Errors.AUTHENTICATION_FAILED;
            LOG.error("Failed to authenticate client");
        }
        return status;
    }
}
