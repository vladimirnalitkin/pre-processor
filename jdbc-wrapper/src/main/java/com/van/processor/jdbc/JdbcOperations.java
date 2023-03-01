package com.van.processor.jdbc;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.DataAccessException;
import com.van.processor.jdbc.extractor.ResultExtractor;
import com.van.processor.jdbc.extractor.RowExtractor;
import com.van.processor.jdbc.parameter.SqlParameters;
import com.van.processor.jdbc.statement.PreparedStatementSetter;
import com.van.processor.jdbc.statement.PreparedStatementBuilder;
import com.van.processor.jdbc.util.KeyHolder;

import java.util.List;
import java.util.Map;

public interface JdbcOperations {
	/**
	 * Return one object. If select return several - will be return first one.
	 *
	 * @param sql - select statement without parameters.
	 * @param rse - Result extractor
	 * @param <T> - type of return object
	 * @return one object
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, final ResultExtractor<T> rse) throws DataAccessException;

	/**
	 * Return one object. If select return several - will be return first one.
	 *
	 * @param sql  - select statement with parameters.
	 * @param args - Array of parameters, which will be binding.
	 * @param rse  - Result extractor
	 * @param <T>  - type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(String sql, Object[] args, final ResultExtractor<T> rse) throws DataAccessException;

	/**
	 * Return one object. If select return several - will be return first one.
	 *
	 * @param sql      - select statement with parameters.
	 * @param args     - Array of parameters, which will be binding.
	 * @param argTypes - Array types of parameters, which will be binding.
	 * @param rse      - Result extractor
	 * @param <T>      - type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(String sql, Object[] args, int[] argTypes, final ResultExtractor<T> rse) throws DataAccessException;

	/**
	 * Return one object. If select return several - will be return first one.
	 *
	 * @param sql      - select statement with parameters.
	 * @param paramMap Map of parameters, which will be binding.
	 * @param rse      - Result extractor
	 * @param <T>      - type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, final Map<String, ?> paramMap, final ResultExtractor<T> rse) throws DataAccessException;

	/**
	 * Return one object. If select return several - will be return first one.
	 *
	 * @param sql           - select statement with parameters.
	 * @param bindingParams - SqlBindingParameters parameters with sql types.
	 * @param rse           - Result extractor
	 * @param <T>-          type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, final SqlParameters bindingParams, final ResultExtractor<T> rse) throws DataAccessException;

	/**
	 * Return first object. The select can return several - will be return first one.
	 *
	 * @param sql          - select statement with parameters.
	 * @param rowExtractor - run on each rows.
	 * @param <T>-         type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return first object. The select can return several - will be return first one.
	 *
	 * @param sql          - select statement with parameters.
	 * @param args     - Array of parameters, which will be binding.
	 * @param rowExtractor - run on each rows.
	 * @param <T>-         type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, Object[] args, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return first object. The select can return several - will be return first one.
	 *
	 * @param sql          - select statement with parameters.
	 * @param args     - Array of parameters, which will be binding.
	 * @param argTypes - Array types of parameters, which will be binding.
	 * @param rowExtractor - run on each rows.
	 * @param <T>-         type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, Object[] args, int[] argTypes, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return first object. The select can return several - will be return first one.
	 *
	 * @param sql           - select statement with parameters.
	 * @param bindingParams -   SqlParameters, which will be binding.
	 * @param rowExtractor  - run on each rows.
	 * @param <T>-          type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, final SqlParameters bindingParams, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return first object. The select can return several - will be return first one.
	 *
	 * @param sql          - select statement with parameters.
	 * @param paramMap     Map of parameters, which will be binding.
	 * @param rowExtractor - run on each rows.
	 * @param <T>-         type of return object
	 * @return one object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> T queryForObject(final String sql, final Map<String, ?> paramMap, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return the list of objects.
	 *
	 * @param sql          - select statement without parameters.
	 * @param rowExtractor - run on each rows.
	 * @param <T>type      of return object
	 * @return list of object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> List<T> query(final String sql, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return the list of objects.
	 *
	 * @param sql          - select statement without parameters.
	 * @param rowExtractor - run on each rows.
	 * @param <T>type      of return object
	 * @return list of object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> List<T> query(final String sql, Object[] args, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return the list of objects.
	 *
	 * @param sql          - select statement without parameters.
	 * @param rowExtractor - run on each rows.
	 * @param <T>type      of return object
	 * @return list of object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> List<T> query(final String sql, Object[] args, int[] argTypes, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return the list of objects.
	 *
	 * @param sql          - select statement without parameters.
	 * @param rowExtractor - run on each rows.
	 * @param <T>type      of return object
	 * @return list of object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> List<T> query(final String sql, final Map<String, ?> paramMap, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Return the list of objects.
	 *
	 * @param sql          - select statement without parameters.
	 * @param rowExtractor - run on each rows.
	 * @param <T>type      of return object
	 * @return list of object with <>T</> type
	 * @throws DataAccessException exception.
	 */
	@Nullable
	<T> List<T> query(final String sql, final SqlParameters bindingParams, final RowExtractor<T> rowExtractor) throws DataAccessException;

