package org.estafet.models;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class CustomerModel {
    private String name, email , reason_for_deactivation, notes, phone;
    private int customer_id, age, address_id;
    private boolean is_active, gdpr_set;
    private Timestamp created_time, updated_time;
}
