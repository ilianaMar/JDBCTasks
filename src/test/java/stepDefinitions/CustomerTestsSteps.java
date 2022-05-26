package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import org.estafet.helpers.DbConnectionHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.github.javafaker.Faker;
import org.estafet.models.Customer;
import org.estafet.models.CustomerAddress;
import org.estafet.objects.CustomerAddressObject;
import org.estafet.objects.CustomerObject;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTestsSteps {
    private static DbConnectionHelper dbHelper;

    static {
        try {
            dbHelper = new DbConnectionHelper(DbConnectionHelper.postgresConfData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Connection dbConnection;
    CustomerObject customerObject = new CustomerObject();
    CustomerAddressObject customerAddressObject = new CustomerAddressObject();
    List<CustomerAddress> customerAddresses = null;
    List<Customer> customers = null;
    CustomerAddress newCustomerAddress;
    Customer newCustomer;
    Faker faker;
    String message = "";

    @BeforeAll(order = 1)
    public static void startDBConnection() throws SQLException {
        dbConnection = dbHelper.startDbConnection();
        System.out.println("open db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());
    }

    @BeforeAll(order = 2)
    @After
    public static void deleteData() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        customerObject.deleteAll(dbConnection);
        customerAddressObject.deleteAll(dbConnection);
    }

    @AfterAll
    public static void stopDBConnection() throws SQLException {
        dbHelper.closeDbConnection(dbConnection);
        System.out.println("close db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());
    }

    @Given("^I create (\\d+) customers{0,1} with all mandatory fields$")
    public void iCreateCustomers(int customerNumber) {
        faker = new Faker();
        try {
            for (int i = 0; i < customerNumber; i++) {
                newCustomerAddress = CustomerAddress.builder()
                        .address(faker.address().streetName())
                        .city(faker.address().city())
                        .country(faker.address().country())
                        .state(faker.address().state())
                        .postalCode(faker.number().numberBetween(1000, 9000))
                        .build();
                int lastAddressId = customerAddressObject.save(dbConnection, newCustomerAddress);
                customerAddresses = customerAddressObject.getAll(dbConnection);
                newCustomer = Customer.builder()
                        .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()))
                        .age(faker.number().numberBetween(20, 90))
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().cellPhone())
                        .active(true)
                        .gdprSet(true)
                        .addressId(lastAddressId)
                        .build();
                customerObject.save(dbConnection, newCustomer);
            }
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
        }
    }

    @Then("I check that there are no customers without address")
    public void iCheckThatThereAreNoCustomersWithoutAddress() {
        for (CustomerAddress address : customerAddresses) {
            assertTrue(!address.getAddress().isEmpty() && !address.getAddress().isBlank());
        }
    }

    @Then("I compare customers phone and email them that are not equal")
    public void iCompareCustomersPhoneAndEmailThemThatAreNotEqual() throws SQLException {
        List<Customer> getAllCustomers = customerObject.getAll(dbConnection);

        List<String> listPhones = new ArrayList<>();
        List<String> listEmails = new ArrayList<>();
        for (Customer customer : getAllCustomers) {
            listPhones.add(customer.getPhone());
            listEmails.add(customer.getEmail());
        }
        Set<String> setPhones = new HashSet<>(listPhones);
        Set<String> setEmails = new HashSet<>(listEmails);
        assertEquals(setPhones.size(), listPhones.size());
        assertEquals(setEmails.size(), listEmails.size());
    }

    @Then("I verify that customer is created correctly")
    public void iVerifyThatCustomerIsCreatedCorrectly() throws SQLException {
        List<Customer> getAllCustomers = customerObject.getAll(dbConnection);
        List<CustomerAddress> getAllCustomerAddresses = customerAddressObject.getAll(dbConnection);
        System.out.println("99999 " + getAllCustomers);
        assertEquals(1, getAllCustomers.size());
        assertEquals(1, getAllCustomerAddresses.size());
        for (Customer customer : getAllCustomers) {
            for (CustomerAddress address : getAllCustomerAddresses) {
                assertEquals(newCustomer.getName(), customer.getName());
                assertEquals(newCustomer.getPhone(), customer.getPhone());
                assertEquals(newCustomer.getEmail(), customer.getEmail());
                assertEquals(newCustomer.getAge(), customer.getAge());
                assertEquals(newCustomer.isActive(), customer.isActive());
                assertEquals(newCustomer.isGdprSet(), customer.isGdprSet());
                assertNotNull(customer.getCreatedTime());
                assertNull(customer.getUpdatedTime());
                assertEquals(customer.getAddressId(), address.getAddressId());
                assertEquals(newCustomerAddress.getAddress(), address.getAddress());
                assertEquals(newCustomerAddress.getCountry(), address.getCountry());
                assertEquals(newCustomerAddress.getCity(), address.getCity());
                assertEquals(newCustomerAddress.getState(), address.getState());
                assertEquals(newCustomerAddress.getPostalCode(), address.getPostalCode());
            }
        }
    }

    @Given("^I create (\\d+) customers{0,1} without mandatory fields (.*)$")
    public void iCreateCustomerWithoutMandatoryFields(int customerNumber, String property) {
        faker = new Faker();
        newCustomer = Customer.builder()
                .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()))
                .age(faker.number().numberBetween(20, 90))
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .active(true)
                .gdprSet(true)
                .build();
        switch (property) {
            case "name":
                newCustomer.setName(null);
                break;
            case "address_id":
                newCustomer.setAddressId(0);
                break;
        }
        try {
            for (int i = 0; i < customerNumber; i++) {
                System.out.println(newCustomer);
                customerObject.save(dbConnection, newCustomer);
            }
        } catch (SQLException sqe) {
            System.out.println("Message = " + sqe.getMessage());
            message = sqe.getMessage();
        }
    }

    @Then("^I cannot save the customer without (.*)$")
    public void iCannotSaveTheCustomer(String property) throws SQLException {
        List<Customer> getAllCustomers = customerObject.getAll(dbConnection);
        List<CustomerAddress> getAllCustomerAddresses = customerAddressObject.getAll(dbConnection);
        assertEquals(0, getAllCustomers.size());
        assertEquals(0, getAllCustomerAddresses.size());
        assertTrue(message.contains(property));
    }

    @When("^I get random (\\d+) customers{0,1}$")
    public void iGetRandomRandomCustomers(int customerNumber) throws SQLException {
        List<CustomerAddress> randomAddressIds = customerAddressObject.getRandomIds(dbConnection, customerNumber);
        List<Integer> listIds= new ArrayList<>();
        for (CustomerAddress address : randomAddressIds) {
            System.out.println(address.getAddressId());
            listIds.add(address.getAddressId());
            customers = customerObject.getByIds(dbConnection, "address_id", listIds);
        }
    }

    @Then("^I check that (\\d+) customers{0,1} mandatory fields are not empty$")
    public void iCheckThatAllMandatoryArePopulated(int customerNumber) {
        assertEquals(customerNumber, customers.size());
        for (Customer customer : customers) {
            assertTrue(!customer.getName().isEmpty() && !customer.getName().isBlank());
            assertTrue(customer.getAge() != 0);
        }
    }

    @Given("^I create (\\d+) customer (?:address|addresses) without (.*)$")
    public void iCreateCustomerAddressesWithoutMandatoryFields(int addressNumber, String property) {
        faker = new Faker();
        newCustomerAddress = CustomerAddress.builder()
                .address(faker.address().streetName())
                .city(faker.address().city())
                .country(faker.address().country())
                .state(faker.address().state())
                .postalCode(faker.number().numberBetween(1000, 9000))
                .build();
        switch (property) {
            case "city":
                newCustomerAddress.setCity(null);
                break;
            case "country":
                newCustomerAddress.setCountry(null);
                break;
            case "postal_code":
                newCustomerAddress.setPostalCode(0);
                break;
        }

        try {
            for (int i = 0; i < addressNumber; i++) {
                customerAddressObject.save(dbConnection, newCustomerAddress);
            }
        } catch (SQLException sqe) {
            System.out.println("Message = " + sqe.getMessage());
            message = sqe.getMessage();
        }
    }

    @And("^I cannot save customer (?:address|addresses) without (.*)$")
    public void iCannotSaveTheCustomerAddressWithoutMandatoryProperties(String property) throws SQLException {
        List<CustomerAddress> getAllCustomerAddresses = customerAddressObject.getAll(dbConnection);
        assertEquals(0, getAllCustomerAddresses.size());
        assertTrue(message.contains(property));
    }

    @And("^I create (\\d+) orders{0,1} for created customers{0,1}$")
    public void iCreateOrdersForCreatedCustomers(int arg0) {
    }

    @And("I get orders for selected users")
    public void iGetOrdersForSelectedUsers() {
    }

    @Then("i verify that all mandatory fields are not null")
    public void iVerifyThatAllMandatoryFieldsAreNotNull() {
    }
}
