package com.van.processor.jdbc;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.DataAccessException;
import com.van.processor.jdbc.extractor.*;
import com.van.processor.jdbc.parameter.ParameterDisposer;
import com.van.processor.jdbc.parameter.SqlParameters;
import com.van.processor.jdbc.parameter.SqlParametersFactory;
import com.van.processor.jdbc.sql.ParsedParametersSql;
import com.van.processor.jdbc.sql.SqlProvider;
import com.van.processor.jdbc.statement.PreparedStatementSetter;
import com.van.processor.jdbc.statement.PreparedStatementBuilder;
import com.van.processor.jdbc.statement.PreparedStatementCallback;
import com.van.processor.jdbc.statement.StatementCallback;
import com.van.processor.jdbc.util.JdbcUtils;
import com.van.processor.jdbc.util.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.van.processor.jdbc.sql.ParsedParametersSqlFactory.getParsedSql;
import static com.van.processor.jdbc.util.JdbcUtils.*;

public class JdbcOperationsService extends AbstractJdbcOperations implements JdbcOperations {

	public JdbcOperationsService(final DataSource dataSource) {
		setDataSource(dataSource);
		afterPropertiesSet();
	}

	@Override
	@Nullable
	public <T> T queryForObject(final String sql, final ResultExtractor<T> rse) throws DataAccessException {
		assert sql != null : "SQL must not be null";
		assert rse != null : "ResultSetExtractor must not be null";
		if (log.isDebugEnabled()) {
			log.debug("Executing SQL query [" + sql + "]");
		}

		ParsedParametersSql parsedParametersSql = getParsedSql(sql);
		if (parsedParametersSql.getTotalParameterCount() != 0) {
			throw new IllegalArgumentException(
					"SQL [" + sql + "]: given 0" +
							" parameters but expected " + parsedParametersSql.getTotalParameterCount());
		}

		/*
		 * Callback to execute the query.
		 */
		class QueryStatementCallback implements StatementCallback<T>, SqlProvider {
			@Override
			public T doInStatement(Statement stmt) throws SQLException {
				ResultSet rs = null;
				try {
					rs = stmt.executeQuery(sql);
					return rse.extractData(rs);
				} finally {
					JdbcUtils.closeResultSet(rs);
				}
			}

			@Override
			public String getSql() {
				return sql;
			}
		}

		return execute(new QueryStatementCallback());
	}

	@Override
	@Nullable
	public <T> T queryForObject(String sql, Object[] args, final ResultExtractor<T> rse) throws DataAccessException {
		return queryForObject(sql, SqlParametersFactory.ofValues(args), rse);
	}

