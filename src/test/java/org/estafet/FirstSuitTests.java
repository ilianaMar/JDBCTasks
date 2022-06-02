package org.estafet;

import org.estafet.models.CustomerAddress;
import org.estafet.objects.CustomerAddressObject;
import org.junit.Ignore;
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
import org.estafet.helpers.DbConnectionHelper;

import javax.persistence.Entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.*;

@DisplayName("First test suite")
public class FirstSuitTests {
    private static DbConnectionHelper dbHelper;

    static {
        try {
            dbHelper = new DbConnectionHelper(DbConnectionHelper.postgresConfData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Connection dbConnection;

    @BeforeAll
    static void beforeAll() throws SQLException {
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
                .postalCode(faker.number().numberBetween(1000, 9000))
                .build();
        customerAddressObject.save(dbConnection, newCustomerAddress);
        List<CustomerAddress> customerAddresses = customerAddressObject.getAll(dbConnection);
        CustomerAddress lastCustomerAddress = customerAddresses.get(customerAddresses.size() - 1);
        Customer newCustomer = Customer.builder()
                .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()))
                .age(faker.number().numberBetween(20, 90))
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .active(true)
                .gdprSet(true)
                .addressId(lastCustomerAddress.getAddressId())
                .build();
        customerObject.save(dbConnection, newCustomer);
        List<Customer> customers = customerObject.getAll(dbConnection);
        Customer lastCustomer = customers.get(customers.size() - 1);
        assertEquals(lastCustomer.getName(), newCustomer.getName());
        assertEquals(lastCustomer.getEmail(), newCustomer.getEmail());
        assertEquals(lastCustomer.getAge(), newCustomer.getAge());
        assertEquals(lastCustomer.getPhone(), newCustomer.getPhone());
        assertEquals(lastCustomer.getAddressId(), lastCustomerAddress.getAddressId());
        assertNotNull(lastCustomer.getCreatedTime());
        assertNull(lastCustomer.getReasonForDeactivation());
        assertNull(lastCustomer.getUpdatedTime());
        assertNull(lastCustomer.getNotes());
        assertTrue(lastCustomer.isActive());
        assertTrue(lastCustomer.isGdprSet());
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
    @Disabled
    void getFirstCustomer() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        List<Customer> customers = customerObject.getAll(dbConnection);
        Customer firstCustomer = customers.get(0);
        System.out.println(firstCustomer);
        assertEquals(1, firstCustomer.getCustomerId());
        assertEquals("iliana", firstCustomer.getName());
        assertEquals("iliana@test.com", firstCustomer.getEmail());
        assertEquals("123444", firstCustomer.getPhone());
        assertEquals(38, firstCustomer.getAge());
        assertTrue(firstCustomer.isActive());
        assertTrue(firstCustomer.isGdprSet());
    }

    @Test
    @DisplayName("Get customer and address by id")
    void getSpecificCustomerData() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        List<Customer> firstCustomer = customerObject.getById(dbConnection, 391, "customer_id");
        List<CustomerAddress> firstCustomerAddress = customerAddressObject.getById(dbConnection, 403, "address_id");
        HashMap<Customer, CustomerAddress> allCustomersData = customerObject.getAllCustomerAddressDataById(dbConnection, 391);

        for (Customer customer : allCustomersData.keySet()) {
            assertEquals(firstCustomer.get(0).getCustomerId(), customer.getCustomerId());
            assertEquals(firstCustomer.get(0).getName(), customer.getName());
            assertEquals(firstCustomer.get(0).getPhone(), customer.getPhone());
            assertEquals(firstCustomer.get(0).getEmail(), customer.getEmail());
            assertEquals(firstCustomer.get(0).getAge(), customer.getAge());
            assertEquals(firstCustomerAddress.get(0).getAddress(), allCustomersData.get(customer).getAddress());
            assertEquals(firstCustomerAddress.get(0).getAddressId(), allCustomersData.get(customer).getAddressId());
            assertEquals(firstCustomerAddress.get(0).getCity(), allCustomersData.get(customer).getCity());
            assertEquals(firstCustomerAddress.get(0).getCountry(), allCustomersData.get(customer).getCountry());
            assertEquals(firstCustomerAddress.get(0).getProvince(), allCustomersData.get(customer).getProvince());
            assertEquals(firstCustomerAddress.get(0).getState(), allCustomersData.get(customer).getState());
//            System.out.println(customer);
//            System.out.println(allCustomersData.get(customer));
        }
    }

    @Test
    @DisplayName("Delete customer and address by id")
    @Disabled
    void deleteSpecificCustomerData() throws SQLException {
        int customer_id = 11;
        int address_id = 0;

        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        HashMap<Customer, CustomerAddress> allCustomersData = customerObject.getAllCustomerAddressDataById(dbConnection, customer_id);
        for (Customer customer : allCustomersData.keySet()) {
            address_id = allCustomersData.get(customer).getAddressId();
            customerObject.delete(dbConnection, customer.getCustomerId());
            customerAddressObject.delete(dbConnection, address_id);

        }
        assertEquals(customerObject.getById(dbConnection, customer_id, "customer_id").get(0).getCustomerId(), 0);
        assertEquals(customerAddressObject.getById(dbConnection, address_id, "address_id").get(0).getAddressId(), 0);
    }

    @Test
    @DisplayName("Get customer by ids")
    void getCustomerByCustomerIds() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        CustomerObject customerObject = new CustomerObject();
        List<Customer> customers = customerObject.getByIds(dbConnection, "customer_id", ids);
        for (Customer customer : customers) {
            assertEquals(customers.size(), ids.size());
            System.out.println(customer);
        }
    }

    @Test
    @DisplayName("Get customer address by ids")
    void getCustomerAddressByIds() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        List<CustomerAddress> addresses = customerAddressObject.getByIds(dbConnection, "address_id", ids);
        for (CustomerAddress address : addresses) {
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
        List<Customer> randomCustomer = customerObject.getByRandomId(dbConnection);

        System.out.println(randomCustomer);
    }

    @Test
    @DisplayName("Get random address")
    void getRandomAddress() throws SQLException {
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        List<CustomerAddress> randomAddress = customerAddressObject.getByRandomId(dbConnection);

        System.out.println(randomAddress);
    }

    @Test
    @DisplayName("Get random 5 addresses")
    void getRandomAddressesIds() throws SQLException {
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        List<CustomerAddress> randIds = customerAddressObject.getRandomIds(dbConnection, 5);
        for (CustomerAddress address : randIds) {
            System.out.println(address.getAddressId());
        }
    }

    @Test
    @DisplayName("Get random 5 customers")
    void getRandomCustomerIds() throws SQLException {
//        CustomerObject customerObject = new CustomerObject();
//        List<Customer> randIds = customerObject.getRandomIds(dbConnection, 5);
//        for (Customer customer : randIds){
//            System.out.println(customer.getCustomerId());
//        }

        Customer customer = new Customer();
        Field[] fields = customer.getClass().getDeclaredFields();
        List<Field> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field);
        }
        Class<?> clazz = customer.getClass();
//        System.out.println(fieldNames);
        System.out.println(clazz.getSimpleName());
        System.out.println(clazz.getName());
        System.out.println(clazz.getCanonicalName());
        System.out.println(clazz.isAnnotationPresent(Entity.class));
        System.out.println(clazz.getModifiers());
    }
}
