package ch.ethz.asl.persistence;

import ch.ethz.asl.message.domain.Client;
import ch.ethz.asl.message.persistence.ClientMapper;
import ch.ethz.asl.message.persistence.MapperRegistry;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Test for client database operations.
 */
public class ClientMapperTest {

    private static ClientMapper clientMapper = (ClientMapper) MapperRegistry.lookup(Client.class);
    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_NAME = "test_name";

    @Before
    public void cleanUpBefore() throws SQLException {
        removeClient(TEST_USERNAME, TEST_PASSWORD);
    }

    @After
    public void cleanUp() throws SQLException {
        removeClient(TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    public void testAddClient() throws SQLException {
        Client first = addClient(TEST_NAME, TEST_USERNAME, TEST_PASSWORD);
        Client second = findClient(TEST_USERNAME, TEST_PASSWORD);
        Assert.assertEquals(first, second);
    }

    private Client addClient(String name, String username, String password) throws SQLException {
        Client client = new Client();
        client.setName(TEST_NAME);
        client.setUsername(TEST_USERNAME);
        client.setPassword(TEST_PASSWORD);

        return clientMapper.persist(client);
    }

    private Client findClient(String username, String password) throws SQLException {
        return clientMapper.findByUsernameAndPassword(username, password);
    }

    private void removeClient(int id) throws SQLException {
        clientMapper.delete(id);
    }

    private void removeClient(String username, String password) throws SQLException {
        Client client = findClient(username, password);
        if (client != null) {
            removeClient(client.getId());
        }
    }





}
