package org.estafet.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Column(name = "id")
    int id;
    @Column(name = "customer_id")
    int customerId;
    @Column(name = "order_completed")
    boolean orderCompleted;
    @Column(name = "order_payed")
    boolean orderPayed;
    @Column(name = "date_of_order")
    Timestamp dateOfOrder;
    @Column(name = "date_order_completed")
    Timestamp dateOrderCompleted;
}
