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
import org.estafet.models.Order;
import org.estafet.models.OrderProductQuantities;
import org.estafet.models.Product;
import org.estafet.objects.*;

import java.sql.Timestamp;

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
    OrderObject orderObject = new OrderObject();
    OrdersProductsObject ordersProductsObject = new OrdersProductsObject();
    List<CustomerAddress> customerAddresses = null;
    List<Customer> customers = null;
    List<Product> products = null;
    List<Order> orders = null;
    List<OrderProductQuantities> ordersProducts = null;
    ArrayList<Integer> productIds = new ArrayList<>();
    ArrayList<Integer> customerIds = new ArrayList<>();
    ArrayList<Integer> addressIds = new ArrayList<>();
    ArrayList<Integer> orderIds = new ArrayList<>();
    List<Product> newProducts = new ArrayList<>();
    List<Customer> newCustomers = new ArrayList<>();
    List<CustomerAddress> newCustomerAddresses = new ArrayList<>();
    List<Order> newOrders = new ArrayList<>();
    List<OrderProductQuantities> newOrdersProducts = new ArrayList<>();
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
        ProductObject productObject = new ProductObject();
        OrderObject orderObject = new OrderObject();
        OrdersProductsObject ordersProductsObject = new OrdersProductsObject();
        customerObject.deleteAll(dbConnection);
        customerAddressObject.deleteAll(dbConnection);
        productObject.deleteAll(dbConnection);
        orderObject.deleteAll(dbConnection);
        ordersProductsObject.deleteAll(dbConnection);
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
                newCustomerAddresses.add(CustomerAddress.builder()
                        .address(faker.address().streetName())
                        .city(faker.address().city())
                        .country(faker.address().country())
                        .state(faker.address().state())
                        .postalCode(faker.number().numberBetween(1000, 9000))
                        .build());
                int lastAddressId = customerAddressObject.save(dbConnection, newCustomerAddresses.get(i));
                addressIds.add(lastAddressId);
                newCustomers.add(Customer.builder()
                        .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()))
                        .age(faker.number().numberBetween(20, 90))
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().cellPhone())
                        .active(true)
                        .gdprSet(true)
                        .addressId(lastAddressId)
                        .build());
                int customerId = customerObject.save(dbConnection, newCustomers.get(i));
                customerIds.add(customerId);
            }
            assertEquals(customerObject.getAllRecordsCount(dbConnection), customerNumber);
            assertEquals(customerAddressObject.getAllRecordsCount(dbConnection), customerNumber);
        } catch (SQLException sqe) {
            System.out.println("Message = " + sqe.getMessage());
        }
    }

    @Then("I check that there are no customers without address")
    public void iCheckThatThereAreNoCustomersWithoutAddress() throws SQLException {
        customerAddresses = customerAddressObject.getAll(dbConnection);
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

    @Then("^I verify that (\\d+) customers{0,1} is created correctly$")
    public void iVerifyThatCustomerIsCreatedCorrectly(int customerNumber) throws SQLException {
        customers = customerObject.getByIds(dbConnection, "customer_id", customerIds);
        customerAddresses = customerAddressObject.getByIds(dbConnection, "address_id", addressIds);
        assertEquals(customerNumber, customers.size());
        assertEquals(customerNumber, customerAddresses.size());
        for (int i = 0; i < customerNumber; i++) {
            assertEquals(newCustomers.get(i).getName(), customers.get(i).getName());
            assertEquals(newCustomers.get(i).getPhone(), customers.get(i).getPhone());
            assertEquals(newCustomers.get(i).getEmail(), customers.get(i).getEmail());
            assertEquals(newCustomers.get(i).getAge(), customers.get(i).getAge());
            assertEquals(newCustomers.get(i).isActive(), customers.get(i).isActive());
            assertEquals(newCustomers.get(i).isGdprSet(), customers.get(i).isGdprSet());
            assertEquals(newCustomers.get(i).getAddressId(), customers.get(i).getAddressId());
            assertEquals(newCustomerAddresses.get(i).getAddress(), customerAddresses.get(i).getAddress());
            assertEquals(newCustomerAddresses.get(i).getCity(), customerAddresses.get(i).getCity());
            assertEquals(newCustomerAddresses.get(i).getCountry(), customerAddresses.get(i).getCountry());
            assertEquals(newCustomerAddresses.get(i).getState(), customerAddresses.get(i).getState());
            assertEquals(newCustomerAddresses.get(i).getProvince(), customerAddresses.get(i).getProvince());
        }
    }

    @Given("^I create (\\d+) customers{0,1} without mandatory fields (.*)$")
    public void iCreateCustomerWithoutMandatoryFields(int customerNumber, String property) {
        faker = new Faker();
        try {
            for (int i = 0; i < customerNumber; i++) {
                newCustomers.add(Customer.builder()
                        .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()))
                        .age(faker.number().numberBetween(20, 90))
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().cellPhone())
                        .active(true)
                        .gdprSet(true)
                        .build());
                switch (property) {
                    case "name":
                        newCustomers.get(i).setName(null);
                        break;
                    case "address_id":
                        newCustomers.get(i).setAddressId(0);
                        break;
                }
                customerObject.save(dbConnection, newCustomers.get(i));
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

        try {
            for (int i = 0; i < addressNumber; i++) {
                newCustomerAddresses.add(CustomerAddress.builder()
                        .address(faker.address().streetName())
                        .city(faker.address().city())
                        .country(faker.address().country())
                        .state(faker.address().state())
                        .postalCode(faker.number().numberBetween(1000, 9000))
                        .build());
                switch (property) {
                    case "city":
                        newCustomerAddresses.get(i).setCity(null);
                        break;
                    case "country":
                        newCustomerAddresses.get(i).setCountry(null);
                        break;
                    case "postal_code":
                        newCustomerAddresses.get(i).setPostalCode(0);
                        break;
                }
                customerAddressObject.save(dbConnection, newCustomerAddresses.get(i));
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
    }

    @And("^I cannot save (\\d+) products{0,1} without (.*)$")
    public void iCannotSaveProductWithoutProperties(int productNumber, String property) throws SQLException {
        List<Product> getAllProducts = productObject.getAll(dbConnection);
        assertEquals(0, getAllProducts.size());
        assertTrue(message.contains(property));
    }

    @When("^I create (\\d+) orders{0,1} with all mandatory fields$")
    public void iCreateOrdersWithAllMandatoryFields(int ordersCount) throws SQLException {
        faker = new Faker();
        for (int i = 0; i < ordersCount; i++) {
            for (Integer cid : customerIds) {
                newOrders.add(Order.builder()
                        .customerId(cid)
                        .orderCompleted(true)
                        .orderPayed(true)
                        .dateOfOrder(new Timestamp(System.currentTimeMillis()))
                        .dateOrderCompleted(new Timestamp(System.currentTimeMillis()))
                        .build()
                );
            }
            int orderId = orderObject.save(dbConnection, newOrders.get(i));
            orderIds.add(orderId);
        }

        for (Integer pid : productIds) {
            for (Integer oid : orderIds) {
                newOrdersProducts.add(OrderProductQuantities.builder()
                        .orderId(oid)
                        .productId(pid)
                        .quantity(1)
                        .build()
                );
            }
        }

        for (OrderProductQuantities query : newOrdersProducts) {
            ordersProductsObject.save(dbConnection, query);
        }
    }

    @Then("I check that orders mandatory fields are filled")
    public void iCheckThatOrdersMandatoryFieldsAreFilled() throws SQLException {
        ArrayList<Integer> randomCustomerIds = new ArrayList<>();
        for (Customer customer : customers) {
            randomCustomerIds.add(customer.getCustomerId());
        }
        orders = orderObject.getByIds(dbConnection, "customer_id", randomCustomerIds);
        for (Order order : orders) {
            assertTrue(order.getId() != 0);
            assertNotNull(order.getCustomerId());
        }
    }

    @And("I get {int} random order")
    public void iGetRandomOrder(int count) throws SQLException {
        orders = orderObject.getRandomIds(dbConnection, count);
    }

    @Then("^I check that customer_id is not null$")
    public void iCheckThatCustomer_idIsNotNull() {
        for (Order order : orders) {
            assertTrue(order.getCustomerId() != 0);
        }
    }

    @Then("I check that order products exists")
    public void iCheckThatOrderProductsExists() throws SQLException {
        List<Integer> pList = new ArrayList<>();
        for (Order order : orders) {
            ordersProducts = ordersProductsObject.getById(dbConnection, order.getId(), "oid");
        }

        for (OrderProductQuantities op : ordersProducts) {
            pList.add(op.getProductId());
        }
        assertNotNull(productObject.getByIds(dbConnection, "product_id", pList));
    }

    @Then("^I verify that (\\d+) orders{0,1} is created correctly$")
    public void iVerifyThatOrdersIsCreatedCorrectly(int count) throws SQLException {
        orders = orderObject.getByIds(dbConnection, "id", orderIds);
        assertEquals(count, orders.size());
        for (int i = 0; i < count; i++) {
            assertEquals(newOrders.get(i).getCustomerId(), orders.get(i).getCustomerId());
            assertEquals(newOrders.get(i).isOrderCompleted(), orders.get(i).isOrderCompleted());
            assertEquals(newOrders.get(i).isOrderPayed(), orders.get(i).isOrderPayed());
            assertEquals(newOrders.get(i).getDateOfOrder(), orders.get(i).getDateOfOrder());
            assertEquals(newOrders.get(i).getDateOrderCompleted(), orders.get(i).getDateOrderCompleted());
        }
    }

    @Given("^I create (\\d+) orders{0,1} without (.*)$")
    public void iCreateOrderWithoutMandatoryProperties(int count, String property) {
        faker = new Faker();
        try {
            for (int i = 0; i < count; i++) {
                for (Integer cid : customerIds) {
                    newOrders.add(Order.builder()
                            .customerId(cid)
                            .orderCompleted(true)
                            .orderPayed(true)
                            .dateOfOrder(new Timestamp(System.currentTimeMillis()))
                            .dateOrderCompleted(new Timestamp(System.currentTimeMillis()))
                            .build()
                    );
                    for (Integer pid : productIds) {
                        newOrdersProducts.add(OrderProductQuantities.builder()
                                .productId(pid)
                                .quantity(1)
                                .build()
                        );
                    }

                    switch (property) {
                        case "customer_id":
                            newOrders.get(i).setCustomerId(-1);
                            break;
                        case "oid":
                            newOrders.get(i).setCustomerId(cid);
                            newOrdersProducts.get(i).setOrderId(-1);
                            break;
                        case "pid":
                            newOrders.get(i).setCustomerId(cid);
                            newOrdersProducts.get(i).setProductId(-1);
                            break;
                    }
                }

                orderObject.save(dbConnection, newOrders.get(i));
                ordersProductsObject.save(dbConnection, newOrdersProducts.get(i));
            }
        } catch (SQLException sqe) {
            System.out.println("Message = " + sqe.getMessage());
            message = sqe.getMessage();
        }
    }

    @And("I cannot save (\\d+) orders{0,1} without (.*)$")
    public void iCannotSaveOrderWithoutMandatoryProperties(int count, String property) {
        assertTrue(message.contains(property));
    }
}
