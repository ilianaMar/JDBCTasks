package org.estafet.helpers;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;

/**
 * Database helper functions for establishing db connections with provided database configuration data
 * and closing connection if it is opened.
 */
public class DbHelper {
    public static final String postgresConfData = "src/test/resources/config.properties";
    private final String dbConnUrl, dbUserName, dbPassword;

    public DbHelper(String confFile) throws IOException {
        FileInputStream input = new FileInputStream(confFile);
        Properties prop = new Properties();
        prop.load(input);
        this.dbConnUrl = prop.getProperty("db.conn.url");
        this.dbUserName = prop.getProperty("db.username");
        this.dbPassword = prop.getProperty("db.password");
    }

    public static void main(String[] args) throws IOException {

    }

    public PreparedStatement dbConnectWithProperties(String query) {
        try (Connection dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword)) {
            System.out.println(dbConn.isClosed());
            PreparedStatement prepareStatement = dbConn.prepareStatement(query);
            return prepareStatement;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Connection startDbConnection() throws SQLException {
        return DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
    }

    public void closeDbConnection(Connection dbConn) throws SQLException {
        if (!dbConn.isClosed()){
            dbConn.close();
        }
    }
}