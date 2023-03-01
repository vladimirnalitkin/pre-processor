package com.van.processor.jdbc.statement;

import com.van.processor.exeption.InvalidDataAccessApiUsageException;
import com.van.processor.jdbc.parameter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static com.van.processor.jdbc.parameter.SqlParameters.EMPTY_PARAMS;
import static java.sql.Types.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

class PreparedStatementFactoryTest {
	private static final String PARAM_NAME_1 = "id1";
	private static final String VAL_1 = "val1";
	private static final String PARAM_NAME_2 = "id2";
	private static final String VAL_2 = "val2";
	private static final String PARAM_NAME_3 = "id3";
	private static final String VAL_3 = "val3";
	private static final String SQL = "INSERT INTO ADRESS (ID, NAME, ZIP) VALUES (?, ?, ?)";
	private static final String SQL_NAMED = "INSERT INTO ADRESS (ID, NAME, ZIP) VALUES (:" + PARAM_NAME_1 + ", :" + PARAM_NAME_2 + ", :" + PARAM_NAME_3 + ")";
	private static final Map<String, Object> TEST_MAP = Collections.unmodifiableMap(new HashMap<String, Object>() {
		{
			put(PARAM_NAME_1, VAL_1);
			put(PARAM_NAME_2, VAL_2);
			put(PARAM_NAME_3, VAL_3);
		}
	});

	private static final List<SqlParameter> EXP_LIST_SQL_PAR = Arrays.asList(SqlParameter.of(VARCHAR), SqlParameter.of(VARCHAR), SqlParameter.of(VARCHAR));
	private static final Object[] EXP_OBJ_ARR_NEG = new Object[]{"Test", 34};
	private static final Object[] EXP_OBJ_ARR = new Object[]{"Test", 34, 3};

	private Connection connection;

	@BeforeEach
	public void setup() throws SQLException {
		connection = mock(Connection.class);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		Mockito.when(connection.prepareStatement(any())).thenReturn(preparedStatement);
	}

	@Test
	public void test_sql() {
		assertEquals(SQL, PreparedStatementFactory.builder(SQL).getSql());
	}

	@Test
	public void test_sql_types() {
		PreparedStatementFactory.PreparedStatementBuilderImp psb = PreparedStatementFactory
				.builder(SQL)
				.declaredParameters(VARCHAR, VARCHAR, VARCHAR);
		assertEquals(SQL, psb.getSql());
		assertEquals(EXP_LIST_SQL_PAR, psb.getDeclaredParameters().getList());
	}

	@Test
	public void test_binding_unnamed_parameters_negative() {
		PreparedStatementBuilderParam psb = PreparedStatementFactory
				.builder(SQL_NAMED);
		assertThrows(IllegalArgumentException.class, () -> psb.bindingParameters(EXP_OBJ_ARR_NEG)
				, "SQL [" + SQL + "]: given " + 3 + " parameters but expected " + 2);
	}

	@Test
	public void test_binding_unnamed_parameters_ok() throws SQLException {
		PreparedStatementBuilder psb = PreparedStatementFactory.builder(SQL)
				.declaredParameters(VARCHAR, VARCHAR, VARCHAR)
				.bindingParameters(EXP_OBJ_ARR);
		assertNotNull(psb);
		PreparedStatement preparedStatement = psb.buildPreparedStatement(connection);
		assertNotNull(preparedStatement);
	}

	@Test
	public void test_binding_named_parameters_ok() throws SQLException {
		SqlParameters namedParams = MapSqlParameters.of(TEST_MAP);
		PreparedStatementBuilder psb = PreparedStatementFactory.builder(SQL_NAMED)
				.bindingParameters(namedParams);
		assertNotNull(psb);
		PreparedStatement preparedStatement = psb.buildPreparedStatement(connection);
		assertNotNull(preparedStatement);
	}

	@Test
	public void test_binding_named_parameters_with_declare_ok() throws SQLException {
		SqlParameters namedParams = MapSqlParameters.of(TEST_MAP);
		PreparedStatementBuilder psb = PreparedStatementFactory.builder(SQL_NAMED)
				.declaredParameters(namedParams)
				.bindingParameters(namedParams);
		assertNotNull(psb);
		PreparedStatement preparedStatement = psb.buildPreparedStatement(connection);
		assertNotNull(preparedStatement);
	}

	@Test
	public void test_binding_named_parameters_incorrect_number() {
		MapSqlParameters namedParams = MapSqlParameters.of()
				.addValue("id1", VARCHAR, "a")
				.addValue("id3", VARCHAR, "c");
		PreparedStatementBuilderParam psb = PreparedStatementFactory.builder(SQL_NAMED);
		assertThrows(IllegalArgumentException.class, () -> psb.bindingParameters(namedParams)
				, "No value registered for key 'id2'");
	}

