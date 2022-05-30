package org.estafet.objects;

import org.estafet.models.Product;
import org.estafet.helpers.DatabaseDriver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductObject extends DatabaseDriver implements DAOInterface<Product> {
    private final String tableName = "public.products_inventory";

    public int save(Connection dbConnection, Product product) throws SQLException {
        String query
                = String.format("INSERT INTO %s(product_name, available_quantity, product_type, " +
                "price_without_vat, in_stock, warehouse, supplier_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", tableName);
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.setString(1, product.getProductName());
        ps.setInt(2, product.getAvailableQuantity());
        ps.setString(3, product.getProductType());
        ps.setFloat(4, product.getPriceWithoutVat());
        ps.setBoolean(5, product.isInStock());
        ps.setInt(6, product.getWarehouse());
        ps.setInt(7, product.getSupplierId());
        System.out.println("33333 " + ps);
        return insertDbTableRowData(dbConnection, ps);
    }

    public void delete(Connection dbConnection, int id) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE product_id=%s", tableName, id);
        deleteTableData(dbConnection, query);
    }

    public void deleteAll(Connection dbConnection) throws SQLException {
        String query = String.format("DELETE FROM %s", tableName);
        deleteTableData(dbConnection, query);
    }

    public List<Product> getById(Connection dbConnection, int id, String columnName) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s=%s", tableName, columnName, id);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Product.class);
    }

    public List<Product> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s IN (?)", tableName, columnName);

        String sqlIN = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));
        query = query.replace("(?)", sqlIN);

        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Product.class);
    }

    public List<Product> getAll(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s", tableName);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Product.class);
    }

    public int getAllRecordsCount(Connection dbConnection) throws SQLException {
        return 0;
    }

    public List<Product> getByRandomId(Connection dbConnection) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT 1", tableName);
        System.out.println("executing query: " + query);
        return getDbResultSet(dbConnection, query, Product.class);
    }

    public List<Product> getRandomIds(Connection dbConnection, int count) throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY random() LIMIT %s", tableName, count);
        return getDbResultSet(dbConnection, query, Product.class);
    }

    public List<Product> getProductsWithoutOrders(Connection dbConnection) throws SQLException {
        String ordersProductsTableName = "public.orders_product_quantities";
        String query = String.format("SELECT  * FROM %s \n" +
                "LEFT JOIN %s \n" +
                "ON public.products_inventory.product_id = public.orders_product_quantities.pid\n" +
                "WHERE public.orders_product_quantities.pid IS NULL\n" +
                "order by public.products_inventory.product_id", tableName, ordersProductsTableName);
        System.out.println(query);
        return getDbResultSet(dbConnection, query, Product.class);
    }
}
