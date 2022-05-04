import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;

@DisplayName("First test suite")
public class FirstTestSuite {
    private static DbHelper dbHelper;

    static {
        try {
            dbHelper = new DbHelper(DbHelper.postgresConfData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Connection dbConnection;

    @BeforeAll
    static void beforeAll() throws SQLException {
        dbConnection = dbHelper.startDbPgConnection();
        System.out.println("open db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());

    }

    @AfterAll
    static void afterAll() throws SQLException {
        dbHelper.closeDbPgConnection(dbConnection);
        System.out.println("close db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());
    }

    @Test
    @DisplayName("test with db connection")
    void simpleTest(){
        System.out.println("hello");
    }

}
