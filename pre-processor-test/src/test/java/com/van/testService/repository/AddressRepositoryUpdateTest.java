package com.van.testService.repository;

import com.van.processor.exeption.InvalidDataAccessApiUsageException;
import com.van.processor.exeption.UncategorizedSQLException;
import com.van.processor.generated.GenRepository;
import com.van.processor.jdbc.statement.PreparedStatementFactory;
import com.van.testService.Application;
import com.van.testService.model.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.van.testService.common.Utils.getCurrentPrbrId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressRepositoryUpdateTest extends AbstractRepositoryTest{

	private static final Long TEST_ID_CREATE = 130000L;
	private static final Long TEST_ID_CREATE_2 = 130002L;

	private static final Long TEST_ID_UPDATE = 5L;
	private static final Long TEST_ID_UPDATE_2 = 15L;
	private static final Long TEST_ID_UPDATE_3 = 17L;
	private static final Long TEST_ID_UPDATE_NEW = 23L;

	private static final Long TEST_ID_DELETE_1 = 18L;
	private static final Long TEST_ID_DELETE_NOT_EXEST = -1L;
	private static final Long TEST_ID_DELETE_2 = 1L;

	@Autowired
	private GenRepository<Address, Long> repositoryForUpdate;

	@BeforeAll
	static void init() {
		AbstractRepositoryTest.init();
		AbstractRepositoryTest.setPrbrId(2L);
	}

	@Test
	public void address_create_ok() {
		List<Address> addresss = repositoryForUpdate.getAll();
		assertNotNull(addresss);
		//assertEquals(11, addresss.size());

		Address addressTestOrig = new Address();
		addressTestOrig.setId(TEST_ID_CREATE);
		addressTestOrig.setStreet("Test1Street");
		addressTestOrig.setHouseNumber("Test1HouseNumber");
		addressTestOrig.setZipCode("123234");

		Optional<Long> newId = repositoryForUpdate.create(addressTestOrig);
		assertTrue(newId.isPresent());

		addresss = repositoryForUpdate.getAll();
		assertNotNull(addresss);

		Optional<Address> fromDb = repositoryForUpdate.getById(newId.get());
		assertTrue(fromDb.isPresent());
		log.debug("fromDb = " + fromDb.toString());
		Address addressFromDb = fromDb.get();
		assertEquals(addressTestOrig, addressFromDb);

		addresss = repositoryForUpdate.getAll();
		assertNotNull(addresss);
	}

	@Test
	public void address_create_negative() {
		List<Address> addresss = repositoryForUpdate.getAll();
		assertNotNull(addresss);

		Address addressTestOrig = new Address();
		addressTestOrig.setId(TEST_ID_CREATE_2);
		addressTestOrig.setHouseNumber("Test1HouseNumber");
		addressTestOrig.setZipCode("123234");

		assertThrows(UncategorizedSQLException.class, () ->
				repositoryForUpdate.create(addressTestOrig), "NULL not allowed for column 'STREET'");

		addresss = repositoryForUpdate.getAll();
		assertNotNull(addresss);

		Optional<Address> fromDb = repositoryForUpdate.getById(TEST_ID_CREATE_2);
		assertFalse(fromDb.isPresent());
	}

	@Test
	public void address_create_in_def_prbrId() {
		Address addressTestOrig = new Address();
		addressTestOrig.setId(120000L);
		addressTestOrig.setPrbrId(13L);
		addressTestOrig.setStreet("Test1Street");
		addressTestOrig.setHouseNumber("Test1HouseNumber");
		addressTestOrig.setZipCode("123234");

		Optional<Long> newId = repositoryForUpdate.create(addressTestOrig);
		assertTrue(newId.isPresent());
		Optional<Address> fromDb = repositoryForUpdate.getById(newId.get());
		assertNotNull(fromDb);
		assertTrue(fromDb.isPresent());
		log.debug("testOrig = " + fromDb.toString());
		assertEquals(fromDb.get().getPrbrId(), getCurrentPrbrId());
	}

	@Test
	public void address_update_prbrid_unposible_ok() {
		List<Address> addresss = repositoryForUpdate.getAll();
		log.debug("addresss = " + addresss.toString());

		Optional<Address> fromDb = repositoryForUpdate.getById(TEST_ID_UPDATE);
		assertTrue(fromDb.isPresent());
		Address address = fromDb.get();
		assertNotNull(address);

		address.setStreet("Wert");
		address.setPrbrId(23L);
		Optional<Long> newId = repositoryForUpdate.update(address);
		assertTrue(newId.isPresent());

		addresss = repositoryForUpdate.getAll();
		log.debug("addresss = " + addresss.toString());

		fromDb = repositoryForUpdate.getById(TEST_ID_UPDATE);
		assertTrue(fromDb.isPresent());
		log.debug("testOrig = " + fromDb.toString());
		assertEquals(fromDb.get().getPrbrId(), getCurrentPrbrId());

		addresss = repositoryForUpdate.getAll();
		assertNotNull(addresss);
	}

	@Test
	public void address_update_id_unposible_ok() {
		Optional<Address> fromDb = repositoryForUpdate.getById(TEST_ID_UPDATE_2);
		assertTrue(fromDb.isPresent());
		Address address = fromDb.get();
		assertNotNull(address);

		address.setId(TEST_ID_UPDATE_NEW);
		Optional<Long> newId = repositoryForUpdate.update(address);
		assertFalse(newId.isPresent());

		fromDb = repositoryForUpdate.getById(TEST_ID_UPDATE_NEW);
		assertFalse(fromDb.isPresent());
	}

	@Test
	public void address_update_street_processed_ok() {
		Optional<Address> fromDb = repositoryForUpdate.getById(TEST_ID_UPDATE_3);
		assertTrue(fromDb.isPresent());
		Address address = fromDb.get();
		assertNotNull(address);

		address.setStreet("Wsssssss");
		repositoryForUpdate.update(address);

		fromDb = repositoryForUpdate.getById(TEST_ID_UPDATE_3);
		assertTrue(fromDb.isPresent());
		log.debug("testOrig = " + fromDb.toString());
		assertEquals(fromDb.get().getPrbrId(), getCurrentPrbrId());
	}

	@Test
	public void address_delete_processed_ok() {
		Optional<Address> fromDb = repositoryForUpdate.getById(TEST_ID_DELETE_1);
		assertTrue(fromDb.isPresent());
		log.debug("fromDb = " + fromDb.toString());
		assertEquals(1, repositoryForUpdate.delete(fromDb.get()));
		fromDb = repositoryForUpdate.getById(TEST_ID_DELETE_1);
		assertFalse(fromDb.isPresent());
	}

	@Test
	public void address_delete_unexist_id_ok() {
		assertEquals(0, repositoryForUpdate.deleteById(TEST_ID_DELETE_NOT_EXEST));
	}

	@Test
	public void address_delete_other_prbr_ok() {
		assertEquals(0, repositoryForUpdate.deleteById(TEST_ID_DELETE_2));
	}
}