	@Override
	@Nullable
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, final ResultExtractor<T> rse) throws DataAccessException {
		return queryForObject(sql, SqlParametersFactory.ofArray(args, argTypes), rse);
	}

	@Override
	@Nullable
	public <T> T queryForObject(final String sql, final Map<String, ?> paramMap, final ResultExtractor<T> rse) throws DataAccessException {
		return queryForObject(sql, SqlParametersFactory.ofMap(paramMap), rse);
	}

	@Override
	public <T> T queryForObject(String sql, RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(sql, new RowToObjectConverter<>(rowExtractor));
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(sql, SqlParametersFactory.ofValues(args), new RowToObjectConverter<>(rowExtractor));
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(sql, SqlParametersFactory.ofArray(args, argTypes), new RowToObjectConverter<>(rowExtractor));
	}

	@Override
	@Nullable
	public <T> T queryForObject(final String sql, final SqlParameters bindingParams, final ResultExtractor<T> rse) throws DataAccessException {
		return queryForObject(getPreparedStatementBuilder(sql, bindingParams), rse);
	}

	@Override
	@Nullable
	public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(sql, SqlParametersFactory.ofMap(paramMap), rowExtractor);
	}

	@Nullable
	private <T> T queryForObject(final PreparedStatementBuilder psc, final ResultExtractor<T> rse) throws DataAccessException {
		return queryForObject(psc, null, rse);
	}

	@Nullable
	private <T> T queryForObject(final PreparedStatementBuilder psc, final PreparedStatementSetter pss, final ResultExtractor<T> rse) throws DataAccessException {
		assert rse != null : "ResultSetExtractor must not be null";
		log.debug("Executing prepared SQL query");

		return execute(psc, new PreparedStatementCallback<T>() {
			@Override
			@Nullable
			public T doInPreparedStatement(PreparedStatement ps) throws SQLException {
				ResultSet rs = null;
				try {
					if (pss != null) {
						pss.setValues(ps);
					}
					rs = ps.executeQuery();
					return rse.extractData(rs);
				} finally {
					JdbcUtils.closeResultSet(rs);
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}

	@Nullable
	private <T> List<T> queryForObject(final PreparedStatementBuilder psc, final RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(psc, new RowToListConverter<>(rowExtractor));
	}

	@Override
	@Nullable
	public <T> T queryForObject(final String sql, final SqlParameters bindingParams, final RowExtractor<T> rowExtractor) throws DataAccessException {
		List<T> results = queryForObject(getPreparedStatementBuilder(sql, bindingParams), rowExtractor);
		return nullableSingleResult(results);
	}

	@Override
	@Nullable
	public <T> List<T> query(final String sql, final Map<String, ?> paramMap, final RowExtractor<T> rowExtractor) throws DataAccessException {
		return query(sql, SqlParametersFactory.ofMap(paramMap), rowExtractor);
	}

	@Override
	@Nullable
	public <T> List<T> query(final String sql, final RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(sql, new RowToListConverter<>(rowExtractor));
	}

	@Override
	@Nullable
	public <T> List<T> query(String sql, Object[] args, RowExtractor<T> rowExtractor) throws DataAccessException {
		return query(sql, SqlParametersFactory.ofValues(args), rowExtractor);
	}

	@Override
	@Nullable
	public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowExtractor<T> rowExtractor) throws DataAccessException {
		return query(sql, SqlParametersFactory.ofArray(args, argTypes), rowExtractor);
	}

	@Override
	@Nullable
	public <T> List<T> query(final String sql, final SqlParameters bindingParams, final RowExtractor<T> rowExtractor) throws DataAccessException {
		return queryForObject(getPreparedStatementBuilder(sql, bindingParams), rowExtractor);
	}

	//----------------------- Update  -----------------------

	@Override
	public int update(final String sql) throws DataAccessException {
		assert sql != null : "SQL must not be null";
		if (log.isDebugEnabled()) {
			log.debug("Executing SQL update [" + sql + "]");
		}

		/**
		 * Callback to execute the update statement.
		 */
		class UpdateStatementCallback implements StatementCallback<Integer>, SqlProvider {
			@Override
			public Integer doInStatement(Statement stmt) throws SQLException {
				int rows = stmt.executeUpdate(sql);
				if (log.isTraceEnabled()) {
					log.trace("SQL update affected " + rows + " rows");
				}
				return rows;
			}

			@Override
			public String getSql() {
				return sql;
			}
		}

		return updateCountIsNotNull(execute(new UpdateStatementCallback()));
	}

	@Override
	public int update(String sql, KeyHolder keyHolder) throws DataAccessException {
		return update(sql, (Map<String, ?>) null, keyHolder);
	}

	@Override
	public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return update(sql, SqlParametersFactory.ofArray(args, argTypes), null, null);
	}

	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		return update(sql, SqlParametersFactory.ofValues(args), null, null);
	}

	@Override
	public int update(final String sql, final Map<String, ?> paramMap) throws DataAccessException {
		return update(sql, SqlParametersFactory.ofMap(paramMap), null, null);
	}

	@Override
	public int update(final String sql, final Map<String, ?> paramMap, final KeyHolder keyHolder) throws DataAccessException {
		return update(sql, SqlParametersFactory.ofMap(paramMap), keyHolder, null);
	}

	@Override
	public int update(final String sql, final Map<String, ?> paramMap, final KeyHolder keyHolder, final String[] keyColumnNames) throws DataAccessException {
		return update(sql, SqlParametersFactory.ofMap(paramMap), keyHolder, keyColumnNames);
	}

	@Override
	public int update(final String sql, final SqlParameters bindingParams) throws DataAccessException {
		return update(sql, bindingParams, null, null);
	}

	@Override
	public int update(final String sql, final SqlParameters bindingParams, final KeyHolder keyHolder) throws DataAccessException {
		return update(sql, bindingParams, keyHolder, null);
	}

	@Override
	public int update(final String sql, final SqlParameters bindingParams, final KeyHolder keyHolder, final String[] keyColumnNames) throws DataAccessException {
		PreparedStatementBuilder preparedStatementBuilder = getPreparedStatementBuilder(sql, null, bindingParams, preparedStatementConfig -> {
			if (keyColumnNames != null) {
				preparedStatementConfig.generatedKeysColumnNames(keyColumnNames);
			} else {
				preparedStatementConfig.returnGeneratedKeys(true);
			}
		});
		return update(preparedStatementBuilder, null, keyHolder);
	}

	@Override
	public int update(final PreparedStatementBuilder psc) throws DataAccessException {
		return update(psc, null, null);
	}

	@Override
	public int update(final PreparedStatementBuilder psc, final KeyHolder keyHolder) throws DataAccessException {
		assert keyHolder != null : "KeyHolder must not be null";
		log.debug("Executing SQL update and returning generated keys");
		return update(psc, null, keyHolder);
	}

	@Override
	public int update(final PreparedStatementBuilder psc, final PreparedStatementSetter pss, final KeyHolder keyHolder) throws DataAccessException {
		log.debug("Executing prepared SQL update");
		return updateCountIsNotNull(execute(psc, ps -> {
			try {
				if (pss != null) {
					pss.setValues(ps);
				}
				int rows = ps.executeUpdate();
				if (keyHolder != null) {
					List<Map<String, Object>> generatedKeys = keyHolder.getKeyList();
					generatedKeys.clear();
					try (ResultSet keys = ps.getGeneratedKeys()) {
						if (keys != null) {
							RowToListConverter<Map<String, Object>> rse = new RowToListConverter<>(new ToMapRowExtractor(), 1);
							generatedKeys.addAll(resultIsNotNull(rse.extractData(keys)));
						}
					}
					if (log.isTraceEnabled()) {
						log.trace("SQL update affected " + rows + " rows and returned " + generatedKeys.size() + " keys");
					}
				} else if (log.isTraceEnabled()) {
					log.trace("SQL update affected " + rows + " rows");
				}
				return rows;
			} finally {
				if (pss instanceof ParameterDisposer) {
					((ParameterDisposer) pss).cleanupParameters();
				}
			}
		}));
	}

	//--------------------------------------- updateRetKeys  -----------------------------------------
	@Override
	public List<Map<String, Object>> updateRetKeys(String sql) throws DataAccessException {
		return updateRetKeys(sql, (SqlParameters) null, null);
	}

	@Override
	public List<Map<String, Object>> updateRetKeys(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return updateRetKeys(sql, SqlParametersFactory.ofMap(paramMap), null);
	}

	@Override
	public List<Map<String, Object>> updateRetKeys(String sql, Map<String, ?> paramMap, String[] keyColumnNames) throws DataAccessException {
		return updateRetKeys(sql, SqlParametersFactory.ofMap(paramMap), keyColumnNames);
	}

	@Override
	public List<Map<String, Object>> updateRetKeys(final String sql, final SqlParameters bindingParams) throws DataAccessException {
		return updateRetKeys(sql, bindingParams, null);
	}

	@Override
	public List<Map<String, Object>> updateRetKeys(final String sql, final SqlParameters bindingParams, final String[] keyColumnNames) throws DataAccessException {
		PreparedStatementBuilder preparedStatementBuilder = getPreparedStatementBuilder(sql, null, bindingParams, preparedStatementConfig -> {
			if (keyColumnNames != null) {
				preparedStatementConfig.generatedKeysColumnNames(keyColumnNames);
			} else {
				preparedStatementConfig.returnGeneratedKeys(true);
			}
		});
		return updateRetKeys(preparedStatementBuilder, null);
	}

	@Override
	public List<Map<String, Object>> updateRetKeys(final PreparedStatementBuilder preparedStatementBuilder, final PreparedStatementSetter pss) throws DataAccessException {
		log.debug("Executing prepared SQL update");
		return execute(preparedStatementBuilder, ps -> {
			try {
				if (pss != null) {
					pss.setValues(ps);
				}
				int rows = ps.executeUpdate();
				List<Map<String, Object>> generatedKeys = new LinkedList<>();
				try (ResultSet keys = ps.getGeneratedKeys()) {
					if (keys != null) {
						RowToListConverter<Map<String, Object>> rse = new RowToListConverter<>(new ToMapRowExtractor(), 1);
						generatedKeys.addAll(resultIsNotNull(rse.extractData(keys)));
					}
				}
				if (log.isTraceEnabled()) {
					log.trace("SQL update affected " + rows + " rows and returned " + generatedKeys.size() + " keys");
				}
				return generatedKeys;
			} finally {
				if (pss instanceof ParameterDisposer) {
					((ParameterDisposer) pss).cleanupParameters();
				}
			}
		});
	}

	//-------------------------------- execute ----------------------------

	@Nullable
	private <T> T execute(final PreparedStatementBuilder preparedStatementBuilder, final PreparedStatementCallback<T> action) throws DataAccessException {
		assert preparedStatementBuilder != null : "PreparedStatementCreator must not be null";
		assert action != null : "Callback object must not be null";
		if (log.isDebugEnabled()) {
			String sql = getSql(preparedStatementBuilder);
			log.debug("Executing prepared SQL statement" + (sql != null ? " [" + sql + "]" : ""));
		}

		try (Connection con = obtainDataSource().getConnection(); PreparedStatement preparedStatement = preparedStatementBuilder.buildPreparedStatement(con)) {
			applyStatementSettings(preparedStatement);
			T result = action.doInPreparedStatement(preparedStatement);
			handleWarnings(preparedStatement);
			return result;
		} catch (SQLException ex) {
			throw translateException("PreparedStatementCallback", getSql(action), ex);
		}
	}

	@Override
	public void execute(final String sql) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("Executing SQL statement [" + sql + "]");
		}

		/*
		 * Callback to execute the statement.
		 */
		class ExecuteStatementCallback implements StatementCallback<Object>, SqlProvider {
			@Override
			public Object doInStatement(Statement stmt) throws SQLException {
				stmt.execute(sql);
				return null;
			}

			@Override
			public String getSql() {
				return sql;
			}
		}

		execute(new ExecuteStatementCallback());
	}

	@Nullable
	private <T> T execute(final StatementCallback<T> action) throws DataAccessException {
		assert action != null : "Callback object must not be null";
		try (Connection con = obtainDataSource().getConnection(); Statement stmt = con.createStatement()) {
			applyStatementSettings(stmt);
			T result = action.doInStatement(stmt);
			handleWarnings(stmt);
			return result;
		} catch (SQLException ex) {
			throw translateException("StatementCallback", getSql(action), ex);
		}
	}
}
