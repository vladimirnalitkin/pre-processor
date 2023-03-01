package com.van.testService.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddressTest {
    @Test
    public void address_create_ok() {
        Address address = new Address();
        address.setId(123L);
        address.setPrbrId(23L);
        address.setHouseNumber("12");

        assertNotNull(address.getId());
    }
}
