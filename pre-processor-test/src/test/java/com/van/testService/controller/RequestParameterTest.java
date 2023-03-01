package com.van.testService.controller;

import com.van.testService.common.AbstractMvcControllerTest;
import com.van.testService.config.TestControllerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URLEncoder;

import static com.van.testService.common.TestConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = TestControllerConfig.class)
@ActiveProfiles("test")
public class RequestParameterTest extends AbstractMvcControllerTest {

    @Test
    public void filter_1_ok() throws Exception {
        check("name='test' & suname='we'");
    }

    @Test
    public void filter_2_ok() throws Exception {
        check("(name='test' & suname='we') | weer # 'asdasd'");
    }

    private void check(String filter_value) throws Exception {
        mockMvc.perform(
                get(BASE_TEST_URL + TEST_1_URL + FILTER + URLEncoder.encode(filter_value, "UTF-8"))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(filter_value));
    }

}
