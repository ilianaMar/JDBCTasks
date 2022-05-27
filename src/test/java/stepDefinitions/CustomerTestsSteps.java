package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.bs.A;
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
import org.estafet.models.Product;
import org.estafet.objects.CustomerAddressObject;
import org.estafet.objects.CustomerObject;
import org.estafet.objects.ProductObject;

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
    ProductObject productObject = new ProductObject();
    List<CustomerAddress> customerAddresses = null;
    List<Customer> customers = null;
    List<Product> products = null;
    ArrayList<Integer> productIds = new ArrayList<>();
    CustomerAddress newCustomerAddress;
    Customer newCustomer;
    List<Product> newProducts = new ArrayList<>();
    Faker faker;
    String message = "";

    @BeforeAll(order = 1)
    public static void startDBConnection() throws SQLException {
        dbConnection = dbHelper.startDbConnection();
        System.out.println("open db connection");
        System.out.println("DB connection is closed = " + dbConnection.isClosed());
    }

    @BeforeAll(order = 2)
//    @After
    public static void deleteData() throws SQLException {
        CustomerObject customerObject = new CustomerObject();
        CustomerAddressObject customerAddressObject = new CustomerAddressObject();
        ProductObject productObject = new ProductObject();
        customerObject.deleteAll(dbConnection);
        customerAddressObject.deleteAll(dbConnection);
        productObject.deleteAll(dbConnection);
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
        List<Integer> listIds = new ArrayList<>();
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

    @Given("^I create (\\d+) products{0,1} with all mandatory fields$")
    public void iCreateProducts(int productNumber) {
        faker = new Faker();
        try {
            for (int i = 0; i < productNumber; i++) {
                newProducts.add(Product.builder()
                        .productName(faker.commerce().productName())
                        .productType(String.format("product%s", faker.number().numberBetween(10, 100)))
                        .availableQuantity(faker.number().numberBetween(10, 100))
                        .priceWithoutVat(Float.parseFloat(faker.commerce().price()))
                        .inStock(true)
                        .warehouse(faker.number().numberBetween(10, 100))
                        .supplierId(faker.number().numberBetween(1, 24))
                        .build());
                int productId = productObject.save(dbConnection, newProducts.get(i));
                productIds.add(productId);
            }
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
        }
    }

    @When("^I check that (\\d+) products{0,1} is created correctly$")
    public void iCheckThatProductIsCreatedCorrectly(int productNumber) throws SQLException {
        products = productObject.getByIds(dbConnection, "product_id", productIds);

        for (int i = 0; i < productNumber; i++) {
            double priceVat = 1.2 * newProducts.get(i).getPriceWithoutVat();
            assertEquals(newProducts.get(i).getProductName(), products.get(i).getProductName());
            assertEquals(newProducts.get(i).getProductType(), products.get(i).getProductType());
            assertEquals(newProducts.get(i).getAvailableQuantity(), products.get(i).getAvailableQuantity());
            assertEquals(newProducts.get(i).getWarehouse(), products.get(i).getWarehouse());
            assertEquals(newProducts.get(i).getPriceWithoutVat(), products.get(i).getPriceWithoutVat());
            assertEquals(Float.parseFloat(String.valueOf(String.format("%.2f", priceVat))),
                    products.get(i).getPriceWithVat());
            assertEquals(newProducts.get(i).getSupplierId(), products.get(i).getSupplierId());
        }
    }

    @Then("^I check that no orders are related with (\\d+) products{0,1}$")
    public void iCheckThatNoOrdersAreRelatedWithThisProduct(int productNumber) throws SQLException {
        products = productObject.getProductsWithoutOrders(dbConnection);
        System.out.println(products);
        for (int i = 0; i < productNumber; i++) {
            assertEquals(products.get(i).getProductId(), productIds.get(i));
        }
    }

    @Given("^I create (\\d+) products{0,1} without (.*)$")
    public void iCreateProductWithoutName(int productNumber, String property) {
        faker = new Faker();
        try {
            for (int i = 0; i < productNumber; i++) {
                newProducts.add(Product.builder()
                        .productName(faker.commerce().productName())
                        .productType(String.format("product%s", faker.number().numberBetween(10, 100)))
                        .availableQuantity(faker.number().numberBetween(10, 100))
                        .priceWithoutVat(Float.parseFloat(faker.commerce().price()))
                        .inStock(true)
                        .warehouse(faker.number().numberBetween(10, 100))
                        .supplierId(faker.number().numberBetween(1, 24))
                        .build());
                switch (property) {
                    case "product_name":
                        newProducts.get(i).setProductName(null);
                        break;
                    case "product_type":
                        newProducts.get(i).setProductType(null);
                        break;
                    case "available_quantity":
                        newProducts.get(i).setAvailableQuantity(-1);
                        break;
                    case "price_without_vat":
                        newProducts.get(i).setPriceWithoutVat(-1);
                        break;
                    case "warehouse":
                        newProducts.get(i).setWarehouse(-1);
                        break;
                    case "supplier_id":
                        newProducts.get(i).setSupplierId(-1);
                        break;
                }

                productObject.save(dbConnection, newProducts.get(i));
            }
        } catch (SQLException sqe) {
            System.out.println("Message = " + sqe.getMessage());
            message = sqe.getMessage();
        }

        System.out.println(message);

    }

    @And("^I cannot save (\\d+) products{0,1} without (.*)$")
    public void iCannotSaveProductWithoutProperties(int productNumber, String property) throws SQLException {
        List<Product> getAllProducts = productObject.getAll(dbConnection);
        assertEquals(0, getAllProducts.size());
        assertTrue(message.contains(property));
    }
}