	@Test
	public void test_named_declared_negative() {
		PreparedStatementBuilderParam psb = PreparedStatementFactory.builder(SQL_NAMED);
		assertThrows(IllegalArgumentException.class, () -> psb.declaredParameters(VARCHAR, VARCHAR, VARCHAR)
				, "SQL [" + SQL_NAMED + "]: required named parameters for declaration");
	}

	@Test
	public void test_named_parameters_negative() {
		SqlParameters namedParams = MapSqlParameters.of(TEST_MAP);
		PreparedStatementBuilderParam psb = PreparedStatementFactory.builder(SQL_NAMED)
				.declaredParameters(namedParams);
		assertThrows(IllegalArgumentException.class, () -> psb.bindingParameters(EXP_OBJ_ARR)
				, "SQL [" + SQL_NAMED + "]: required named parameters for binding");
	}

	@Test
	public void test_binding_named_parameters_ok2() throws SQLException {
		String sql = "select 'first name' from artists where id = :id and name=:wee and quote = 'exsqueeze me?'";
		SqlParameters namedParams = MapSqlParameters.of("id", Arrays.asList(23, 45, 77)).addValue("wee", "sdfsdfs");
		PreparedStatementBuilder psb = PreparedStatementFactory.builder(sql)
				.bindingParameters(namedParams);
		assertNotNull(psb);
		PreparedStatement preparedStatement = psb.buildPreparedStatement(connection);
		assertNotNull(preparedStatement);
	}

	@Test
	public void test_binding_named_parameters_nagetive() throws SQLException {
		String sql = "select 'first name' from artists where id = :id and name=:wee and quote = 'exsqueeze me?'";
		SqlParameters namedParams = MapSqlParameters.of("id", new Integer[]{23, 45, 77}).addValue("wee", "sdfsdfs");
		PreparedStatementBuilder psb = PreparedStatementFactory.builder(sql)
				.bindingParameters(namedParams);
		assertNotNull(psb);
		PreparedStatement preparedStatement = psb.buildPreparedStatement(connection);
		assertNotNull(preparedStatement);
	}

	@Test
	public void test_BuildSqlParameter_NamedParameters_List() {
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		List<Integer> expectValue = Arrays.asList(23, 45, 77);
		SqlParameters testParam = SqlParametersFactory.ofMap("id", expectValue);
		assertEquals(expectValue, testParam.getValue("id"));
		Object[] expectedParameters = new Object[]{expectValue};
		Object[] newParameters = PreparedStatementFactory.buildSqlParametersArray(sql, testParam).getValues();
		assertArrayEquals(expectedParameters, newParameters);
	}

	@Test
	public void test_BuildSqlParameter_NamedParameters_List2() {
		String sql = "select 'first name' from artists where id = :id and id= :id and quote = 'exsqueeze me?'";
		List<Integer> expectValue = Arrays.asList(23, 45, 77);
		SqlParameters testParam = SqlParametersFactory.ofMap("id", expectValue);
		assertEquals(expectValue, testParam.getValue("id"));

		Object[] expectedParameters = new Object[]{expectValue, expectValue};
		Object[] newParameters = PreparedStatementFactory.buildSqlParametersArray(sql, testParam).getValues();
		assertArrayEquals(expectedParameters, newParameters);
	}

	@Test
	public void test_BuildSqlParameter_UnNamedParameters_List() {
		List<Integer> expectValue = Arrays.asList(23, 45, 77);
		String sql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		SqlParameters newParameters = PreparedStatementFactory
				.buildSqlParametersArray(
						sql, SqlParametersFactory.ofValues(expectValue)
				);
		assert newParameters != null;
		assertEquals(1, newParameters.size());
		assertEquals(expectValue, newParameters.getValue(0));
	}

	@Test
	public void test_BuildSqlParameter_UnNamedParameters_Array() {
		Object[] params = new Object[] {23, 45, 77};
		Object[] paramsExpect = new Object[] {23};
		String sql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		SqlParameters newParameters = PreparedStatementFactory
				.buildSqlParametersArray(
						sql, SqlParametersFactory.ofValues(params)
				);
		assert newParameters != null;
		assertEquals(1, newParameters.size());
		assertArrayEquals(paramsExpect, newParameters.getValues());
	}


	@Test
	public void testBuildSqlParameterListNamedParametersArray_negative() {
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		assertThrows(IllegalArgumentException.class, () ->
				PreparedStatementFactory.buildSqlParametersArray(sql, SqlParametersFactory.ofTypes(23, 45, 77))
				, "SQL [" + sql + "]: required named parameters for declaration");
	}

