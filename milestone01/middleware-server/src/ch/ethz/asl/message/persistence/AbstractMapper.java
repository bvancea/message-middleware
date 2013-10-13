package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Client;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMapper<T> {

    private DataSource dataSource;

    protected abstract String persistStatement();
    protected abstract String findStatement();
    protected abstract String findAllStatement();
    protected abstract String deleteStatement();

    public abstract List<Object> toDatabaseParams(T entity);
    public abstract T load(ResultSet rs) throws SQLException;

    public AbstractMapper() {
        dataSource = retrieveDataSource();
    }

    public T persist(T entity) throws SQLException {
        CallableStatement statement = getConnection().prepareCall(persistStatement());
        List<Object> parameterMap = toDatabaseParams(entity);
        addParameters(statement, parameterMap);
        return load(statement.executeQuery());
    }
	
	public T findOne(int id) throws SQLException {
        CallableStatement statement = getConnection().prepareCall(findStatement());
        statement.setLong(1, id);
        return load(statement.executeQuery());
    }
	
	public List<T> findAll() throws SQLException {
        CallableStatement statement = getConnection().prepareCall(findAllStatement());
        return loadAll(statement.executeQuery());
    }
	
	public void delete(int id) throws SQLException {
        CallableStatement statement = getConnection().prepareCall(deleteStatement());
        statement.executeUpdate();
    }

    public List<T> loadAll(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(load(rs));
        }
        return rows;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private DataSource retrieveDataSource() {
        try {
            return DB.getDataSource();
        } catch (NamingException e) {
            throw new RuntimeException("Cannot locate the data source in the JNDI directory.");
        }
    }

    private CallableStatement addParameters(CallableStatement statement, List<Object> params) throws SQLException {
        int index = 1;
        for (Object entry : params) {
            statement.setObject(index++, entry);
        }
        return statement;
    }

    protected DataSource getDataSource() {
        return dataSource;
    }



}
