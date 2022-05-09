package org.estafet.objects;
import org.estafet.models.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class CustomerObject implements CustomerInterface{

    public int create(Connection dbConnection, Customer customer) throws SQLException, IOException {
        String query
                = "insert into public.customers(name, email, phone, age, gdpr_set, is_active)" +
                "VALUES (?, ?, ?, ?, ?, ? )";
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.setInt(4, customer.getAge());
        ps.setBoolean(5, customer.isGdpr_set());
        ps.setBoolean(6, customer.is_active());
        return ps.executeUpdate();
    }


    public void delete(int customer_id) throws SQLException {

    }


    public Customer getCustomer(int id) throws SQLException {
        return null;
    }


    public List<Customer> getCustomers(Connection dbConnection) throws SQLException {
        String query = "select * from public.customers";
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Customer> customers = new ArrayList<>();

        while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomer_id(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));
            customer.setAge(rs.getInt("age"));
            customer.setGdpr_set(rs.getBoolean("gdpr_set"));
            customer.set_active(rs.getBoolean("is_active"));
            customer.setCreated_time(rs.getTimestamp("created_time"));
            customer.setUpdated_time(rs.getTimestamp("updated_time"));
            customer.setReason_for_deactivation(rs.getString("reason_for_deactivation"));
            customer.setNotes(rs.getString("notes"));
            customer.setAddress_id(rs.getInt("address_id"));
            customers.add(customer);
        }
        return customers;
    }


    public void update(Customer customer) throws SQLException {

    }
}
