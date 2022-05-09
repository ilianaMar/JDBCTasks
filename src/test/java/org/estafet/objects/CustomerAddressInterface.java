package org.estafet.objects;
import org.estafet.models.CustomerAddress;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerAddressInterface {

    int save(Connection dbConnection, CustomerAddress customerAddress) throws SQLException, IOException;
    List<CustomerAddress> getCustomerAddresses(Connection dbConnection) throws SQLException;
}
