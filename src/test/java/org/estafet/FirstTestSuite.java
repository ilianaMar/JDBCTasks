package org.estafet;
import org.estafet.helpers.DbHelper;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import com.github.javafaker.Faker;
import org.estafet.objects.CustomerObject;
import org.estafet.models.CustomerModel;

@DisplayName("First test suite")
public class FirstTestSuite {

    @Nested
    @DisplayName("Nested first class tests")
    static class FirstSubTestsClass{
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
        static void  beforeAll() throws SQLException {
            dbConnection = dbHelper.startDbConnection();
            System.out.println("open db connection");
            System.out.println("DB connection is closed = " + dbConnection.isClosed());

        }

        @AfterAll
        static void afterAll() throws SQLException {
            dbHelper.closeDbConnection(dbConnection);
            System.out.println("close db connection");
            System.out.println("DB connection is closed = " + dbConnection.isClosed());
        }

        @Test
        @DisplayName("First test with db connection")
        void simpleTest(){
            System.out.println("hello");
        }

    }


    @Nested
    @DisplayName("Nested second class tests")
    class SecondSubTestsClass {

        @Test
        @DisplayName("Second test with insert db record")
        void TestCustomerInsertion() throws SQLException, IOException {
            Faker faker = new Faker();
            CustomerModel newCustomer = new CustomerModel();
            CustomerObject customerObject = new CustomerObject();
            newCustomer.setName(faker.name().fullName());
            newCustomer.setAge(faker.number().numberBetween(20,90));
            newCustomer.setEmail(faker.internet().emailAddress());
            newCustomer.setPhone(faker.phoneNumber().cellPhone());
            newCustomer.set_active(true);
            newCustomer.setGdpr_set(true);
            customerObject.create(newCustomer);
        }

    }



}
