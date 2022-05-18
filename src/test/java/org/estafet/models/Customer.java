package org.estafet.models;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Data
public class Customer {
    String name, email, reason_for_deactivation, notes, phone;
    int customer_id, age, address_id;
    boolean is_active, gdpr_set;
    Timestamp created_time, updated_time;
}
