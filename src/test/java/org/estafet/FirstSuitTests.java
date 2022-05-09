package org.estafet;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.github.javafaker.Faker;
import org.estafet.objects.CustomerObject;
import org.estafet.models.Customer;
import org.estafet.helpers.DbHelper;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("First test suite")
public class FirstSuitTests {
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

    @Test
    @Disabled("This will be implemented in next task")
    @DisplayName("Second test with insert db record")
    void testCustomerInsertion() throws SQLException, IOException {
        Faker faker = new Faker();
        Customer newCustomer = new Customer();
        CustomerObject customerObject = new CustomerObject();
        newCustomer.setName(faker.name().fullName());
        newCustomer.setAge(faker.number().numberBetween(20,90));
        newCustomer.setEmail(faker.internet().emailAddress());
        newCustomer.setPhone(faker.phoneNumber().cellPhone());
        newCustomer.set_active(true);
        newCustomer.setGdpr_set(true);
        customerObject.create(dbConnection, newCustomer);
    }

    @Test
    @DisplayName("Third test with get all customers")
    void testGetAllCustomers() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        List<Customer> customers = customerObject.getCustomers(dbConnection);
//        System.out.println(customers.get(customers.size()-1));
        for (Customer allCustomers : customers) {
            System.out.println(allCustomers);
        }
    }

    @Test
    @DisplayName("Get first customer is correct")
    void getFirstCustomer() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        List<Customer> customers = customerObject.getCustomers(dbConnection);
        Customer firstCustomer = customers.get(0);
        System.out.println(firstCustomer);
        assertEquals(1, firstCustomer.getCustomer_id());
        assertEquals("iliana", firstCustomer.getName());
        assertEquals("iliana@test.com", firstCustomer.getEmail());
        assertEquals("123444", firstCustomer.getPhone());
        assertEquals(38, firstCustomer.getAge());
        assertTrue(firstCustomer.is_active());
        assertTrue(firstCustomer.isGdpr_set());
    }
}
