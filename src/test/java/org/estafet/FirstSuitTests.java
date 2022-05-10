package org.estafet;
import org.estafet.models.CustomerAddress;
import org.estafet.objects.CustomerAddressObject;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.github.javafaker.Faker;
import org.estafet.objects.CustomerObject;
import org.estafet.models.Customer;
import org.estafet.helpers.DbHelper;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("Second test with insert db record")
    void testCustomerInsertion() throws SQLException {
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
        List<CustomerAddress> customerAddresses = customerAddressObject.getAll(dbConnection);
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
        List<Customer> customers = customerObject.getAll(dbConnection);
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
        List<Customer> customers = customerObject.getAll(dbConnection);
//        System.out.println(customers.get(customers.size()-1));
        for (Customer allCustomers : customers) {
            System.out.println(allCustomers);
        }
    }

    @Test
    @DisplayName("Get first customer is correct")
    void getFirstCustomer() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        List<Customer> customers = customerObject.getAll(dbConnection);
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

    @Test
    @DisplayName("Get customer and address by id")
    void getSpecificCustomerData() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        Customer firstCustomer = customerObject.getById(dbConnection, 1);
        CustomerAddress firstCustomerAddress = customerAddressObject.getById(dbConnection, 1);
        HashMap<Customer, CustomerAddress> allCustomersData = customerObject.getAllCustomerAddressDataById(dbConnection, 1);

        for(Customer customer: allCustomersData.keySet()){
            assertEquals(firstCustomer.getCustomer_id(), customer.getCustomer_id());
            assertEquals(firstCustomer.getName(), customer.getName());
            assertEquals(firstCustomer.getPhone(), customer.getPhone());
            assertEquals(firstCustomer.getEmail(), customer.getEmail());
            assertEquals(firstCustomer.getAge(), customer.getAge());
            assertEquals(firstCustomerAddress.getAddress(), allCustomersData.get(customer).getAddress());
            assertEquals(firstCustomerAddress.getAddress_id(), allCustomersData.get(customer).getAddress_id());
            assertEquals(firstCustomerAddress.getCity(), allCustomersData.get(customer).getCity());
            assertEquals(firstCustomerAddress.getCountry(), allCustomersData.get(customer).getCountry());
            assertEquals(firstCustomerAddress.getProvince(), allCustomersData.get(customer).getProvince());
            assertEquals(firstCustomerAddress.getState(), allCustomersData.get(customer).getState());
//            System.out.println(customer);
//            System.out.println(allCustomersData.get(customer));
        }
    }

    @Test
    @DisplayName("Delete customer and address by id")
    void deleteSpecificCustomerData() throws SQLException {
        int customer_id = 11;
        int address_id = 0;

        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        HashMap<Customer, CustomerAddress> allCustomersData = customerObject.getAllCustomerAddressDataById(dbConnection, customer_id);
        for(Customer customer: allCustomersData.keySet()){
            address_id = allCustomersData.get(customer).getAddress_id();
            customerObject.delete(dbConnection, customer.getCustomer_id());
            customerAddressObject.delete(dbConnection, address_id);

        }
        assertEquals(customerObject.getById(dbConnection, customer_id).getCustomer_id(), 0);
        assertEquals(customerAddressObject.getById(dbConnection, address_id).getAddress_id(), 0);
    }

    @Test
    @DisplayName("Get customer by ids")
    void getCustomerByCustomerIds() throws SQLException {
        List<Integer> ids =  new ArrayList<>();
        ids.add(1);
        ids.add(2);
        CustomerObject customerObject = new CustomerObject();
        List<Customer> customers= customerObject.getByIds(dbConnection, ids);
        for (Customer customer : customers){
            assertEquals(customers.size(), ids.size());
            System.out.println(customer);
        }
    }

    @Test
    @DisplayName("Get customer address by ids")
    void getCustomerAddressByIds() throws SQLException {
        List<Integer> ids =  new ArrayList<>();
        ids.add(1);
        ids.add(2);
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        List<CustomerAddress> addresses= customerAddressObject.getByIds(dbConnection, ids);
        for (CustomerAddress address : addresses){
            assertEquals(addresses.size(), ids.size());
            System.out.println(address);
        }
    }

    @Test
    @DisplayName("Get addresses and customers count")
    void getAllCount() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        System.out.println("Customers count is " + customerObject.getAllRecordsCount(dbConnection));
        System.out.println("Addresses count is " + customerAddressObject.getAllRecordsCount(dbConnection));
    }

    @Test
    @DisplayName("Get random customer")
    void getRandomCustomer() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        Customer randomCustomer = customerObject.getByRandomId(dbConnection);

        System.out.println(randomCustomer);
    }

    @Test
    @DisplayName("Get random address")
    void getRandomAddress() throws SQLException {
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        CustomerAddress randomAddress = customerAddressObject.getByRandomId(dbConnection);

        System.out.println(randomAddress);
    }

    @Test
    @DisplayName("Get random 5 addresses")
    void getRandomAddressesIds() throws SQLException {
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        List<Integer> randIds =  customerAddressObject.getRandomIds(dbConnection, 5);

        System.out.println(randIds);
    }

    @Test
    @DisplayName("Get random 5 customers")
    void getRandomCustomerIds() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        List<Integer> randIds =  customerObject.getRandomIds(dbConnection, 5);

        System.out.println(randIds);
    }
}
