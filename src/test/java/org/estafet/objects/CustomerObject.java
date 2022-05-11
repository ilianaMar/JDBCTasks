package org.estafet.objects;
import org.estafet.models.Customer;
import org.estafet.models.CustomerAddress;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class CustomerObject implements DAOInterface<Customer> {
    private final String tableName = "public.customers";

    public void save(Connection dbConnection, Customer customer) throws SQLException {
        String query
                = String.format("INSERT INTO %s(name, email, phone, age, gdpr_set, is_active, address_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.setInt(4, customer.getAge());
        ps.setBoolean(5, customer.isGdpr_set());
        ps.setBoolean(6, customer.is_active());
        ps.setInt(7, customer.getAddress_id());
        ps.executeUpdate();
    }


//    delete - deletes the record from the database
    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE customer_id=?", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

//    deleteAll - deletes all records in the table
    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.executeUpdate();
    }


//    getById - get a single record from the table by id
    public Customer getById(Connection dbConnection, int id) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE customer_id=?", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Customer.CustomerBuilder customer = Customer.builder();
        while (rs.next()) {
            customer.customer_id(rs.getInt("customer_id"))
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
                    .address_id(rs.getInt("address_id"));
        }
        return customer.build();
    }

    public HashMap<Customer, CustomerAddress> getAllCustomerAddressDataById(Connection dbConnection, int id)  throws SQLException{
        String query = "SELECT customer_id,name, email, phone, age, public.customers.address_id, " +
                "address, city, province, state, postal_code, country\n" +
                "FROM public.customers\n" +
                "INNER JOIN public.customers_addresses\n" +
                "ON public.customers.address_id = public.customers_addresses.address_id\n" +
                "WHERE customer_id=?";
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        HashMap<Customer, CustomerAddress> customerData = new HashMap<>();

        while (rs.next()) {
            Customer customer = Customer.builder()
                    .customer_id(rs.getInt("customer_id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .age(rs.getInt("age"))
                    .phone(rs.getString("phone"))
                    .build();

            CustomerAddress customerAddress = CustomerAddress.builder()
                    .address_id(rs.getInt("address_id"))
                    .country(rs.getString("country"))
                    .city(rs.getString("city"))
                    .province(rs.getString("province"))
                    .state(rs.getString("state"))
                    .address(rs.getString("address"))
                    .postal_code(rs.getInt("postal_code"))
                    .build();
            customerData.put(customer, customerAddress);
        }
        return customerData;
    }

//    getByIds - get a list of records by list of ids
    public List<Customer> getByIds(Connection dbConnection, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE customer_id IN (?)", tableName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);
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

    public List<Customer> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", tableName);
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

    public int getAllRecordsCount(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT COUNT(*) as count FROM %s", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public Customer getByRandomId(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT 1", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        Customer.CustomerBuilder customer = Customer.builder();
        while (rs.next()) {
            customer.customer_id(rs.getInt("customer_id"))
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
                    .address_id(rs.getInt("address_id"));
        }
        return customer.build();
    }

    public List<Integer> getRandomIds(Connection dbConnection, int count) throws SQLException {
        String query = String.format("SELECT customer_id FROM %s ORDER BY random() LIMIT %s", tableName, count);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (rs.next()){
            ids.add(rs.getInt("customer_id"));
        }
        return ids;
    }
}
