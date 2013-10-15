package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Queue;

import java.sql.*;
import java.util.*;

public class QueueMapper extends AbstractMapper<Queue>{

    private static final String FIND_BY_NAME = "{call find_queue(?)}";

    @Override
    protected java.lang.String persistStatement() {
        return "{call add_queue(?, ?)}";
    }

    @Override
    protected java.lang.String findStatement() {
        return "{call find_queue(?)}";
    }

    @Override
    protected java.lang.String findAllStatement() {
        return "{call find_queues()}";
    }

    @Override
    protected java.lang.String deleteStatement() {
        return "{call delete_queue(?)}";
    }

    @Override
    public Map<Object, Integer> toDatabaseParams(Queue entity) {
        HashMap<Object, Integer> map = new LinkedHashMap<>();
        map.put(entity.getName(), Types.VARCHAR);
        map.put(entity.getCreator(), Types.BIGINT);
        return map;
    }

    @Override
    public Queue load(ResultSet rs) throws SQLException {
        if (rs.next() && !rs.isBeforeFirst()) {
            return new Queue(rs.getInt(1), rs.getString(2), rs.getInt(3));
        } else {
            return null;
        }
    }

    public Queue findByName(String name) throws SQLException {

        final Connection connection = getConnection();
        CallableStatement statement = connection.prepareCall(FIND_BY_NAME);
        statement.setString(1, name);
        Queue queue = load(statement.executeQuery());

        connection.close();
        return queue;
    }

}
