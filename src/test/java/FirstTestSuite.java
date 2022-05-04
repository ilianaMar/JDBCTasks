import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;

@DisplayName("First test suite")
public class FirstTestSuite {
    static Connection dbConnection;

    @BeforeAll
    static void beforeAll() throws SQLException, IOException {
        dbConnection = DBHelper.startDBPGConnection("config.properties");
        System.out.println("open db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());

    }

    @AfterAll
    static void afterAll() throws SQLException {
        DBHelper.closeDBPGConnection(dbConnection);
        System.out.println("close db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());
    }

    @Test
    @DisplayName("test with db connection")
    void simpleTest(){
        System.out.println("hello");
    }

}
