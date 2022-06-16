/*
 * This file is generated by jOOQ.
 */
package org.estafet.jooqModels.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.estafet.jooqModels.Keys;
import org.estafet.jooqModels.Public;
import org.estafet.jooqModels.tables.records.CustomersRecord;
import org.jooq.Check;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Customers extends TableImpl<CustomersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.customers</code>
     */
    public static final Customers CUSTOMERS = new Customers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CustomersRecord> getRecordType() {
        return CustomersRecord.class;
    }

    /**
     * The column <code>public.customers.customer_id</code>.
     */
    public final TableField<CustomersRecord, Integer> CUSTOMER_ID = createField(DSL.name("customer_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.customers.name</code>.
     */
    public final TableField<CustomersRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.customers.email</code>.
     */
    public final TableField<CustomersRecord, String> EMAIL = createField(DSL.name("email"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.customers.phone</code>.
     */
    public final TableField<CustomersRecord, String> PHONE = createField(DSL.name("phone"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.customers.age</code>.
     */
    public final TableField<CustomersRecord, Integer> AGE = createField(DSL.name("age"), SQLDataType.INTEGER.defaultValue(DSL.field("99", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.customers.gdpr_set</code>.
     */
    public final TableField<CustomersRecord, Boolean> GDPR_SET = createField(DSL.name("gdpr_set"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.customers.active</code>.
     */
    public final TableField<CustomersRecord, Boolean> ACTIVE = createField(DSL.name("active"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.customers.created_time</code>.
     */
    public final TableField<CustomersRecord, OffsetDateTime> CREATED_TIME = createField(DSL.name("created_time"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * The column <code>public.customers.updated_time</code>.
     */
    public final TableField<CustomersRecord, OffsetDateTime> UPDATED_TIME = createField(DSL.name("updated_time"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>public.customers.reason_for_deactivation</code>.
     */
    public final TableField<CustomersRecord, String> REASON_FOR_DEACTIVATION = createField(DSL.name("reason_for_deactivation"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.customers.notes</code>.
     */
    public final TableField<CustomersRecord, String> NOTES = createField(DSL.name("notes"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.customers.address_id</code>.
     */
    public final TableField<CustomersRecord, Integer> ADDRESS_ID = createField(DSL.name("address_id"), SQLDataType.INTEGER.nullable(false), this, "");

    private Customers(Name alias, Table<CustomersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Customers(Name alias, Table<CustomersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.customers</code> table reference
     */
    public Customers(String alias) {
        this(DSL.name(alias), CUSTOMERS);
    }

    /**
     * Create an aliased <code>public.customers</code> table reference
     */
    public Customers(Name alias) {
        this(alias, CUSTOMERS);
    }

    /**
     * Create a <code>public.customers</code> table reference
     */
    public Customers() {
        this(DSL.name("customers"), null);
    }

    public <O extends Record> Customers(Table<O> child, ForeignKey<O, CustomersRecord> key) {
        super(child, key, CUSTOMERS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<CustomersRecord, Integer> getIdentity() {
        return (Identity<CustomersRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<CustomersRecord> getPrimaryKey() {
        return Keys.CUSTOMERS_PKEY;
    }

    @Override
    public List<UniqueKey<CustomersRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CUSTOMERS_EMAIL_KEY, Keys.CUSTOMERS_ADDRESS_ID_KEY);
    }

    @Override
    public List<ForeignKey<CustomersRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CUSTOMERS__CUSTOMERS_ADDRESS_ID_FKEY);
    }

    private transient CustomersAddresses _customersAddresses;

    /**
     * Get the implicit join path to the <code>public.customers_addresses</code>
     * table.
     */
    public CustomersAddresses customersAddresses() {
        if (_customersAddresses == null)
            _customersAddresses = new CustomersAddresses(this, Keys.CUSTOMERS__CUSTOMERS_ADDRESS_ID_FKEY);

        return _customersAddresses;
    }

    @Override
    public List<Check<CustomersRecord>> getChecks() {
        return Arrays.asList(
            Internal.createCheck(this, DSL.name("customers_age_check"), "((age >= 18))", true)
        );
    }

    @Override
    public Customers as(String alias) {
        return new Customers(DSL.name(alias), this);
    }

    @Override
    public Customers as(Name alias) {
        return new Customers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Customers rename(String name) {
        return new Customers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Customers rename(Name name) {
        return new Customers(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<Integer, String, String, String, Integer, Boolean, Boolean, OffsetDateTime, OffsetDateTime, String, String, Integer> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}