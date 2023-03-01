package com.van.testService.service;

import com.van.processor.exeption.UncategorizedSQLException;
import com.van.processor.generated.GenService;
import com.van.testService.Application;
import com.van.testService.model.Address;
import com.van.testService.model.UserDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressServiceTest {
	@Autowired
	GenService<Address, Long> service;

	@BeforeAll
	static void init() {
		UserDetails userDetails = new UserDetails(1L);
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getDetails()).thenReturn(userDetails);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	// Collection<T> getAll(String filter) throws ParseException;
	@Test
	public void address_load_ok() {
		Collection<Address> addresss = service.getAll(null);
		assertNotNull(addresss);
		assertEquals(12, addresss.size());
	}

	//Optional<T> get(ID id);
	@Test
	public void address_get_ok() {
		Optional<Address> address = service.get(2L);
		assertTrue(address.isPresent());
	}

	//Optional<ID> create(T item);
	@Test
	public void address_create_ok() {
		Address addressTest = new Address();
		addressTest.setId(123L);
		addressTest.setHouseNumber("12");
		addressTest.setStreet("Common street");
		addressTest.setPrbrId(1L);

		Optional<Long> id = service.create(addressTest);
		assertTrue(id.isPresent());
		Optional<Address> address = service.get(id.get());
		assertTrue(address.isPresent());
	}

	//Optional<ID> create(T item);
	@Test
	public void address_create_negative() {
		Address addressTest = new Address();
		addressTest.setId(123L);
		addressTest.setHouseNumber("12");
		addressTest.setPrbrId(1L);
		assertThrows(UncategorizedSQLException.class, () ->
				service.create(addressTest), "NULL not allowed for column \"STREET\"");
	}

	//Optional<ID> update(T item);
	@Test
	public void address_update_ok() {
		Optional<Address> opAddress = service.get(1L);
		assertTrue(opAddress.isPresent());
		Address address = opAddress.get();
		address.setHouseNumber("12");
		Optional<Long> id = service.update(address);
		assertTrue(id.isPresent());
		opAddress = service.get(id.get());
		assertTrue(opAddress.isPresent());
		Address addressUpdated = opAddress.get();
		assertEquals(address, addressUpdated);
	}

	//void deleteById(ID id);
	@Test
	public void address_deleteById_ok() {
		Optional<Address> address = service.get(2L);
		assertTrue(address.isPresent());
		service.deleteById(2L);
		address = service.get(2L);
		assertFalse(address.isPresent());
	}

	//void delete(T item);
	@Test
	public void address_delete_ok() {
		Optional<Address> opAddress = service.get(3L);
		assertTrue(opAddress.isPresent());
		Address address = opAddress.get();

		service.delete(address);
		opAddress = service.get(3L);
		assertFalse(opAddress.isPresent());
	}
}
