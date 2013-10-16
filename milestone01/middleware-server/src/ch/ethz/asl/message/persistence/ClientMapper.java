package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Client;

import java.sql.*;
import java.util.*;

public class ClientMapper extends AbstractMapper<Client>{

    private static final String FIND_CLIENT_USERNAME_PASSWORD = "{call find_client_username_password(?, ?)}";

    @Override
    protected String persistStatement() {
        return "{call add_client(?, ?, ?)}";
    }

    @Override
    protected String findStatement() {
        return "{call find_client_id(?)}";
    }

    @Override
    protected String findAllStatement() {
        return "{call find_clients()}";
    }

    @Override
    protected String deleteStatement() {
        return "{call delete_client(?)}";
    }

    @Override
    public Map<Object, Integer> toDatabaseParams(Client entity) {
        Map<Object, Integer> map = new LinkedHashMap<>();
        map.put(entity.getName(), Types.VARCHAR);
        map.put(entity.getUsername(), Types.VARCHAR);
        map.put(entity.getPassword(), Types.VARCHAR);
        return map;
    }

    public Client findByUsernameAndPassword(String username, String password) throws SQLException {
        final Connection connection  = getConnection();

        CallableStatement statement = connection.prepareCall(FIND_CLIENT_USERNAME_PASSWORD);
        statement.setString(1, username);
        statement.setString(2, password);
        Client returnedValue = load(statement.executeQuery());
        connection.close();

        return returnedValue;
    }

    @Override
    public Client loadOne(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt(1));
        client.setName(rs.getString(2));
        client.setUsername(rs.getString(3));
        client.setPassword(rs.getString(4));
        return client;
    }
}
