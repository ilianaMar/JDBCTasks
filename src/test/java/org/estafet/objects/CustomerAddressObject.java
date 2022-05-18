package org.estafet.objects;
import org.estafet.models.CustomerAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerAddressObject implements DAOInterface<CustomerAddress>{
    private final String tableName = "public.customers_addresses";

    public void save(Connection dbConnection, CustomerAddress customerAddress) throws SQLException {
        String query
                = String.format("INSERT INTO %s(address, city, province, state, country, postal_code)" +
                "VALUES (?, ?, ?, ?, ?, ? )", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, customerAddress.getAddress());
        ps.setString(2, customerAddress.getCity());
        ps.setString(3, customerAddress.getProvince());
        ps.setString(4, customerAddress.getState());
        ps.setString(5, customerAddress.getCountry());
        ps.setInt(6, customerAddress.getPostal_code());
        ps.executeUpdate();
    }

    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE address_id=?", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.executeUpdate();
    }

    public CustomerAddress getById(Connection dbConnection, int id) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE address_id=?", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        CustomerAddress.CustomerAddressBuilder customerAddress = CustomerAddress.builder();
        while (rs.next()) {
            customerAddress.address_id(rs.getInt("address_id"))
                    .address(rs.getString("address"))
                    .city(rs.getString("city"))
                    .country(rs.getString("country"))
                    .state(rs.getString("state"))
                    .province(rs.getString("province"))
                    .postal_code(rs.getInt("postal_code"));
        }
        return customerAddress.build();
    }

    public List<CustomerAddress> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s IN (?)", tableName, columnName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<CustomerAddress> customerAddresses = new ArrayList<>();
        while (rs.next()) {
            CustomerAddress address = CustomerAddress.builder()
                    .address_id(rs.getInt("address_id"))
                    .address(rs.getString("address"))
                    .city(rs.getString("city"))
                    .country(rs.getString("country"))
                    .state(rs.getString("state"))
                    .province(rs.getString("province"))
                    .postal_code(rs.getInt("postal_code"))
                    .build();
            customerAddresses.add(address);
        }
        return customerAddresses;
    }

    public List<CustomerAddress> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<CustomerAddress> customerAddresses = new ArrayList<>();

        while (rs.next()) {
            CustomerAddress customerAddress = CustomerAddress.builder()
                    .address_id(rs.getInt("address_id"))
                    .address(rs.getString("address"))
                    .city(rs.getString("city"))
                    .country(rs.getString("country"))
                    .state(rs.getString("state"))
                    .province(rs.getString("province"))
                    .postal_code(rs.getInt("postal_code"))
                    .build();
            customerAddresses.add(customerAddress);
        }
        return customerAddresses;
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

    public CustomerAddress getByRandomId(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT 1", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        CustomerAddress.CustomerAddressBuilder address = CustomerAddress.builder();
        while (rs.next()) {
            address.address_id(rs.getInt("address_id"))
                    .address(rs.getString("address"))
                    .city(rs.getString("city"))
                    .country(rs.getString("country"))
                    .state(rs.getString("state"))
                    .province(rs.getString("province"))
                    .postal_code(rs.getInt("postal_code"));
        }
        return address.build();
    }

    public List<Integer> getRandomIds(Connection dbConnection, int count) throws SQLException {
        String query = String.format("SELECT address_id FROM %s ORDER BY random() LIMIT %s", tableName, count);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (rs.next()){
            ids.add(rs.getInt("address_id"));
        }
        return ids;
    }
}
