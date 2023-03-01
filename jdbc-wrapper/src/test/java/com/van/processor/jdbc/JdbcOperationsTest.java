package com.van.processor.jdbc;

import com.van.processor.jdbc.extractor.RowExtractor;
import com.van.processor.jdbc.parameter.SqlParameters;
import com.van.processor.jdbc.parameter.SqlParametersFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JdbcOperationsTest {

	private static Connection connection;

	private static PreparedStatement preparedStatement;

	private static ResultSet resultSet;

	private static ResultSetMetaData resultSetMetaData;

	private static JdbcOperations template;

	@BeforeAll
	static void setup() throws Exception {
		connection = mock(Connection.class);
		DataSource dataSource = mock(DataSource.class);
		preparedStatement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		resultSetMetaData = mock(ResultSetMetaData.class);
		template = new JdbcOperationsService(dataSource);
		given(dataSource.getConnection()).willReturn(connection);
		given(resultSetMetaData.getColumnCount()).willReturn(1);
		given(resultSetMetaData.getColumnLabel(1)).willReturn("age");
		given(connection.prepareStatement(anyString())).willReturn(preparedStatement);
		given(preparedStatement.executeQuery()).willReturn(resultSet);
	}

	@Test
	public void testQueryWithParamMap() throws Exception {
		given(resultSet.getMetaData()).willReturn(resultSetMetaData);
		given(resultSet.next()).willReturn(true, true, false);
		given(resultSet.getObject(1)).willReturn(11, 12);

		Map<String, Object> params = new HashMap<String, Object>() {{
			put("id", 12);
			put("we", "name");
		}};
		List<TestParameterBean> li = template.query("SELECT AGE FROM CUSTMR WHERE ID < :id AND NAME = :we", params
				, (rs, i) -> new TestParameterBean((Integer) rs.getObject(1)));
		assertNotNull(li);
		assertEquals(2, li.size());
		assertEquals(11, li.get(0).getId());
		assertEquals(12, li.get(1).getId());
		verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ? AND NAME = ?");
		verify(preparedStatement).setObject(1, 12);
	}

	@Test
	public void testQueryWithParamArray() throws Exception {
		given(resultSet.getMetaData()).willReturn(resultSetMetaData);
		given(resultSet.next()).willReturn(true, true, false);
		given(resultSet.getObject(1)).willReturn(11, 12);

		Object[] params = new Object[]{17, 21};
		List<TestParameterBean> li = template.query("SELECT AGE FROM CUSTMR WHERE ID < ? AND ID > ?", params
				, (rs, i) -> new TestParameterBean((Integer) rs.getObject(1)));
		assertNotNull(li);
		assertEquals(2, li.size());
		assertEquals(11, li.get(0).getId());
		assertEquals(12, li.get(1).getId());
		verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ? AND ID > ?");
		verify(preparedStatement).setObject(1, 17);
	}

	@Test
	public void testQueryWithNamParamArray() throws Exception {
		given(resultSet.getMetaData()).willReturn(resultSetMetaData);
		given(resultSet.next()).willReturn(true, true, false);
		given(resultSet.getObject(1)).willReturn(11, 12);

		Map<String, Object> params = new HashMap<String, Object>() {{
			put("ids", Arrays.asList(23, 45, 77, 2));
		}};
		List<TestParameterBean> li = template.query("SELECT AGE FROM CUSTMR WHERE ID in :ids", params
				, (rs, i) -> new TestParameterBean((Integer) rs.getObject(1)));
		assertNotNull(li);
		assertEquals(2, li.size());
		assertEquals(11, li.get(0).getId());
		assertEquals(12, li.get(1).getId());
		verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID in ?, ?, ?, ?");
	}


	@Test
	public void testQueryForObjectWithParamMapAndRowMapper() throws Exception {
		given(resultSet.next()).willReturn(true, false);
		given(resultSet.getInt(1)).willReturn(22);

		SqlParameters params = SqlParametersFactory.ofMap("id", 3);
		Object o = template.queryForObject("SELECT AGE FROM CUSTMR WHERE ID = :id",
				params, new RowExtractor<Object>() {
					@Override
					public Object extractData(ResultSet rs, int rowNum) throws SQLException {
						return rs.getInt(1);
					}
				});

		assertTrue(o instanceof Integer);
		verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID = ?");
		verify(preparedStatement).setObject(1, 3);
	}

	/*
    @Test
    public void testQueryForListWithParamMap() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, true, false);
        given(resultSet.getObject(1)).willReturn(11, 12);

        SqlParameters params = SqlParametersFactory.ofMap("id", 3);
        List<Map<String, Object>> li = template.queryForList(
                "SELECT AGE FROM CUSTMR WHERE ID < :id", params);

        assertThat(li.size()).as("All rows returned").isEqualTo(2);
        assertThat(((Integer) li.get(0).get("age")).intValue()).as("First row is Integer").isEqualTo(11);
        assertThat(((Integer) li.get(1).get("age")).intValue()).as("Second row is Integer").isEqualTo(12);

        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForListWithParamMapAndEmptyResult() throws Exception {
        given(resultSet.next()).willReturn(false);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", 3);
        List<Map<String, Object>> li = template.queryForList(
                "SELECT AGE FROM CUSTMR WHERE ID < :id", params);

        assertThat(li.size()).as("All rows returned").isEqualTo(0);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForListWithParamMapAndSingleRowAndColumn() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getObject(1)).willReturn(11);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", 3);
        List<Map<String, Object>> li = template.queryForList(
                "SELECT AGE FROM CUSTMR WHERE ID < :id", params);

        assertThat(li.size()).as("All rows returned").isEqualTo(1);
        assertThat(((Integer) li.get(0).get("age")).intValue()).as("First row is Integer").isEqualTo(11);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ?");
        verify(preparedStatement).setObject(1, 3);
    }
*/

  /*  @Test
    public void testQueryForListWithParamMapAndIntegerElementAndSingleRowAndColumn()
            throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt(1)).willReturn(11);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", 3);
        List<Integer> li = template.query("SELECT AGE FROM CUSTMR WHERE ID < :id",
                params, Integer.class);

        assertThat(li.size()).as("All rows returned").isEqualTo(1);
        assertThat(li.get(0).intValue()).as("First row is Integer").isEqualTo(11);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForMapWithParamMapAndSingleRowAndColumn() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getObject(1)).willReturn(11);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", 3);
        Map<String, Object> map = template.queryForMap("SELECT AGE FROM CUSTMR WHERE ID < :id", params);

        assertThat(((Integer) map.get("age")).intValue()).as("Row is Integer").isEqualTo(11);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID < ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForObjectWithMapAndInteger() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt(1)).willReturn(22);

        Map<String, Object> params = new HashMap<>();
        params.put("id", 3);
        Object o = template.queryForObject("SELECT AGE FROM CUSTMR WHERE ID = :id",
                params, Integer.class);

        boolean condition = o instanceof Integer;
        assertThat(condition).as("Correct result type").isTrue();
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID = ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForObjectWithParamMapAndInteger() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt(1)).willReturn(22);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", 3);
        Object o = template.queryForObject("SELECT AGE FROM CUSTMR WHERE ID = :id",
                params, Integer.class);

        boolean condition = o instanceof Integer;
        assertThat(condition).as("Correct result type").isTrue();
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID = ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForObjectWithParamMapAndList() throws Exception {
        String sql = "SELECT AGE FROM CUSTMR WHERE ID IN (:ids)";
        String sqlToUse = "SELECT AGE FROM CUSTMR WHERE ID IN (?, ?)";
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt(1)).willReturn(22);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", Arrays.asList(3, 4));
        Object o = template.queryForObject(sql, params, Integer.class);

        boolean condition = o instanceof Integer;
        assertThat(condition).as("Correct result type").isTrue();
        verify(connection).prepareStatement(sqlToUse);
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForObjectWithParamMapAndListOfExpressionLists() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt(1)).willReturn(22);

        MapSqlParameterSource params = new MapSqlParameterSource();
        List<Object[]> l1 = new ArrayList<>();
        l1.add(new Object[] {3, "Rod"});
        l1.add(new Object[] {4, "Juergen"});
        params.addValue("multiExpressionList", l1);
        Object o = template.queryForObject(
                "SELECT AGE FROM CUSTMR WHERE (ID, NAME) IN (:multiExpressionList)",
                params, Integer.class);

        boolean condition = o instanceof Integer;
        assertThat(condition).as("Correct result type").isTrue();
        verify(connection).prepareStatement(
                "SELECT AGE FROM CUSTMR WHERE (ID, NAME) IN ((?, ?), (?, ?))");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForIntWithParamMap() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getInt(1)).willReturn(22);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", 3);
        int i = template.queryForObject("SELECT AGE FROM CUSTMR WHERE ID = :id", params, Integer.class).intValue();

        assertThat(i).as("Return of an int").isEqualTo(22);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID = ?");
        verify(preparedStatement).setObject(1, 3);
    }

    @Test
    public void testQueryForLongWithParamBean() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getLong(1)).willReturn(87L);

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(new ParameterBean(3));
        long l = template.queryForObject("SELECT AGE FROM CUSTMR WHERE ID = :id", params, Long.class).longValue();

        assertThat(l).as("Return of a long").isEqualTo(87);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID = ?");
        verify(preparedStatement).setObject(1, 3, Types.INTEGER);
    }

    @Test
    public void testQueryForLongWithParamBeanWithCollection() throws Exception {
        given(resultSet.getMetaData()).willReturn(resultSetMetaData);
        given(resultSet.next()).willReturn(true, false);
        given(resultSet.getLong(1)).willReturn(87L);

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(new ParameterCollectionBean(3, 5));
        long l = template.queryForObject("SELECT AGE FROM CUSTMR WHERE ID IN (:ids)", params, Long.class).longValue();

        assertThat(l).as("Return of a long").isEqualTo(87);
        verify(connection).prepareStatement("SELECT AGE FROM CUSTMR WHERE ID IN (?, ?)");
        verify(preparedStatement).setObject(1, 3);
        verify(preparedStatement).setObject(2, 5);
    }
*/

	static class TestParameterBean {

		private final int id;

		public TestParameterBean(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	static class TestParameterCollectionBean {

		private final Collection<Integer> ids;

		public TestParameterCollectionBean(Integer... ids) {
			this.ids = Arrays.asList(ids);
		}

		public Collection<Integer> getIds() {
			return ids;
		}
	}
}
