package org.estafet.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Column(name="name")
    String name;
    @Column(name="email")
    String email;
    @Column(name="notes")
    String notes;
    @Column(name="phone")
    String phone;
    @Column(name="reason_for_deactivation")
    String reasonForDeactivation;
    @Column(name="customer_id")
    int customerId;
    @Column(name="age")
    int age;
    @Column(name="address_id")
    int addressId;
    @Column(name="active")
    boolean active;
    @Column(name="gdpr_set")
    boolean gdprSet;
    @Column(name="created_time")
    Timestamp createdTime;
    @Column(name="updated_time")
    Timestamp updatedTime;
}
