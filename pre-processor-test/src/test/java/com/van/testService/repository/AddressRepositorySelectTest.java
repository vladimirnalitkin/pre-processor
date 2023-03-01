package com.van.testService.repository;

import com.van.processor.domain.RequestParam;
import com.van.processor.generated.GenRepository;
import com.van.testService.Application;
import com.van.testService.model.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressRepositorySelectTest extends AbstractRepositoryTest{
	private static final int EXPECT_COUNT = 11;

	@Autowired
	private GenRepository<Address, Long> repositoryForSelect;

	@BeforeAll
	static void init() {
		AbstractRepositoryTest.init();
	}

    @Test
    public void address_load_ok() {
        List<Address> addresss = repositoryForSelect.getAll();
        assertNotNull(addresss);
        assertEquals(EXPECT_COUNT, addresss.size());
		// test repeatable read
        addresss = repositoryForSelect.getAll();
		assertNotNull(addresss);
		assertEquals(EXPECT_COUNT, addresss.size());
    }

    @Test
    public void address_get_ok() {
		List<Address> addresss = repositoryForSelect.getAll();
		assertNotNull(addresss);
		Optional<Address>  address = repositoryForSelect.getById(1L);
		assertTrue(address.isPresent());
        assertEquals("Street_prbr_1_id_1", address.get().getStreet());
    }

	@Test
	public void address_get_not_exist_ok() {
		Optional<Address> address = repositoryForSelect.getById(-2L);
		assertFalse(address.isPresent());
	}

	@Test
	public void address_load_limit_ok() {
		List<Address> addresss = repositoryForSelect.getAll(RequestParam.of(2,3));
		assertNotNull(addresss);
		assertEquals(3,  addresss.size());
	}

	@Test
	public void address_filter_equals_ok() {
		List<Address> addresss = repositoryForSelect.getAll(RequestParam.of("zip_code = '07097'", null));
		assertNotNull(addresss);
		assertEquals(3, addresss.size());
	}

	@Test
	public void address_load_not_equals_ok() {
		List<Address> addresss = repositoryForSelect.getAll(RequestParam.of("zip_code # '07097' ", null));
		assertNotNull(addresss);
		assertEquals(8, addresss.size());
	}

	@Test
	public void address_load_not_equals_and_ok() {
		List<Address> addresss = repositoryForSelect.getAll(RequestParam.of("zip_code # '07097' & house_number = 23 ", null));
		assertNotNull(addresss);
		assertEquals(2, addresss.size());
	}
}
