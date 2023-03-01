package com.van.testService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.van.testService.Application;
import com.van.testService.common.AbstractMvcControllerTest;
import com.van.testService.model.Address;
import com.van.testService.model.UserDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URLEncoder;

import static com.van.processor.common.ApiConstant.URL_DELIMITER;
import static com.van.processor.common.ApiConstant.URL_ID;
import static com.van.testService.common.TestConstant.ADDRESS_URL;
import static com.van.testService.common.TestConstant.FILTER;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class AdressMvcControllerTest extends AbstractMvcControllerTest {

	@BeforeAll
	static void init() {
		UserDetails userDetails = new UserDetails(1L);
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getDetails()).thenReturn(userDetails);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	public void addresses_ok() throws Exception {
		mockMvc.perform(
				get(ADDRESS_URL)
		).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void addresses_filter_ok() throws Exception {
		mockMvc.perform(
				get(ADDRESS_URL + FILTER + URLEncoder.encode(" street = 'Street_prbr_1_id_4' ", "UTF-8"))
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("[{\"entityId\":4,\"street\":\"Street_prbr_1_id_4\",\"houseNumber\":\"67\",\"zipCode\":\"07079\",\"houses\":null}]"));
	}

	@Test
	public void addresses_filter_not_ok() throws Exception {
		mockMvc.perform(
				get(ADDRESS_URL + FILTER + URLEncoder.encode(" street  'Avert' ", "UTF-8"))
		).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addresses_filter_2_ok() throws Exception {
		mockMvc.perform(
				get(ADDRESS_URL + FILTER + URLEncoder.encode(" street = 'Street_prbr_1_id_3' | street = 'Street_prbr_1_id_4' ", "UTF-8"))
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void addresses_create_ok() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		mockMvc.perform(
				get(ADDRESS_URL + FILTER + URLEncoder.encode(" street = 'Street_prbr_1_id_130001'", "UTF-8"))
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

		Address addressTestOrig = new Address();
		addressTestOrig.setId(130001L);
		addressTestOrig.setStreet("Street_prbr_1_id_130001");
		addressTestOrig.setHouseNumber("Test3HouseNumber");
		addressTestOrig.setZipCode("3333333");

		mockMvc.perform(
				post(ADDRESS_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(addressTestOrig)))
				.andDo(print())
				.andExpect(status().isCreated());

		mockMvc.perform(
				get(ADDRESS_URL + URL_DELIMITER + "130001")
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("{\"entityId\":130001,\"street\":\"Street_prbr_1_id_130001\",\"houseNumber\":\"Test3HouseNumber\",\"zipCode\":\"3333333\",\"houses\":null}"));
	}

	@Test
	public void addresses_create_negative() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		mockMvc.perform(
				get(ADDRESS_URL + FILTER + URLEncoder.encode(" street = 'Street_prbr_1_id_13'", "UTF-8"))
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		Address addressTestOrig = new Address();
		addressTestOrig.setId(13L);
		addressTestOrig.setStreet("Street_prbr_1_id_130001");
		addressTestOrig.setHouseNumber("Test3HouseNumber");
		addressTestOrig.setZipCode("3333333");

		mockMvc.perform(
				post(ADDRESS_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(addressTestOrig)))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	public void addresses_update_ok() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		//check before
		mockMvc.perform(
				get(ADDRESS_URL + URL_DELIMITER + "10")
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("{\"entityId\":10,\"street\":\"Street_prbr_1_id_10\",\"houseNumber\":\"5\",\"zipCode\":\"07092\",\"houses\":null}"));

		Address addressTestOrig = new Address();
		addressTestOrig.setId(10L);
		addressTestOrig.setStreet("Test3Street");
		addressTestOrig.setHouseNumber("Test3HouseNumber");
		addressTestOrig.setZipCode("3333333");

		//update
		mockMvc.perform(
				put(ADDRESS_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(addressTestOrig))
		).andDo(print())
				.andExpect(status().isOk());
		//check
		mockMvc.perform(
				get(ADDRESS_URL + URL_DELIMITER + "10")
		).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("{\"entityId\":10,\"street\":\"Test3Street\",\"houseNumber\":\"Test3HouseNumber\",\"zipCode\":\"3333333\",\"houses\":null}"));
	}
}

