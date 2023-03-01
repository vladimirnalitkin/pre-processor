package com.van.processor.jdbc.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Company {
    private Long id = null;
    private Long prbrId;
    private String name;
    private List<Address> addresses;
}
