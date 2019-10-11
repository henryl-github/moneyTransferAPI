package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtils {

    private static final Logger log = LogManager.getLogger(DBUtils.class);
    public static void rollbackTransaction(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error("rollback exception", e);
            }
        }
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("close db connection exception", e);
            }
        }
    }
}
