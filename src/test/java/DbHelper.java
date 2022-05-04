import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;

/**
 * Database helper functions for establishing db connections with provided database configuration data
 * and closing connection if it is opened.
 */
public class DbHelper {
    static Connection dbConn = null;
    protected static final String postgresConfData = "src/test/resources/config.properties";
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
       DbHelper dbConnection = new DbHelper(postgresConfData);
       dbConnection.dbConnectWithProperties();
    }

    private void dbConnectWithProperties() {
        try (Connection dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword)) {
            System.out.println(dbConn.isClosed());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected Connection startDbPgConnection() throws SQLException {
        dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
        return dbConn;
    }

    protected void closeDbPgConnection(Connection dbConn) throws SQLException {
        if (!dbConn.isClosed()){
            dbConn.close();
        }
    }
}