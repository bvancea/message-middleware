package ch.ethz.asl.message;


import ch.ethz.asl.message.domain.Client;
import ch.ethz.asl.message.persistence.ClientMapper;
import ch.ethz.asl.message.persistence.MapperRegistry;
import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;

import java.sql.SQLException;

public class ClientService {

    private static final Log LOG = LogFactory.getLog(ClientService.class);

    ClientMapper mapper = (ClientMapper) MapperRegistry.lookup(Client.class);

    public int authenticate(String username, String password) {
        int status = 0;
        try {
            Client client = mapper.findByUsernameAndPassword(username, password);
        } catch (SQLException e) {
            status = Errors.AUTHENTICATION_FAILED;
            LOG.error("Failed to authenticate client");
        }
        return status;
    }
}
