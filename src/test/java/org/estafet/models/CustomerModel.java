package org.estafet.models;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class CustomerModel {
    String name, email , reason_for_deactivation, notes, phone;
    int customer_id, age, address_id;
    boolean is_active, gdpr_set;
    Timestamp created_time, updated_time;

    public String toString()
    {
        return "[customer_id=" + getCustomer_id() + ", name=" + getName() + ", email=" + getEmail() +
                " ,phone=" + getPhone() + " ,age=" + getAge() + " ,gdpr_set="+ isGdpr_set() + " ,is_active ="
                + is_active() + " ,created_at=" + getCreated_time() + " ,updated_at" + getUpdated_time() +
                " ,reason_for_deactivation=" + getReason_for_deactivation() + " ,notes=" + getNotes() +
                " ,address_id=" + getAddress_id() +"]";
    }
}
