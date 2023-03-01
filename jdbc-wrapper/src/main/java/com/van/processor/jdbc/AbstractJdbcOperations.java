package com.van.processor.jdbc;

import com.van.processor.exeption.DataAccessException;
import com.van.processor.exeption.SQLWarningException;
import com.van.processor.exeption.UncategorizedSQLException;
import com.van.processor.jdbc.parameter.SqlParameter;
import com.van.processor.jdbc.parameter.SqlParameters;
import com.van.processor.jdbc.sql.NamedParameterUtils;
import com.van.processor.jdbc.sql.ParsedParametersSql;
import com.van.processor.jdbc.sql.ParsedParametersSqlFactory;
import com.van.processor.jdbc.sql.SqlProvider;
import com.van.processor.jdbc.statement.PreparedStatementBuilder;
import com.van.processor.jdbc.statement.PreparedStatementConfig;
import com.van.processor.jdbc.statement.PreparedStatementFactory;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;

import static com.van.processor.jdbc.sql.ParsedParametersSqlFactory.getParsedSql;
import static com.van.processor.jdbc.statement.PreparedStatementFactory.builder;

/**
 * Parent for JdbcOperations implementation classes.
 */
public abstract class AbstractJdbcOperations extends DataSourceAccessor implements JdbcSettings {

	/**
	 * If this variable is false, we will throw exceptions on SQL warnings.
	 */
	private boolean ignoreWarnings = true;

	/**
	 * If this variable is set to a non-negative value, it will be used for setting the
	 * fetchSize property on statements used for query processing.
	 */
	private int fetchSize = -1;

	/**
	 * If this variable is set to a non-negative value, it will be used for setting the
	 * maxRows property on statements used for query processing.
	 */
	private int maxRows = -1;

	/**
	 * If this variable is set to a non-negative value, it will be used for setting the
	 * queryTimeout property on statements used for query processing.
	 */
	private int queryTimeout = -1;

	/**
	 * If this variable is set to true, then all results checking will be bypassed for any
	 * callable statement processing. This can be used to avoid a bug in some older Oracle
	 * JDBC drivers like 10.1.0.2.
	 */
	private boolean skipResultsProcessing = false;

	/**
	 * If this variable is set to true then all results from a stored procedure call
	 * that don't have a corresponding SqlOutParameter declaration will be bypassed.
	 * All other results processing will be take place unless the variable
	 * {@code skipResultsProcessing} is set to {@code true}.
	 */
	private boolean skipUndeclaredResults = false;

	/**
	 * If this variable is set to true then execution of a CallableStatement will return
	 * the results in a Map that uses case insensitive names for the parameters.
	 */
	private boolean resultsMapCaseInsensitive = false;

	@Override
	public boolean isIgnoreWarnings() {
		return ignoreWarnings;
	}

	@Override
	public void setIgnoreWarnings(boolean ignoreWarnings) {
		this.ignoreWarnings = ignoreWarnings;
	}

	@Override
	public int getFetchSize() {
		return fetchSize;
	}

	@Override
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	@Override
	public int getMaxRows() {
		return maxRows;
	}

	@Override
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public int getQueryTimeout() {
		return queryTimeout;
	}

	@Override
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	@Override
	public boolean isSkipResultsProcessing() {
		return skipResultsProcessing;
	}

	@Override
	public void setSkipResultsProcessing(boolean skipResultsProcessing) {
		this.skipResultsProcessing = skipResultsProcessing;
	}

	@Override
	public boolean isSkipUndeclaredResults() {
		return skipUndeclaredResults;
	}

	@Override
	public void setSkipUndeclaredResults(boolean skipUndeclaredResults) {
		this.skipUndeclaredResults = skipUndeclaredResults;
	}

	@Override
	public boolean isResultsMapCaseInsensitive() {
		return resultsMapCaseInsensitive;
	}

