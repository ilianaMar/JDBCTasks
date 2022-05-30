package org.estafet.objects;

import org.estafet.helpers.DatabaseDriver;
import org.estafet.models.Order;
import org.estafet.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderObject extends DatabaseDriver implements DAOInterface<Order> {
    private final String orderTableName = "public.orders";
    private final String ordersProductsTableName = "public.orders_product_quantities";

    public int save(Connection dbConnection, Order order) throws SQLException {
        String query
                = String.format("INSERT INTO %s(customer_id, order_completed, order_payed, " +
                "date_of_order, date_order_completed) VALUES (?, ?, ?, ?, ?)", orderTableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setInt(1, order.getCustomerId());
        ps.setBoolean(2, order.isOrderPayed());
        ps.setBoolean(3, order.isOrderCompleted());
        ps.setTimestamp(4, order.getDateOrderCompleted());
        ps.setTimestamp(5, order.getDateOfOrder());
        System.out.println("33333 " + ps);
        return insertDbTableRowData(dbConnection, ps);
    }

    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE id=%s", orderTableName, id);
        deleteTableData(dbConnection, query);
    }

    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", orderTableName);
        deleteTableData(dbConnection, query);
    }

    public List<Order> getById(Connection dbConnection, int id, String columnName) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s=%s", orderTableName, columnName, id);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Order.class);
    }

    public List<Order> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s IN (?)", orderTableName, columnName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);

        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Order.class);
    }

    public List<Order> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", orderTableName);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Order.class);
    }

    public int getAllRecordsCount(Connection dbConnection) throws SQLException {
        return 0;
    }

    public List<Order> getByRandomId(Connection dbConnection) throws SQLException {
        return null;
    }

    public List<Order> getRandomIds(Connection dbConnection, int count) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT %s", orderTableName, count);
        return getDbResultSet(dbConnection, query, Order.class);
    }
}