	/**
	 * Update sql statement.
	 *
	 * @param sql - SQL without parameters.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql       - SQL with parameters.
	 * @param keyHolder - Key Holder.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final KeyHolder keyHolder) throws DataAccessException;

	/**
	 * Issue a single SQL update operation (such as an insert, update or delete statement)
	 * via a prepared statement, binding the given arguments.
	 *
	 * @param sql      the SQL containing bind parameters
	 * @param args     arguments to bind to the query
	 * @param argTypes the SQL types of the arguments
	 *                 (constants from {@code java.sql.Types})
	 * @return the number of rows affected
	 * @throws DataAccessException if there is any problem issuing the update
	 * @see java.sql.Types
	 */
	int update(String sql, Object[] args, int[] argTypes) throws DataAccessException;

	/**
	 * Issue a single SQL update operation (such as an insert, update or delete statement)
	 * via a prepared statement, binding the given arguments.
	 *
	 * @param sql  the SQL containing bind parameters
	 * @param args arguments to bind to the query
	 *             (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 *             may also contain {@link SqlParameterValue} objects which indicate not
	 *             only the argument value but also the SQL type and optionally the scale
	 * @return the number of rows affected
	 * @throws DataAccessException if there is any problem issuing the update
	 */
	int update(String sql, @Nullable Object... args) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql      - SQL with parameters.
	 * @param paramMap Map of parameters, which will be binding.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final Map<String, ?> paramMap) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql       - SQL with parameters.
	 * @param paramMap  - Map of parameters, which will be binding.
	 * @param keyHolder - Key Holder.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final Map<String, ?> paramMap, final KeyHolder keyHolder) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql            - SQL with parameters.
	 * @param paramMap       Map of parameters, which will be binding.
	 * @param keyHolder      - Key Holder.
	 * @param keyColumnNames - columns array for key.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final Map<String, ?> paramMap, final KeyHolder keyHolder, final String[] keyColumnNames) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql           - SQL with parameters.
	 * @param bindingParams - SqlBindingParameters parameters with sql types.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final SqlParameters bindingParams) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql           - SQL with parameters.
	 * @param bindingParams - SqlBindingParameters parameters with sql types.
	 * @param keyHolder     - Key Holder.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final SqlParameters bindingParams, final KeyHolder keyHolder) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql            - SQL with parameters.
	 * @param bindingParams  - SqlBindingParameters parameters with sql types.
	 * @param keyHolder      - Key Holder.
	 * @param keyColumnNames - columns array for key.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final String sql, final SqlParameters bindingParams, final KeyHolder keyHolder, final String[] keyColumnNames) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param psc - PreparedStatementBuilder.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final PreparedStatementBuilder psc) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param psc       - PreparedStatementBuilder.
	 * @param keyHolder - Key Holder.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final PreparedStatementBuilder psc, final KeyHolder keyHolder) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param psc       - PreparedStatementBuilder.
	 * @param pss       - PreparedStatementBinder.
	 * @param keyHolder - Key Holder.
	 * @return - number of processed rows.
	 * @throws DataAccessException exception.
	 */
	int update(final PreparedStatementBuilder psc, final PreparedStatementSetter pss, final KeyHolder keyHolder) throws DataAccessException;

	//--------------------------------------- updateRetKeys  -----------------------------------------

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql - SQL with parameters.
	 * @return - list of Map with keys of updated records.
	 * @throws DataAccessException exception.
	 */
	List<Map<String, Object>> updateRetKeys(final String sql) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql      - SQL with parameters.
	 * @param paramMap Map of parameters, which will be binding.
	 * @return - list of Map with keys of updated records.
	 * @throws DataAccessException exception.
	 */
	List<Map<String, Object>> updateRetKeys(final String sql, final Map<String, ?> paramMap) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql            - SQL with parameters.
	 * @param paramMap       Map of parameters, which will be binding.
	 * @param keyColumnNames - columns array for key.
	 * @return - list of Map with keys of updated records.
	 * @throws DataAccessException exception.
	 */
	List<Map<String, Object>> updateRetKeys(final String sql, final Map<String, ?> paramMap, final String[] keyColumnNames) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql           - SQL with parameters.
	 * @param bindingParams - SqlBindingParameters parameters with sql types.
	 * @return - list of Map with keys of updated records.
	 * @throws DataAccessException exception.
	 */
	List<Map<String, Object>> updateRetKeys(final String sql, final SqlParameters bindingParams) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param sql            - SQL with parameters.
	 * @param bindingParams  - SqlBindingParameters parameters with sql types.
	 * @param keyColumnNames - columns array for key.
	 * @return - list of Map with keys of updated records.
	 * @throws DataAccessException exception.
	 */
	List<Map<String, Object>> updateRetKeys(final String sql, final SqlParameters bindingParams, final String[] keyColumnNames) throws DataAccessException;

	/**
	 * Update sql statement with parameters.
	 *
	 * @param psc - PreparedStatementBuilder.
	 * @param pss - PreparedStatementBinder.
	 * @return - list of Map with keys of updated records.
	 * @throws DataAccessException exception.
	 */
	List<Map<String, Object>> updateRetKeys(final PreparedStatementBuilder psc, final PreparedStatementSetter pss) throws DataAccessException;

	/**
	 * Issue a single SQL execute, typically a DDL statement.
	 *
	 * @param sql static SQL to execute
	 * @throws DataAccessException if there is any problem
	 */
	void execute(String sql) throws DataAccessException;
}
