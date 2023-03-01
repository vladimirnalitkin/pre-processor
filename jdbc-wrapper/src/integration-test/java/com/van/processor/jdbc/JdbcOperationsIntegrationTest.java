package com.van.processor.jdbc;

import com.van.processor.exeption.DataAccessException;
import com.van.processor.jdbc.model.Address;
import com.van.processor.jdbc.parameter.ArraySqlParameters;
import com.van.processor.jdbc.parameter.MapSqlParameters;
import com.van.processor.jdbc.parameter.SqlParameters;
import com.van.processor.jdbc.util.GeneratedKeyHolder;
import com.van.processor.jdbc.util.KeyHolder;
import com.van.processor.jdbc.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcOperationsIntegrationTest {
	private JdbcOperations template;

	@BeforeEach
	public void init() throws SQLException {
		TestUtils testUtils = new TestUtils();
		testUtils.init();
		testUtils.reCreateSchema(testUtils.getDataSource());
		template = new JdbcOperationsService(testUtils.getDataSource());
	}

	//--------------------------- queryForObject ----------------------------
	@Test
	public void testQueryForObject_RowExtractor_simpleSql() throws DataAccessException {
		Address addres = template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (1,2,3) ", (rs, i) -> {
			Address result = new Address();
			result.setId(rs.getLong("Id"));
			result.setPrbrId(rs.getLong("prbr_id"));
			result.setStreet(rs.getString("street"));
			result.setHouseNumber(rs.getString("house_number"));
			result.setZipCode(rs.getString("zip_code"));
			return result;
		});
		assertNotNull(addres);
	}

	@Test
	public void testQueryForObject_RowExtractor_simpleSql_With_ParamArray() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		Address addres = template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (1,2,3) "
				, params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(addres);
	}

	@Test
	public void testQueryForObject_RowExtractor_With_ParamArray() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		Address addres = template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
				, params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(addres);
	}

	@Test
	public void testQueryForObject_RowExtractor_With_ParamArray_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
				template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQueryForObject_RowExtractor_With_ParamArray_negative2() throws DataAccessException {
		Object[] params = new Object[]{1, 3};
		assertThrows(IllegalArgumentException.class, () ->
				template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, params
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQueryForObject_RowExtractor_With_ParamArray_negative3() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
				template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, (Object[]) null
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQueryForObject_RowExtractor_With_ParamArray_Types() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		int[] types = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER};
		Address addres = template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
				, params, types
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(addres);
	}

	@Test
	public void testQueryForObject_ResultExtractor_With_ParamArray_Types() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		int[] types = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER};
		Address addres = template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
				, params, types
				, (rs) -> {
					if (rs.next()) {
						Address result = new Address();
						result.setId(rs.getLong("Id"));
						result.setPrbrId(rs.getLong("prbr_id"));
						result.setStreet(rs.getString("street"));
						result.setHouseNumber(rs.getString("house_number"));
						result.setZipCode(rs.getString("zip_code"));
						return result;
					} else {
						return null;
					}
				});
		assertNotNull(addres);
	}

	@Test
	public void testQueryForObject_ResultExtractor_With_ParamArray_Types_negative() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		int[] types = new int[]{Types.DATE, Types.INTEGER, Types.INTEGER};
		assertThrows(RuntimeException.class, () ->
				template.queryForObject("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, params, types
						, (rs) -> {
							if (rs.next()) {
								Address result = new Address();
								result.setId(rs.getLong("Id"));
								result.setPrbrId(rs.getLong("prbr_id"));
								result.setStreet(rs.getString("street"));
								result.setHouseNumber(rs.getString("house_number"));
								result.setZipCode(rs.getString("zip_code"));
								return result;
							} else {
								return null;
							}
						}), "Cannot parse \"DATE\" constant \"1\" ");
	}

	//--------------------------- query ----------------------------
	@Test
	public void testQuery_RowExtractor_simpleSql() throws DataAccessException {
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (1,2,3) ", (rs, i) -> {
			Address result = new Address();
			result.setId(rs.getLong("Id"));
			result.setPrbrId(rs.getLong("prbr_id"));
			result.setStreet(rs.getString("street"));
			result.setHouseNumber(rs.getString("house_number"));
			result.setZipCode(rs.getString("zip_code"));
			return result;
		});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_simpleSql_with_null() throws DataAccessException {
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (1,2,3) "
				, (Object[]) null
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_simpleSql_with_SqlParameters_null() throws DataAccessException {
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (1,2,3) "
				, (SqlParameters) null
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_simpleSql_with_params() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (1,2,3) "
				, params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) ", params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
				template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_null_array_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
				template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, (Object[]) null
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_null_map_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
				template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, (Map<String, ?>) null
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_null_SqlParameters_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
				template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, (SqlParameters) null
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_negative2() throws DataAccessException {
		Object[] params = new Object[]{1, 3};
		assertThrows(IllegalArgumentException.class, () ->
				template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
						, params
						, (rs, i) -> {
							Address result = new Address();
							result.setId(rs.getLong("Id"));
							result.setPrbrId(rs.getLong("prbr_id"));
							result.setStreet(rs.getString("street"));
							result.setHouseNumber(rs.getString("house_number"));
							result.setZipCode(rs.getString("zip_code"));
							return result;
						}), "");
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_Types() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		int[] types = new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER};
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
				, params, types
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_With_ParamArray_Types2() throws DataAccessException {
		Object[] params = new Object[]{1, 2, 3};
		int[] types = new int[]{Types.INTEGER, Types.INTEGER};
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) "
				, params, types
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_With_ParamMap() throws DataAccessException {
		Map<String, ?> params = Collections.singletonMap("name", Arrays.asList(1, 2, 3));

		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (:name) ", params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	@Test
	public void testQuery_RowExtractor_With_MapSqlParameters() throws DataAccessException {
		MapSqlParameters params = MapSqlParameters.of()
				.addValue("name", Arrays.asList(1, 2, 3));

		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (:name) ", params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}


	@Test
	public void testQuery_RowExtractor_With_ArraySqlParameters() throws DataAccessException {
		ArraySqlParameters params = ArraySqlParameters.of()
				.addValue(1)
				.addValue(2)
				.addValue(3);

		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (?,?,?) ", params
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
	}

	//--------------------------- Update ----------------------------
	@Test
	public void testUpdate_SimpleSql() throws DataAccessException {
		int rows = template.update("UPDATE ADDRESS SET STREET = 'STREET-1' WHERE ID in (1,2,3) ");
		assertEquals(3, rows);
	}

	@Test
	public void testUpdate_With_SqlParameters_null_array_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
						template.update("UPDATE ADDRESS SET STREET = 'STREET-1' WHERE ID in (?,?,?) ", (Object[]) null)
				, "");
	}

	@Test
	public void testUpdate_With_SqlParameters_null_map_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
						template.update("UPDATE ADDRESS SET STREET = 'STREET-1' WHERE ID in (?,?,?) ", (Map<String, ?>) null)
				, "");
	}

	@Test
	public void testUpdate_With_SqlParameters_null_SqlParameters_negative() throws DataAccessException {
		assertThrows(IllegalArgumentException.class, () ->
						template.update("UPDATE ADDRESS SET STREET = 'STREET-1' WHERE ID in (?,?,?) ", (SqlParameters) null)
				, "");
	}

	@Test
	public void testUpdate_With_SqlParameters_negative() throws DataAccessException {
		Object[] params = new Object[]{1, 3};
		assertThrows(IllegalArgumentException.class, () ->
						template.update("UPDATE ADDRESS SET STREET = 'STREET-1' WHERE ID in (?,?,?) ", params)
				, "");
	}

	@Test
	public void testUpdate_With_SqlParameters() throws DataAccessException {
		MapSqlParameters paramSource = MapSqlParameters.of()
				.addValue("name", Arrays.asList(1, 2, 3));

		int rows = template.update("UPDATE ADDRESS SET STREET = 'STREET-2' WHERE ID in (:name) ", paramSource);
		assertEquals(3, rows);

		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (:name) ", paramSource
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(3, address.size());
		assertEquals("STREET-2", address.get(0).getStreet());
	}

	@Test
	public void testUpdate_With_SqlParameters_And_keyHolder() throws DataAccessException {
		MapSqlParameters paramSource = MapSqlParameters.of()
				.addValue("name", Arrays.asList(1, 2, 3));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = template.update("UPDATE ADDRESS SET STREET = 'STREET-2' WHERE ID in (:name) ", paramSource, keyHolder);
		assertEquals(3, rows);
		assertNotNull(keyHolder.getKeyList());
	}

	@Test
	public void testUpdate_With_ParamMap_And_keyHolder() throws DataAccessException {
		Map<String, ?> params = Collections.singletonMap("name", Arrays.asList(1, 2, 3));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = template.update("UPDATE ADDRESS SET STREET = 'STREET-2' WHERE ID in (:name) ", params, keyHolder);
		assertEquals(3, rows);
		assertNotNull(keyHolder.getKeyList());
	}

	@Test
	public void testUpdate_With_ParamMap_with_filter() throws DataAccessException {
		Map<String, ?> params = Collections.singletonMap("name", Arrays.asList(1, 2, 3));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = template.update("UPDATE ADDRESS SET STREET = 'STREET-2' WHERE ID in (:name) and PRBR_ID =7 ", params, keyHolder);
		assertEquals(0, rows);
		assertNotNull(keyHolder.getKeyList());
	}

	@Test
	public void testUpdate_RetKeys_With_ParamMap() throws DataAccessException {
		Map<String, ?> params = Collections.singletonMap("name", Arrays.asList(1, 2, 3));
		List<Map<String, Object>> rows = template.updateRetKeys("UPDATE ADDRESS SET STREET = 'STREET-2' WHERE ID in (:name) ", params);
		assertNotNull(rows);
		assertNotNull(rows.get(0));
	}

	@Test
	public void testUpdate_RetKeys_With_MapSqlParameters() throws DataAccessException {
		MapSqlParameters paramSource = MapSqlParameters.of()
				.addValue("name", Arrays.asList(1, 2, 3));
		List<Map<String, Object>> rows = template.updateRetKeys("UPDATE ADDRESS SET STREET = 'STREET-2' WHERE ID in (:name) ", paramSource);
		assertNotNull(rows);
		assertNotNull(rows.get(0));
	}

	//--------------------------- Create/Delete ----------------------------
	@Test
	public void testCreate() throws DataAccessException {
		int rows = template.update("INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID,COMPANY_ID) " +
				"VALUES (77, 'Broy77', '31', '07094',300,1)");
		assertEquals(1, rows);

		MapSqlParameters paramSource = MapSqlParameters.of()
				.addValue("ids", Arrays.asList(77, 1000));
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (:ids) ", paramSource
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(77, address.get(0).getId().intValue());
	}

	@Test
	public void testCreate_KeyHolder() throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = template.update("INSERT INTO ADDRESS (STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID,COMPANY_ID) " +
						"VALUES ('TestStreet', '31a', '07094',1,1)"
				, keyHolder);
		assertEquals(1, rows);
		assertNotNull(keyHolder.getKey());
	}

	@Test
	public void testCreate_KeyHolder_WithParam() throws DataAccessException {
		MapSqlParameters paramInsert = MapSqlParameters.of()
				.addValue("ID", 177)
				.addValue("STREET", "StreetTestIns2")
				.addValue("HOUSE_NUMBER", 23)
				.addValue("ZIP_CODE", "07093")
				.addValue("PRBR_ID", 10)
				.addValue("COMPANY_ID", 1);
		List<Map<String, Object>> retKeys = template.updateRetKeys("INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID,COMPANY_ID) " +
				"VALUES (:ID,:STREET,:HOUSE_NUMBER,:ZIP_CODE,:PRBR_ID,:COMPANY_ID)", paramInsert);

		MapSqlParameters paramSource = MapSqlParameters.of()
				.addValue("ids", Arrays.asList(177, 1000));
		List<Address> address = template.query("SELECT * FROM ADDRESS WHERE ID in (:ids) ", paramSource
				, (rs, i) -> {
					Address result = new Address();
					result.setId(rs.getLong("Id"));
					result.setPrbrId(rs.getLong("prbr_id"));
					result.setStreet(rs.getString("street"));
					result.setHouseNumber(rs.getString("house_number"));
					result.setZipCode(rs.getString("zip_code"));
					return result;
				});
		assertNotNull(address);
		assertEquals(177, address.get(0).getId().intValue());
		assertEquals("StreetTestIns2", address.get(0).getStreet());
	}

}
