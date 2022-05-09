package org.estafet.objects;
import org.estafet.models.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerInterface {
    int save(Connection dbConnection, Customer customer) throws SQLException, IOException;
    void delete(int customer_id) throws SQLException;
    Customer getCustomer(int id) throws SQLException;
    List<Customer> getCustomers(Connection dbConnection) throws SQLException;
    void update(Customer customer) throws SQLException;
}


