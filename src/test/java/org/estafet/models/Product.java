package org.estafet.models;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Column(name = "product_id")
    int productId;
    @Column(name = "product_name")
    String productName;
    @Column(name = "available_quantity")
    int availableQuantity;
    @Column(name = "product_type")
    String productType;
    @Column(name = "price_without_vat")
    float priceWithoutVat;
    @Column(name = "price_with_vat")
    float priceWithVat;
    @Column(name = "in_stock")
    boolean inStock;
    @Column(name = "warehouse")
    int warehouse;
    @Column(name = "supplier_id")
    int supplierId;
}
