package org.estafet.helpers;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbHelper {
    public static final String postgresConfData = "src/test/resources/config.properties";
    private final String dbConnUrl;
    private final String dbUserName;
    private final String dbPassword;

    public DbHelper(String confFile) throws IOException {
        FileInputStream input = new FileInputStream(confFile);
        Properties prop = new Properties();
        prop.load(input);
        this.dbConnUrl = prop.getProperty("db.conn.url");
        this.dbUserName = prop.getProperty("db.username");
        this.dbPassword = prop.getProperty("db.password");
    }

    public Connection startDbConnection() throws SQLException {
        return DriverManager.getConnection(this.dbConnUrl, this.dbUserName, this.dbPassword);
    }

    public void closeDbConnection(Connection dbConn) throws SQLException {
        if (!dbConn.isClosed()) {
            dbConn.close();
        }
    }
}