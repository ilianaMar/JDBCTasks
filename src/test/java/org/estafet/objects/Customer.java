package org.estafet.objects;
import org.estafet.models.CustomerModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Customer {
    public int create(Connection dbConnection, CustomerModel customer) throws SQLException, IOException;
    public void delete(int customer_id) throws SQLException;
    public CustomerModel getCustomer(int id) throws SQLException;
    public List<CustomerModel> getCustomers(Connection dbConnection) throws SQLException;
    public void update(CustomerModel customer) throws SQLException;
}


