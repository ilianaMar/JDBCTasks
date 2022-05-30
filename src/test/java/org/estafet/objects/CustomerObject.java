package org.estafet.objects;

import org.estafet.models.Customer;
import org.estafet.models.CustomerAddress;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.estafet.helpers.DatabaseDriver;


public class CustomerObject extends DatabaseDriver implements DAOInterface<Customer> {
    private final String tableName = "public.customers";

    public int save(Connection dbConnection, Customer customer) throws SQLException {
        String query
                = String.format("INSERT INTO %s(name, email, phone, age, gdpr_set, active, address_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.setInt(4, customer.getAge());
        ps.setBoolean(5, customer.isGdprSet());
        ps.setBoolean(6, customer.isActive());
        ps.setInt(7, customer.getAddressId());
//        System.out.println("33333 " + ps);
        return insertDbTableRowData(dbConnection, ps);
    }

    //    delete - deletes the record from the database
    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE customer_id=%s", tableName, id);
        deleteTableData(dbConnection, query);
    }

    //    deleteAll - deletes all records in the table
    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", tableName);
        deleteTableData(dbConnection, query);
    }

    //    getById - get a single record from the table by id
    public List<Customer> getById(Connection dbConnection, int id, String columnName) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s=%s", tableName, columnName, id);
//        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Customer.class);
    }

    public HashMap<Customer, CustomerAddress> getAllCustomerAddressDataById(Connection dbConnection, int id) throws SQLException {
        String query = String.format("SELECT customer_id,name, email, phone, age, public.customers.address_id, " +
                "address, city, province, state, postal_code, country\n" +
                "FROM public.customers\n" +
                "INNER JOIN public.customers_addresses\n" +
                "ON public.customers.address_id = public.customers_addresses.address_id\n" +
                "WHERE customer_id=?");
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        HashMap<Customer, CustomerAddress> customerData = new HashMap<>();

        while (rs.next()) {
            Customer customer = Customer.builder()
                    .customerId(rs.getInt("customer_id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .age(rs.getInt("age"))
                    .phone(rs.getString("phone"))
                    .build();

            CustomerAddress customerAddress = CustomerAddress.builder()
                    .addressId(rs.getInt("address_id"))
                    .country(rs.getString("country"))
                    .city(rs.getString("city"))
                    .province(rs.getString("province"))
                    .state(rs.getString("state"))
                    .address(rs.getString("address"))
                    .postalCode(rs.getInt("postal_code"))
                    .build();
            customerData.put(customer, customerAddress);
        }
        return customerData;
    }

    //    getByIds - get a list of records by list of ids
    public List<Customer> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s IN (?)", tableName, columnName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);

//        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Customer.class);
    }

    public List<Customer> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", tableName);
//        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Customer.class);
    }

    public long getAllRecordsCount(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT COUNT(*) as count FROM %s", tableName);
        return getDbTableCount(dbConnection, query);
    }

    public List<Customer> getByRandomId(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT 1", tableName);
//        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Customer.class);
    }

    public List<Customer> getRandomIds(Connection dbConnection, int count) throws SQLException {
        String query = String.format("SELECT customer_id FROM %s ORDER BY random() LIMIT %s", tableName, count);
        return getDbResultSet(dbConnection, query, Customer.class);
    }
}
