package org.estafet.objects;

import org.estafet.models.Customer;
import org.estafet.models.CustomerAddress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.estafet.helpers.DatabaseDriver;

public class CustomerAddressObject extends DatabaseDriver implements DAOInterface<CustomerAddress> {
    private final String tableName = "public.customers_addresses";

    public List<CustomerAddress> getRandomRecords(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s order by random() limit 5;", tableName);
        System.out.println("executing query:" + query);
        return getDbTableData(dbConnection, query, CustomerAddress.class);
    }

    public int save(Connection dbConnection, CustomerAddress address) throws SQLException {
        String query
                = String.format("INSERT INTO %s(address, city, province, state, country, postal_code)" +
                "VALUES (?, ?, ?, ?, ?, ? )", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, address.getAddress());
        ps.setString(2, address.getCity());
        ps.setString(3, address.getProvince());
        ps.setString(4, address.getState());
        ps.setString(5, address.getCountry());
        ps.setInt(6, address.getPostalCode());
        return insertDbTableRowData(dbConnection, ps);
    }

    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE address_id=%s", tableName, id);
        deleteTableData(dbConnection, query);
    }

    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", tableName);
        deleteTableData(dbConnection, query);
    }

    public List<CustomerAddress> getById(Connection dbConnection, int id) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE address_id=%s", tableName, id);

        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, CustomerAddress.class);
    }

    public List<CustomerAddress> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s IN (?)", tableName, columnName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);

        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, CustomerAddress.class);
    }

    public List<CustomerAddress> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", tableName);
//        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, CustomerAddress.class);
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

    public List<CustomerAddress> getByRandomId(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT 1", tableName);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, CustomerAddress.class);
    }

    public List<CustomerAddress> getRandomIds(Connection dbConnection, int count) throws SQLException {
        String query = String.format("SELECT address_id FROM %s ORDER BY random() LIMIT %s", tableName, count);
//        PreparedStatement ps = dbConnection.prepareStatement(query);
//        ResultSet rs = ps.executeQuery();
//        List<Integer> ids = new ArrayList<>();
//        while (rs.next()) {
//            ids.add(rs.getInt("address_id"));
//        }
//        return ids;

        return getDbResultSet(dbConnection, query, CustomerAddress.class);
    }
}
