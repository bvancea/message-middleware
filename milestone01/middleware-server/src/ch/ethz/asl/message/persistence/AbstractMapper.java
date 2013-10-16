package ch.ethz.asl.message.persistence;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<T> {

    private DataSource dataSource;

    protected abstract String persistStatement();
    protected abstract String findStatement();
    protected abstract String findAllStatement();
    protected abstract String deleteStatement();

    public abstract Map<Object, Integer> toDatabaseParams(T entity) throws SQLException;

    public abstract T loadOne(ResultSet rs) throws SQLException;

    public AbstractMapper() {
        dataSource = retrieveDataSource();
    }

    public T persist(T entity) throws SQLException {
        final Connection connection  = getConnection();
        CallableStatement statement = connection.prepareCall(persistStatement());

        Map<Object, Integer> parameterMap = toDatabaseParams(entity);
        addParameters(statement, parameterMap);

        T returnedValue = load(statement.executeQuery());
        connection.close();
        return returnedValue;
    }
	
	public T findOne(int id) throws SQLException {
        final Connection connection  = getConnection();

        CallableStatement statement = getConnection().prepareCall(findStatement());
        statement.setLong(1, id);
        T returnedValue = load(statement.executeQuery());

        connection.close();
        return returnedValue;
    }
	
	public List<T> findAll() throws SQLException {
        final Connection connection  = getConnection();
        CallableStatement statement = getConnection().prepareCall(findAllStatement());

        List<T> returnedValue = loadAll(statement.executeQuery());
        connection.close();
        return returnedValue;
    }
	
	public void delete(int id) throws SQLException {
        final Connection connection  = getConnection();
        CallableStatement statement = getConnection().prepareCall(deleteStatement());
        statement.setInt(1, id);
        statement.executeUpdate();
        connection.close();
    }

    public T load(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return loadOne(rs);
        } else {
            return null;
        }
    }

    public List<T> loadAll(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(loadOne(rs));
        }
        return rows;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private DataSource retrieveDataSource() {
        try {
            return DB.getDataSource();
        } catch (NamingException e) {
            throw new RuntimeException("Cannot locate the data source in the JNDI directory.");
        }
    }

    private CallableStatement addParameters(CallableStatement statement, Map<Object, Integer> params) throws SQLException {
        int index = 1;
        for (Map.Entry<Object, Integer> entry : params.entrySet()) {
            statement.setObject(index++, entry.getKey(), entry.getValue());
        }
        return statement;
    }

    protected DataSource getDataSource() {
        return dataSource;
    }

}
