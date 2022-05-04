import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class DBHelper {
    private static Connection dbConn = null;

    public static void main(String[] args) {
        DBHelper dbHelper = new DBHelper();
        dbHelper.dbConnectWithProperties("config.properties");
    }

    private void dbConnectWithProperties(String filename) {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            prop.load(input);

            String dbDriverClass = prop.getProperty("db.driver.class");
            String dbConnUrl = prop.getProperty("db.conn.url");
            String dbUserName = prop.getProperty("db.username");
            String dbPassword = prop.getProperty("db.password");

            System.out.println(dbDriverClass);
            System.out.println(dbConnUrl);
            System.out.println(dbUserName);
            System.out.println(dbPassword);

            Class.forName(dbDriverClass);
            dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                dbConn.close();
                System.out.println(dbConn.isClosed());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    protected static Connection startDBPGConnection(String filename) throws IOException, ClassNotFoundException, SQLException {
        InputStream input = DBHelper.class.getClassLoader().getResourceAsStream(filename);
        Properties prop = new Properties();
        prop.load(input);

        String dbDriverClass = prop.getProperty("db.driver.class");

        String dbConnUrl = prop.getProperty("db.conn.url");

        String dbUserName = prop.getProperty("db.username");

        String dbPassword = prop.getProperty("db.password");

//        System.out.println(dbDriverClass);
//        System.out.println(dbConnUrl);
//        System.out.println(dbUserName);
//        System.out.println(dbPassword);

        Class.forName(dbDriverClass);
        dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
        return dbConn;

    }

    protected static void closeDBPGConnection(Connection dbConn) throws SQLException {
        if (!dbConn.isClosed()){
            dbConn.close();
        }
    }
}