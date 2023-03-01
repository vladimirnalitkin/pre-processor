package com.van.testService.repository;

import com.van.processor.generated.GenRepository;
import com.van.testService.Application;
import com.van.testService.model.Company;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyRepositoryUpdateTest extends AbstractRepositoryTest {
	private static final Long TEST_ID_CREATE = 170000L;

	@Autowired
	private GenRepository<Company, Long> repositoryForUpdate;

	@BeforeAll
	static void init() {
		AbstractRepositoryTest.init();
	}

	@Test
	public void company_create_ok() {
		List<Company> companies = repositoryForUpdate.getAll();
		assertNotNull(companies);

		Company companyTestOrig = new Company();
		companyTestOrig.setId(TEST_ID_CREATE);
		companyTestOrig.setName("NameOfNewVCompany");

		Optional<Long> newId = repositoryForUpdate.create(companyTestOrig);
		assertTrue(newId.isPresent());

		companies = repositoryForUpdate.getAll();
		assertNotNull(companies);

		Optional<Company> fromDb = repositoryForUpdate.getById(newId.get());
		assertTrue(fromDb.isPresent());
		log.debug("fromDb = " + fromDb.toString());
		Company companyFromDb = fromDb.get();
		assertNotEquals(companyTestOrig, companyFromDb);
	}
}
