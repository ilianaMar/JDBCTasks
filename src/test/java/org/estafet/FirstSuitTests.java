package org.estafet;
import org.estafet.models.CustomerAddress;
import org.estafet.objects.CustomerAddressObject;
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
        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        CustomerAddress newCustomerAddress = CustomerAddress.builder()
                .address(faker.address().streetName())
                .city(faker.address().city())
                .country(faker.address().country())
                .state(faker.address().state())
                .postal_code(faker.number().numberBetween(1000,9000))
                .build();
        customerAddressObject.save(dbConnection, newCustomerAddress);
        List<CustomerAddress> customerAddresses = customerAddressObject.getCustomerAddresses(dbConnection);
        CustomerAddress lastCustomerAddress = customerAddresses.get(customerAddresses.size()-1);
        Customer newCustomer = Customer.builder()
                .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()) )
                .age(faker.number().numberBetween(20,90))
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .is_active(true)
                .gdpr_set(true)
                .address_id(lastCustomerAddress.getAddress_id())
                .build();
        customerObject.save(dbConnection, newCustomer);
        List<Customer> customers = customerObject.getCustomers(dbConnection);
        Customer lastCustomer = customers.get(customers.size()-1);
        assertEquals(lastCustomer.getName(), newCustomer.getName());
        assertEquals(lastCustomer.getEmail(), newCustomer.getEmail());
        assertEquals(lastCustomer.getAge(), newCustomer.getAge());
        assertEquals(lastCustomer.getPhone(), newCustomer.getPhone());
        assertEquals(lastCustomer.getAddress_id(), lastCustomerAddress.getAddress_id());
        assertNotNull(lastCustomer.getCreated_time());
        assertNull(lastCustomer.getReason_for_deactivation());
        assertNull(lastCustomer.getUpdated_time());
        assertNull(lastCustomer.getNotes());
        assertTrue(lastCustomer.is_active());
        assertTrue(lastCustomer.isGdpr_set());
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
