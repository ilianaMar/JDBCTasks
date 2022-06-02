package org.estafet.models;

import lombok.*;
import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerAddress {
    @Column(name="address")
    String address;
    @Column(name="city")
    String city;
    @Column(name="province")
    String province;
    @Column(name="state")
    String state;
    @Column(name="country")
    String country;
    @Column(name="postal_code")
    int postalCode;
    @Column(name="address_id")
    int addressId;
}