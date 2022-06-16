/*
 * This file is generated by jOOQ.
 */
package org.estafet.jooqModels;


import org.estafet.jooqModels.tables.Customers;
import org.estafet.jooqModels.tables.CustomersAddresses;
import org.estafet.jooqModels.tables.Orders;
import org.estafet.jooqModels.tables.OrdersProductQuantities;
import org.estafet.jooqModels.tables.ProductsInventory;
import org.estafet.jooqModels.tables.Suppliers;
import org.estafet.jooqModels.tables.SuppliersAddresses;


/**
 * Convenience access to all tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {


    /**
     * The table <code>public.customers</code>.
     */
    public static final Customers CUSTOMERS = Customers.CUSTOMERS;


    /**
     * The table <code>public.customers_addresses</code>.
     */
    public static final CustomersAddresses CUSTOMERS_ADDRESSES = CustomersAddresses.CUSTOMERS_ADDRESSES;

    /**
     * The table <code>public.orders</code>.
     */
    public static final Orders ORDERS = Orders.ORDERS;

    /**
     * The table <code>public.orders_product_quantities</code>.
     */
    public static final OrdersProductQuantities ORDERS_PRODUCT_QUANTITIES = OrdersProductQuantities.ORDERS_PRODUCT_QUANTITIES;

    /**
     * The table <code>public.products_inventory</code>.
     */
    public static final ProductsInventory PRODUCTS_INVENTORY = ProductsInventory.PRODUCTS_INVENTORY;


    /**
     * The table <code>public.suppliers</code>.
     */
    public static final Suppliers SUPPLIERS = Suppliers.SUPPLIERS;

    /**
     * The table <code>public.suppliers_addresses</code>.
     */
    public static final SuppliersAddresses SUPPLIERS_ADDRESSES = SuppliersAddresses.SUPPLIERS_ADDRESSES;
}