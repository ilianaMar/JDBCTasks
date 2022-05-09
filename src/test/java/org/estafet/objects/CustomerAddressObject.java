package org.estafet.objects;

import org.estafet.models.CustomerAddress;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerAddressObject implements CustomerAddressInterface{
    public int save(Connection dbConnection, CustomerAddress customerAddress) throws SQLException, IOException {
        String query
                = "insert into public.customers_addresses(address, city, province, state, country, postal_code)" +
                "VALUES (?, ?, ?, ?, ?, ? )";
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, customerAddress.getAddress());
        ps.setString(2, customerAddress.getCity());
        ps.setString(3, customerAddress.getProvince());
        ps.setString(4, customerAddress.getState());
        ps.setString(5, customerAddress.getCountry());
        ps.setInt(6, customerAddress.getPostal_code());
        return ps.executeUpdate();
    }

    public List<CustomerAddress> getCustomerAddresses(Connection dbConnection) throws SQLException {
        String query = "select * from public.customers_addresses";
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
}
