package cs.cvut.fel.dbs.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DatabaseConnection {
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);
    private static EntityManagerFactory entityManagerFactory;
    private static Connection connection;
    public static void connect() {
        logger.info("Connecting to database...");
        try {
            Class.forName("org.postgresql.Driver");
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

        Map<String, String> env = System.getenv();
        Map<String, String> configOverrides = new HashMap<>();
        configOverrides.put("javax.persistence.jdbc.url", env.get("db_host_url"));
        configOverrides.put("javax.persistence.jdbc.user", env.get("db_username"));
        configOverrides.put("javax.persistence.jdbc.password", env.get("db_password"));

        entityManagerFactory = Persistence.createEntityManagerFactory("myPU", configOverrides);
        logger.info("Connected to database.");
    }
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.error("Connection is not initialized.");
            System.exit(1);
        }
        return connection;
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            logger.error("Entity manager factory is not initialized.");
            System.exit(1);
        }
        return entityManagerFactory.createEntityManager();
    }
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Failed to close connection: " + e.getMessage());
            }
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
