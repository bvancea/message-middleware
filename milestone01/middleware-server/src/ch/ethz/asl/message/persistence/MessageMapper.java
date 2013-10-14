package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MessageMapper extends AbstractMapper<Message> {


    @Override
    protected java.lang.String persistStatement() {
        return "{call add_message(?, ?, ?)}";
    }

    @Override
    protected java.lang.String findStatement() {
        return "{call find_message(?)}";
    }

    @Override
    protected java.lang.String findAllStatement() {
        return "{call find_messages()}";
    }

    @Override
    protected java.lang.String deleteStatement() {
        return "{call delete_message(?)";
    }

    @Override
    public List<Object> toDatabaseParams(Message entity) {

        return Arrays.asList((Object)   entity.getId(),
                                        entity.getSender(),
                                        entity.getReceiver(),
                                        entity.getQueue(),
                                        entity.getPriority(),
                                        entity.getContext(),
                                        entity.getTimestamp(),
                                        entity.getContent());
    }

    @Override
    public Message load(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt(1));
        message.setSender(rs.getInt(2));
        message.setReceiver(rs.getInt(3));
        message.setQueue(Arrays.asList((Integer) rs.getArray(4).getArray()));
        message.setPriority(rs.getInt(5));
        message.setContext(rs.getInt(6));
        message.setTimestamp(rs.getTimestamp(7));
        message.setContent(rs.getString(8));
        return message;
    }

}
