package db;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;

public class DataSource {

    private static final HikariDataSource ds;

    static {
        ds= new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:moneyTransferAPI;" +
                "INIT=RUNSCRIPT FROM 'classpath:scripts/schema.sql';" +
                "TRACE_LEVEL_FILE=1");
        ds.setUsername("sa");
        ds.setPassword("sa");
        ds.setAutoCommit(false);
    }

    private DataSource() {
    }

    public static Connection getConnection() throws Exception {
        return ds.getConnection();
    }
}
