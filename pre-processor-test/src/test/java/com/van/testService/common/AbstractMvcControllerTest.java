package com.van.testService.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AbstractMvcControllerTest {
    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

	@BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                //.addFilter(springSecurityFilterChain)
                .build();
    }
}
