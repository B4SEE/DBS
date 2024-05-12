package cs.cvut.fel.dbs;

import cs.cvut.fel.dbs.app_logic.DatabaseConnection;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting application...");
        DatabaseConnection.connect();
        Connection connection = DatabaseConnection.getConnection();
        logger.info("Connection: " + connection);
    }
}