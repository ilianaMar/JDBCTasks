package org.estafet.models;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddress {
    String address, city, province, state, country;
    int postal_code, address_id;
}