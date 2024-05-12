package cs.cvut.fel.dbs.app_logic;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);
    private static Connection connection;
    public static void connect() {
        logger.info("Connecting to database...");
        try {
            Class.forName("org.postgresql.Driver");

            // Get username and password from user_credentials/credentials.txt
            File file = new File("user_credentials/credentials.txt");
            if (!file.exists()) {
                logger.error("Credentials file not found.");
                System.exit(1);
            }

            String hostname = "";
            String username = "";
            String password = "";

            try {
                Scanner scanner = new Scanner(file);
                hostname = scanner.nextLine();
                username = scanner.nextLine();
                password = scanner.nextLine();
                scanner.close();
            } catch (Exception e) {
                logger.error("Failed to read credentials from file: " + e.getMessage());
                System.exit(1);
            }

            connection = DriverManager.getConnection(
                    hostname,
                    username,
                    password);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Failed to connect to database: " + e.getMessage());
            System.exit(1);
        }
    }
    public static Connection getConnection() {
        return connection;
    }
}