	@Test
	public void test_BuildSqlParameter_map_keySensetive() {
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		assertThrows(IllegalArgumentException.class, () ->
						PreparedStatementFactory.buildSqlParametersArray(sql, SqlParametersFactory.ofMap("ID", 45))
				, "No value registered for key 'id'");
	}

	@Test
	public void testConvertParamMapToArray() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("a", "a");
		paramMap.put("b", "b");
		paramMap.put("c", "c");

		assertEquals(3, PreparedStatementFactory.buildSqlParametersArray("xxx :a :b :c", paramMap).size());
		assertEquals(5, PreparedStatementFactory.buildSqlParametersArray("xxx :a :b :c xx :a :b", paramMap).size());
		assertEquals(5, PreparedStatementFactory.buildSqlParametersArray("xxx :a :a :a xx :a :a", paramMap).size());
		assertEquals("b", Objects.requireNonNull(PreparedStatementFactory.buildSqlParametersArray("xxx :a :b :c xx :a :b", paramMap).get(1)).getValue());
		assertThrows(InvalidDataAccessApiUsageException.class, () ->
				PreparedStatementFactory.buildSqlParametersArray("xxx :a :b ?", paramMap), "mixed named parameters and ? placeholders");
	}

	@Test
	public void testConvertTypeMapToArray() {
		MapSqlParameters namedParams = MapSqlParameters.of()
				.addValue("a", 1, "a")
				.addValue("b", VARCHAR, "b")
				.addValue("c", 3, "c");
		assertEquals(3, PreparedStatementFactory.builder("xxx :a :b :c").buildSqlParametersArray(namedParams).size());
		assertEquals(5, PreparedStatementFactory.builder("xxx :a :b :c xx :a :b").buildSqlParametersArray(namedParams).size());
		assertEquals(5, PreparedStatementFactory.builder("xxx :a :a :a xx :a :a").buildSqlParametersArray(namedParams).size());
		assertEquals(VARCHAR, Objects.requireNonNull(PreparedStatementFactory.builder("xxx :a :b :c xx :a :b")
				.buildSqlParametersArray(namedParams).get(1)).getSqlType());
	}

	@Test
	public void testConvertTypeMapToSqlParameterList() {
		MapSqlParameters namedParams = MapSqlParameters.of()
				.addValue("a", VARCHAR, "a")
				.addValue("b", VARCHAR, "b")
				.addValue("c", REAL, "SQL_TYPE", 345.11)
				.addValue("d", DECIMAL, "SQL_TYPE2", 2, 23.45);
		assertEquals(3, PreparedStatementFactory.builder("xxx :a :b :c").buildSqlParametersArray(namedParams).size());
		assertEquals(5, PreparedStatementFactory.builder("xxx :a :b :c xx :a :b").buildSqlParametersArray(namedParams).size());
		assertEquals(5, PreparedStatementFactory.builder("xxx :a :a :a xx :a :a").buildSqlParametersArray(namedParams).size());
		assertEquals(REAL, Objects.requireNonNull(PreparedStatementFactory.builder("xxx :a :b :c xx :a :b")
				.buildSqlParametersArray(namedParams).get(2))
				.getSqlType()
		);
		assertEquals("REAL", Objects.requireNonNull(PreparedStatementFactory.builder("xxx :a :b :c")
				.buildSqlParametersArray(namedParams).get(2))
				.getTypeName()
		);
		assertEquals(2, Objects.requireNonNull(Objects.requireNonNull(PreparedStatementFactory.builder("xxx :a :b :c :d")
				.buildSqlParametersArray(namedParams).get(3))
				.getScale()).intValue()
		);
	}

	@Test
	public void testbuildDeclaredNamedSqlParametersWithMissingParameterValue() {
		String sql = "select count(0) from foo where id = :id";
		assertThrows(IllegalArgumentException.class, () ->
				((PreparedStatementBuilder) PreparedStatementFactory
						.builder(sql))
						.buildPreparedStatement(connection), "SQL [" + sql + "]: given 0 parameters but expected 1"
		);
	}

	@Test
	public void testbuildDeclaredNamedSqlParametersWithMissingParameterValue2() {
		String sql = "select count(0) from foo where id = :id";
		assertThrows(IllegalArgumentException.class, () ->
				((PreparedStatementBuilder) PreparedStatementFactory
						.builder(sql)
						.bindingParameters(Collections.<String, Object>emptyMap()))
						.buildPreparedStatement(connection), "SQL [" + sql + "]: given 0 parameters but expected 1"
		);
	}
}
