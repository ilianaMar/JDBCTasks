/*
 * This file is generated by jOOQ.
 */
package org.estafet.jooqModels;


import java.util.Arrays;
import java.util.List;

import org.estafet.jooqModels.tables.Customers;
import org.estafet.jooqModels.tables.CustomersAddresses;
import org.estafet.jooqModels.tables.Orders;
import org.estafet.jooqModels.tables.OrdersProductQuantities;
import org.estafet.jooqModels.tables.ProductsInventory;
import org.estafet.jooqModels.tables.Suppliers;
import org.estafet.jooqModels.tables.SuppliersAddresses;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.customers</code>.
     */
    public final Customers CUSTOMERS = Customers.CUSTOMERS;

    /**
     * The table <code>public.customers_addresses</code>.
     */
    public final CustomersAddresses CUSTOMERS_ADDRESSES = CustomersAddresses.CUSTOMERS_ADDRESSES;


    /**
     * The table <code>public.orders</code>.
     */
    public final Orders ORDERS = Orders.ORDERS;

    /**
     * The table <code>public.orders_product_quantities</code>.
     */
    public final OrdersProductQuantities ORDERS_PRODUCT_QUANTITIES = OrdersProductQuantities.ORDERS_PRODUCT_QUANTITIES;

    /**
     * The table <code>public.products_inventory</code>.
     */
    public final ProductsInventory PRODUCTS_INVENTORY = ProductsInventory.PRODUCTS_INVENTORY;


    /**
     * The table <code>public.suppliers</code>.
     */
    public final Suppliers SUPPLIERS = Suppliers.SUPPLIERS;

    /**
     * The table <code>public.suppliers_addresses</code>.
     */
    public final SuppliersAddresses SUPPLIERS_ADDRESSES = SuppliersAddresses.SUPPLIERS_ADDRESSES;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Customers.CUSTOMERS,
            CustomersAddresses.CUSTOMERS_ADDRESSES,
            Orders.ORDERS,
            OrdersProductQuantities.ORDERS_PRODUCT_QUANTITIES,
            ProductsInventory.PRODUCTS_INVENTORY,
            Suppliers.SUPPLIERS,
            SuppliersAddresses.SUPPLIERS_ADDRESSES
        );
    }
}
