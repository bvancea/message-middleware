package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Client;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return "{call delete_client(?)";
    }

    @Override
    public List<Object> toDatabaseParams(Client entity) {
        return Arrays.asList((Object) entity.getName(), entity.getUsername(), entity.getPassword());
    }

    public Client findByUsernameAndPassword(String username, String password) throws SQLException {
        CallableStatement statement = getDataSource().getConnection().prepareCall(FIND_CLIENT_USERNAME_PASSWORD);
        statement.setString(1, username);
        statement.setString(2, password);
        return load(statement.executeQuery());
    }

    @Override
    public Client load(ResultSet rs) throws SQLException {
        rs.next();
        if (!rs.isBeforeFirst()) {
            Client client = new Client();
            client.setId(rs.getInt(1));
            client.setName(rs.getString(2));
            client.setUsername(rs.getString(3));
            client.setPassword(rs.getString(4));
            return client;
        } else {
            return null;
        }
    }
}
