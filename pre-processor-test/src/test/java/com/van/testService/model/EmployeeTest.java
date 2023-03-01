package com.van.testService.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EmployeeTest {

    @Test
    public void equals_test() {
        Employee employee1 = new Employee();
        employee1.setId(120000L);
        employee1.setPrbrId(13L);
        employee1.setName("Test1Name");
        employee1.setSurname("Test1SurName");
        employee1.setFulName("Test1Name" + "Test1SurName");

        Employee employee2 = new Employee();
        employee2.setId(120000L);
        employee2.setPrbrId(13L);
        employee2.setName("Test1Name");
        employee2.setSurname("Test1SurName");
        employee2.setFulName("Test1Name" + "Test1SurName");

        assertEquals(employee1, employee2);

		Employee employee3 = new Employee();
		employee2.setId(120001L);
		employee2.setPrbrId(13L);
		employee2.setName("Test1Name");
		employee2.setSurname("Test1SurName");
		employee2.setFulName("Test1Name" + "Test1SurName");

		assertNotEquals(employee1, employee3);
    }

}
