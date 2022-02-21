package com.tui.proof.domain.entities.base;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {

    String street;

    String postcode;

    String city;

    String country;


}
