package com.van.processor.jdbc.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    private Long id = null;
    private Long prbrId;
    private String street;
    private String houseNumber;
    private String zipCode;
}