	@Override
	public void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive) {
		this.resultsMapCaseInsensitive = resultsMapCaseInsensitive;
	}

	/**
	 * Specify the maximum number of entries for this template's SQL cache.
	 * Default is 256.
	 */
	@Override
	public void setCacheLimit(int cacheLimit) {
		ParsedParametersSqlFactory.setCacheLimit(cacheLimit);
	}

	/**
	 * Return the maximum number of entries for this template's SQL cache.
	 */
	@Override
	public int getCacheLimit() {
		return ParsedParametersSqlFactory.getCacheLimit();
	}

	/**
	 * Return new PreparedStatementBuilder object.
	 *
	 * @param sql           - original SQL
	 * @param bindingParams - SqlParameters for binding.
	 * @return new SqlParameters instance.
	 */
	protected PreparedStatementBuilder getPreparedStatementBuilder(String sql, SqlParameters bindingParams) {
		return getPreparedStatementBuilder(sql, null, bindingParams, null);
	}

	/**
	 * Return new PreparedStatementBuilder object.
	 *
	 * @param sql            - original SQL
	 * @param declaredParams - SqlParameters for declaration.
	 * @param bindingParams  - SqlParameters for binding.
	 * @return new SqlParameters instance.
	 */
	protected PreparedStatementBuilder getPreparedStatementBuilder(String sql, SqlParameters declaredParams, SqlParameters bindingParams) {
		return getPreparedStatementBuilder(sql, declaredParams, bindingParams, null);
	}

	/**
	 * Return new PreparedStatementBuilder object.
	 *
	 * @param sql            - original SQL
	 * @param declaredParams - SqlParameters for declaration.
	 * @param bindingParams  - SqlParameters for binding.
	 * @param customizer - Consumer<PreparedStatementConfig> for PreparedStatementConfig customization
	 * @return new SqlParameters instance.
	 */
	protected PreparedStatementBuilder getPreparedStatementBuilder(String sql, SqlParameters declaredParams, SqlParameters bindingParams
			, Consumer<PreparedStatementConfig> customizer) {
		PreparedStatementBuilder pscf = PreparedStatementFactory.builder(sql)
				.declaredParameters(declaredParams)
				.bindingParameters(bindingParams);
		if (customizer != null && pscf != null) {
			customizer.accept((PreparedStatementConfig) pscf);
		}
		return pscf;
	}

	/**
	 * Determine SQL from potential provider object.
	 *
	 * @param sqlProvider object which is potentially a SqlProvider
	 * @return the SQL string, or {@code null} if not known
	 * @see SqlProvider
	 */
	protected static String getSql(Object sqlProvider) {
		if (sqlProvider instanceof SqlProvider) {
			return ((SqlProvider) sqlProvider).getSql();
		} else {
			return null;
		}
	}

	protected void applyStatementSettings(Statement stmt) throws SQLException {
		int fetchSize = getFetchSize();
		if (fetchSize != -1) {
			stmt.setFetchSize(fetchSize);
		}
		int maxRows = getMaxRows();
		if (maxRows != -1) {
			stmt.setMaxRows(maxRows);
		}
		//DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
	}

	protected void handleWarnings(Statement stmt) throws SQLException {
		if (isIgnoreWarnings()) {
			if (log.isDebugEnabled()) {
				SQLWarning warningToLog = stmt.getWarnings();
				while (warningToLog != null) {
					log.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" +
							warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
					warningToLog = warningToLog.getNextWarning();
				}
			}
		} else {
			handleWarnings(stmt.getWarnings());
		}
	}

	/**
	 * Throw a SQLWarningException if encountering an actual warning.
	 *
	 * @param warning the warnings object from the current statement.
	 *                May be {@code null}, in which case this method does nothing.
	 * @throws SQLWarningException in case of an actual warning to be raised
	 */
	protected void handleWarnings(SQLWarning warning) throws SQLWarningException {
		if (warning != null) {
			throw new SQLWarningException("Warning not ignored", warning);
		}
	}

	protected DataAccessException translateException(String task, String sql, SQLException ex) {
		return new UncategorizedSQLException(task, sql, ex);
	}
}

