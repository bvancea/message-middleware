package ch.ethz.asl.message.persistence;

import ch.ethz.asl.message.shared.log.Log;
import ch.ethz.asl.message.shared.log.LogFactory;
import org.postgresql.ds.PGPoolingDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Holds database constants.
 */
public class DB {

    private static final Log LOG = LogFactory.getLog(DB.class);
    public static final String JNDI_NAME = "jdbc/asl";
    public static final String NAME = "asl";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String URL = "localhost";
    public static final int PORT = 5432;

    public static final int CONNECTION_POOL_SIZE = 10;
    public static final int STATEMENT_POOL_SIZE = 10;
    private static DataSource dataSource;

    public static Context jndiContext;

    static {
        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            LOG.error("Error initializing the JNDI InitialContext.");
        }
    }

    public static DataSource initializePGDataSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setDatabaseName(NAME);
        dataSource.setPortNumber(PORT);
        dataSource.setServerName(URL);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaxConnections(CONNECTION_POOL_SIZE);
        dataSource.setMaxConnections(STATEMENT_POOL_SIZE);
        dataSource.setDataSourceName(NAME);
        return dataSource;
    }

    public static DataSource getDataSource() throws NamingException {
        if (dataSource == null) {
            dataSource = initializePGDataSource();
        }
        return dataSource;
    }
}
