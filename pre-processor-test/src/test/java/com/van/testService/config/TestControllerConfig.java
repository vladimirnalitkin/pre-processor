package com.van.testService.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.van.testService.common.TestConstant.BASE_TEST_URL;
import static com.van.testService.common.TestConstant.TEST_1_URL;

@TestConfiguration
@RestController
@RequestMapping(BASE_TEST_URL)
public class TestControllerConfig {
    @GetMapping(TEST_1_URL)
    public String getRequestParam(@RequestParam(value = "filter", required = false) String filter) throws UnsupportedEncodingException {
        return URLDecoder.decode(filter, "UTF-8");
    }
}