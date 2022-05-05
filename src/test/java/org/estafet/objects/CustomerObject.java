package org.estafet.objects;
import org.estafet.models.CustomerModel;
import org.estafet.helpers.DbHelper;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;



public class CustomerObject implements Customer{

    public int create(CustomerModel customer) throws SQLException, IOException {
        DbHelper dbConnection = new DbHelper(DbHelper.postgresConfData);

        String query
                = "insert into public.customers(name, email, phone, age, gdpr_set, is_active"
                + "VALUES (?, ?, ?, ?, ?, ? )";
        PreparedStatement ps = dbConnection.dbConnectWithProperties(query);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.setInt(4, customer.getAge());
        ps.setBoolean(5, customer.isGdpr_set());
        ps.setBoolean(6, customer.is_active());
        int n = ps.executeUpdate();
        return n;
    }


    public void delete(int customer_id) throws SQLException {

    }


    public CustomerModel getCustomer(int id) throws SQLException {
        return null;
    }


    public List<CustomerModel> getCustomers() throws SQLException {
        return null;
    }


    public void update(CustomerModel customer) throws SQLException {

    }
}
