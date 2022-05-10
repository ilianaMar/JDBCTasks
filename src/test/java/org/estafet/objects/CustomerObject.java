package org.estafet.objects;
import org.estafet.models.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class CustomerObject implements DAOInterface<Customer> {

    public int save(Connection dbConnection, Customer customer) throws SQLException, IOException {
        String query
                = "insert into public.customers(name, email, phone, age, gdpr_set, is_active, address_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.setInt(4, customer.getAge());
        ps.setBoolean(5, customer.isGdpr_set());
        ps.setBoolean(6, customer.is_active());
        ps.setInt(7, customer.getAddress_id());
        return ps.executeUpdate();
    }


//    delete - deletes the record from the database
    public void delete(Connection dbConnection, int customer_id) throws SQLException {

    }

//    deleteAll - deletes all records in the table
    public void deleteAll(Connection dbConnection) throws SQLException {

    }


//    getById - get a single record from the table by id
    public Customer getById(Connection dbConnection, int id) throws SQLException {
        return null;
    }

//    getByIds - get a list of records by list of ids
    public Customer getByIds(Connection dbConnection, int[] ids) throws SQLException {
        return null;
    }


    public List<Customer> getAll(Connection dbConnection) throws SQLException {
        String query = "select * from public.customers";
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Customer> customers = new ArrayList<>();

        while (rs.next()) {
            Customer customer = Customer.builder()
                    .customer_id(rs.getInt("customer_id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .phone(rs.getString("phone"))
                    .age(rs.getInt("age"))
                    .gdpr_set(rs.getBoolean("gdpr_set"))
                    .is_active(rs.getBoolean("is_active"))
                    .created_time(rs.getTimestamp("created_time"))
                    .updated_time(rs.getTimestamp("updated_time"))
                    .reason_for_deactivation(rs.getString("reason_for_deactivation"))
                    .notes(rs.getString("notes"))
                    .address_id(rs.getInt("address_id"))
                    .build();
            customers.add(customer);
        }
        return customers;
    }
}
