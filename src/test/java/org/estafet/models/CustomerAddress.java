package org.estafet.models;

import lombok.*;

@Data
@Builder
public class CustomerAddress {
    String address, city, province, state, country;
    int postal_code, address_id;
}