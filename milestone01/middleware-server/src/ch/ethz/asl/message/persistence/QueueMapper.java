package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.Queue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class QueueMapper extends AbstractMapper<Queue>{

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
        return "{call delete_queue(?)";
    }

    @Override
    public List<Object> toDatabaseParams(Queue entity) {
        return Arrays.asList( (Object) entity.getId(), entity.getName(), entity.getCreator() );
    }

    @Override
    public Queue load(ResultSet rs) throws SQLException {
        return new Queue(rs.getInt(1), rs.getString(2), rs.getInt(3));
    }

}
