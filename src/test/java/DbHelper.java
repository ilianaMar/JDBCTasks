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
    private static Connection dbConn = null;
    private static final String filePath = "src/test/resources/config.properties";

    public static void main(String[] args) {
        DbHelper.dbConnectWithProperties();
    }

    private static Connection dbConnectWithProperties() {
        try (FileInputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            String dbConnUrl = prop.getProperty("db.conn.url");
            String dbUserName = prop.getProperty("db.username");
            String dbPassword = prop.getProperty("db.password");

            System.out.println(dbConnUrl);
            System.out.println(dbUserName);
            System.out.println(dbPassword);
            dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
            return dbConn;
        } catch (IOException | SQLException  ex) {
            ex.printStackTrace();
        } finally {
            try {
                dbConn.close();
                System.out.println(dbConn.isClosed());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected static Connection startDBPGConnection() throws IOException, SQLException {
        FileInputStream input = new FileInputStream(filePath);
        Properties prop = new Properties();
        prop.load(input);
        String dbConnUrl = prop.getProperty("db.conn.url");
        String dbUserName = prop.getProperty("db.username");
        String dbPassword = prop.getProperty("db.password");
        dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
        return dbConn;
    }

    protected static void closeDBPGConnection(Connection dbConn) throws SQLException {
        if (!dbConn.isClosed()){
            dbConn.close();
        }
    }
}