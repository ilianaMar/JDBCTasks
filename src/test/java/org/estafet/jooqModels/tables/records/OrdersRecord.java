/*
 * This file is generated by jOOQ.
 */
package org.estafet.jooqModels.tables.records;


import java.time.OffsetDateTime;

import org.estafet.jooqModels.tables.Orders;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OrdersRecord extends UpdatableRecordImpl<OrdersRecord> implements Record6<Integer, Integer, Boolean, Boolean, OffsetDateTime, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.orders.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.orders.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.orders.customer_id</code>.
     */
    public void setCustomerId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.orders.customer_id</code>.
     */
    public Integer getCustomerId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.orders.order_completed</code>.
     */
    public void setOrderCompleted(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.orders.order_completed</code>.
     */
    public Boolean getOrderCompleted() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>public.orders.order_payed</code>.
     */
    public void setOrderPayed(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.orders.order_payed</code>.
     */
    public Boolean getOrderPayed() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>public.orders.date_of_order</code>.
     */
    public void setDateOfOrder(OffsetDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.orders.date_of_order</code>.
     */
    public OffsetDateTime getDateOfOrder() {
        return (OffsetDateTime) get(4);
    }

    /**
     * Setter for <code>public.orders.date_order_completed</code>.
     */
    public void setDateOrderCompleted(OffsetDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.orders.date_order_completed</code>.
     */
    public OffsetDateTime getDateOrderCompleted() {
        return (OffsetDateTime) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, Integer, Boolean, Boolean, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Integer, Integer, Boolean, Boolean, OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Orders.ORDERS.ID;
    }

    @Override
    public Field<Integer> field2() {
        return Orders.ORDERS.CUSTOMER_ID;
    }

    @Override
    public Field<Boolean> field3() {
        return Orders.ORDERS.ORDER_COMPLETED;
    }

    @Override
    public Field<Boolean> field4() {
        return Orders.ORDERS.ORDER_PAYED;
    }

    @Override
    public Field<OffsetDateTime> field5() {
        return Orders.ORDERS.DATE_OF_ORDER;
    }

    @Override
    public Field<OffsetDateTime> field6() {
        return Orders.ORDERS.DATE_ORDER_COMPLETED;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getCustomerId();
    }

    @Override
    public Boolean component3() {
        return getOrderCompleted();
    }

    @Override
    public Boolean component4() {
        return getOrderPayed();
    }

    @Override
    public OffsetDateTime component5() {
        return getDateOfOrder();
    }

    @Override
    public OffsetDateTime component6() {
        return getDateOrderCompleted();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getCustomerId();
    }

    @Override
    public Boolean value3() {
        return getOrderCompleted();
    }

    @Override
    public Boolean value4() {
        return getOrderPayed();
    }

    @Override
    public OffsetDateTime value5() {
        return getDateOfOrder();
    }

    @Override
    public OffsetDateTime value6() {
        return getDateOrderCompleted();
    }

    @Override
    public OrdersRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public OrdersRecord value2(Integer value) {
        setCustomerId(value);
        return this;
    }

    @Override
    public OrdersRecord value3(Boolean value) {
        setOrderCompleted(value);
        return this;
    }

    @Override
    public OrdersRecord value4(Boolean value) {
        setOrderPayed(value);
        return this;
    }

    @Override
    public OrdersRecord value5(OffsetDateTime value) {
        setDateOfOrder(value);
        return this;
    }

    @Override
    public OrdersRecord value6(OffsetDateTime value) {
        setDateOrderCompleted(value);
        return this;
    }

    @Override
    public OrdersRecord values(Integer value1, Integer value2, Boolean value3, Boolean value4, OffsetDateTime value5, OffsetDateTime value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OrdersRecord
     */
    public OrdersRecord() {
        super(Orders.ORDERS);
    }

    /**
     * Create a detached, initialised OrdersRecord
     */
    public OrdersRecord(Integer id, Integer customerId, Boolean orderCompleted, Boolean orderPayed, OffsetDateTime dateOfOrder, OffsetDateTime dateOrderCompleted) {
        super(Orders.ORDERS);

        setId(id);
        setCustomerId(customerId);
        setOrderCompleted(orderCompleted);
        setOrderPayed(orderPayed);
        setDateOfOrder(dateOfOrder);
        setDateOrderCompleted(dateOrderCompleted);
    }
}
