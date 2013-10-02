package ch.ethz.asl.message.persistence;

import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<T> {

	private DatabaseConnection connection;
	
	public void setDatabaseConnection(DatabaseConnection connection) {
		this.connection = connection;
	}

	public abstract T persist(T entity);
	
	public abstract T findOne(int id);
	
	public abstract List<T> findAll();
	
	public abstract void delete(int id);

	public abstract List<T> find(Map<String, Object> conditions);
}
