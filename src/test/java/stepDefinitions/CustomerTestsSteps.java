package stepDefinitions;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import org.estafet.helpers.DbHelper;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import com.github.javafaker.Faker;
import org.estafet.models.CustomerAddress;
import org.estafet.objects.CustomerAddressObject;
import org.estafet.objects.CustomerObject;
import org.estafet.models.Customer;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTestsSteps {
    private static DbHelper dbHelper;

    static {
        try {
            dbHelper = new DbHelper(DbHelper.postgresConfData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Connection dbConnection;
    CustomerObject customerObject = new CustomerObject();
    CustomerAddressObject customerAddressObject = new CustomerAddressObject();
    List<CustomerAddress> customerAddresses = null;
    CustomerAddress newCustomerAddress;
    Customer newCustomer;
    Faker faker;

    @BeforeAll(order=1)
    public static void startDBConnection() throws SQLException {
        dbConnection = dbHelper.startDbConnection();
        System.out.println("open db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());
    }

    @BeforeAll(order=2)
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

    @Given("^I create (\\d+) (customer|customers) with all mandatory fields$")
    public void iUseSimpleString(int customerNumber, String string) throws SQLException {
        faker = new Faker();
        for (int i= 0; i < customerNumber; i++){
            newCustomerAddress = CustomerAddress.builder()
                    .address(faker.address().streetName())
                    .city(faker.address().city())
                    .country(faker.address().country())
                    .state(faker.address().state())
                    .postal_code(faker.number().numberBetween(1000,9000))
                    .build();
            customerAddressObject.save(dbConnection, newCustomerAddress);
            customerAddresses = customerAddressObject.getAll(dbConnection);
            CustomerAddress lastCustomerAddress = customerAddresses.get(customerAddresses.size()-1);
            newCustomer = Customer.builder()
                    .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()) )
                    .age(faker.number().numberBetween(20,90))
                    .email(faker.internet().emailAddress())
                    .phone(faker.phoneNumber().cellPhone())
                    .is_active(true)
                    .gdpr_set(true)
                    .address_id(lastCustomerAddress.getAddress_id())
                    .build();
            customerObject.save(dbConnection, newCustomer);
        }
    }

    @Then("I check that  there are no customers without address")
    public void iCheckThatThereAreNoCustomersWithoutAddress() {
        for(CustomerAddress address : customerAddresses){
            System.out.println(address.getAddress());
            assertTrue(!address.getAddress().isEmpty() && !address.getAddress().isBlank());
        }
    }

    @Then("I compare customers phone and email them that are not equal")
    public void iCompareCustomersPhoneAndEmailThemThatAreNotEqual() throws SQLException {
        List<Customer> getAllCustomers = customerObject.getAll(dbConnection);

        for (int i = 0; i < getAllCustomers.size() - 1; ++i){
            assertNotEquals(getAllCustomers.get(i).getPhone(), getAllCustomers.get(i+1).getPhone());
            assertNotEquals(getAllCustomers.get(i).getEmail(), getAllCustomers.get(i+1).getEmail());
        }
    }

    @Then("I verify that customer is created correctly")
    public void iVerifyThatCustomerIsCreatedCorrectly() throws SQLException {
        List<Customer> getAllCustomers = customerObject.getAll(dbConnection);
        List<CustomerAddress> getAllCustomerAddresses = customerAddressObject.getAll(dbConnection);
        assertEquals(1, getAllCustomers.size());
        assertEquals(1, getAllCustomerAddresses.size());
        System.out.println(newCustomer.getName());
        for (Customer customer: getAllCustomers){
            for(CustomerAddress address : getAllCustomerAddresses){
                assertEquals(newCustomer.getName(), customer.getName());
                assertEquals(newCustomer.getPhone(), customer.getPhone());
                assertEquals(newCustomer.getEmail(), customer.getEmail());
                assertEquals(newCustomer.getAge(), customer.getAge());
                assertEquals(newCustomer.is_active(), customer.is_active());
                assertEquals(newCustomer.isGdpr_set(), customer.isGdpr_set());
                assertNotNull(customer.getCreated_time());
                assertNull(customer.getUpdated_time());
                assertEquals(customer.getAddress_id(), address.getAddress_id());
                assertEquals(newCustomerAddress.getAddress(), address.getAddress());
                assertEquals(newCustomerAddress.getCountry(), address.getCountry());
                assertEquals(newCustomerAddress.getCity(), address.getCity());
                assertEquals(newCustomerAddress.getState(), address.getState());
                assertEquals(newCustomerAddress.getPostal_code(), address.getPostal_code());
                assertEquals(newCustomerAddress.getProvince(), address.getProvince());
            }
        }
    }

    @Given("^I create (\\d+) (customer|customers) without mandatory fields$")
    public void iCreateCustomerWithoutMandatoryFields(int customerNumber, String string) throws SQLException {
        faker = new Faker();
        for (int i= 0; i < customerNumber; i++){
            newCustomerAddress = CustomerAddress.builder()
                    .address(faker.address().streetName())
                    .state(faker.address().state())
                    .build();
            customerAddressObject.save(dbConnection, newCustomerAddress);
            newCustomer = Customer.builder()
                    .age(faker.number().numberBetween(20,90))
                    .email(faker.internet().emailAddress())
                    .phone(faker.phoneNumber().cellPhone())
                    .build();
            customerObject.save(dbConnection, newCustomer);
        }

    }

    @Then("I cannot save the customer")
    public void iCannotSaveTheCustomer() throws SQLException {
        List<Customer> getAllCustomers = customerObject.getAll(dbConnection);
        List<CustomerAddress> getAllCustomerAddresses = customerAddressObject.getAll(dbConnection);
        assertEquals(0, getAllCustomers.size());
        assertEquals(0, getAllCustomerAddresses.size());

    }
}
