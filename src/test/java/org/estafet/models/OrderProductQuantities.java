package org.estafet.models;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductQuantities {
    @Column(name = "id")
    int id;
    @Column(name = "pid")
    int productId;
    @Column(name = "oid")
    int orderId;
    @Column(name = "quantity")
    int quantity;
}
