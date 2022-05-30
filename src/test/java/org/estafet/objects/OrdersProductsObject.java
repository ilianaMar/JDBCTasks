package org.estafet.objects;

import org.estafet.helpers.DatabaseDriver;
import org.estafet.models.Order;
import org.estafet.models.OrderProductQuantities;
import org.estafet.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersProductsObject extends DatabaseDriver implements DAOInterface<OrderProductQuantities> {
    private final String ordersProductsTableName = "public.orders_product_quantities";

    public int save(Connection dbConnection, OrderProductQuantities orderProductQuantities) throws SQLException {
        String query
                = String.format("INSERT INTO %s(pid, oid, quantity) VALUES (?, ?, ?)", ordersProductsTableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, orderProductQuantities.getProductId());
        ps.setInt(2, orderProductQuantities.getOrderId());
        ps.setInt(3, orderProductQuantities.getQuantity());
        System.out.println("33333 " + ps);
        return insertDbTableRowData(dbConnection, ps);
    }

    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE id=%s", ordersProductsTableName, id);
        deleteTableData(dbConnection, query);
    }

    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", ordersProductsTableName);
        deleteTableData(dbConnection, query);
    }

    public List<OrderProductQuantities> getById(Connection dbConnection, int id, String columnName) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s=%s", ordersProductsTableName, columnName, id);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, OrderProductQuantities.class);
    }

    public List<OrderProductQuantities> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s IN (?)", ordersProductsTableName, columnName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);

        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, OrderProductQuantities.class);
    }

    public List<OrderProductQuantities> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", ordersProductsTableName);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, OrderProductQuantities.class);
    }

    public int getAllRecordsCount(Connection dbConnection) throws SQLException {
        return 0;
    }

    public List<OrderProductQuantities> getByRandomId(Connection dbConnection) throws SQLException {
        return null;
    }

    public List<OrderProductQuantities> getRandomIds(Connection dbConnection, int count) throws SQLException {
        return null;
    }
}
