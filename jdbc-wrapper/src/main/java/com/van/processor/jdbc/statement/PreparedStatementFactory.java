package com.van.processor.jdbc.statement;

import com.google.common.base.Throwables;
import com.van.processor.jdbc.parameter.*;
import com.van.processor.jdbc.sql.ParameterHolder;
import com.van.processor.jdbc.sql.ParsedParametersSql;
import com.van.processor.jdbc.sql.SqlProvider;
import com.van.processor.jdbc.util.JdbcUtils;
import com.van.processor.jdbc.util.StatementCreatorUtils;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

import static com.van.processor.jdbc.sql.ParsedParametersSqlFactory.getParsedSql;

public abstract class PreparedStatementFactory {

	/**
	 * Return PreparedStatementBuilder implementation.
	 *
	 * @param originalSql - original SQL expression
	 * @return PreparedStatementBuilderImp object
	 */
	public static PreparedStatementBuilderImp builder(String originalSql) {
		return new PreparedStatementBuilderImp(originalSql);
	}

	/**
	 * Convert parameter declarations from an SqlParameterSource to a corresponding SqlBindingParameters.
	 * This is necessary in order to reuse existing methods on JdbcTemplate.
	 * The SqlParameter for a named parameter is placed in the correct position in the
	 * resulting list based on the parsed SQL statement info.
	 *
	 * @param sql           the SQL statement
	 * @param bindingParams the binding named parameters
	 */
	public static SqlParameters buildSqlParametersArray(String sql, Map<String, Object> bindingParams) {
		return builder(sql).buildSqlParametersArray(MapSqlParameters.of(bindingParams));
	}

	/**
	 * Convert parameter declarations from an SqlParameterSource to a corresponding SqlBindingParameters.
	 * This is necessary in order to reuse existing methods on JdbcTemplate.
	 * The SqlParameter for a named parameter is placed in the correct position in the
	 * resulting list based on the parsed SQL statement info.
	 *
	 * @param sql           the SQL statement
	 * @param bindingParams the binding named parameters
	 */
	public static SqlParameters buildSqlParametersArray(String sql, SqlParameters bindingParams) {
		return builder(sql).buildSqlParametersArray(bindingParams);
	}

	/**
	 * Convert parameter declarations from an SqlParameterSource to a corresponding SqlBindingParameters.
	 * This is necessary in order to reuse existing methods on JdbcTemplate.
	 * The SqlParameter for a named parameter is placed in the correct position in the
	 * resulting list based on the parsed SQL statement info.
	 *
	 * @param sql            the SQL statement
	 * @param declaredParams the declared named parameters
	 * @param bindingParams  the binding named parameters
	 */
	public static SqlParameters buildSqlParametersArray(String sql, SqlParameters declaredParams, SqlParameters bindingParams) {
		return builder(sql).declaredParameters(declaredParams).buildSqlParametersArray(bindingParams);
	}

	/**
	 * PreparedStatementBuilder implementation returned by this class.
	 */
	public static class PreparedStatementBuilderImp implements PreparedStatementBuilder, PreparedStatementBuilderParam
			, PreparedStatementSetter
			, PreparedStatementConfig
			, SqlProvider, ParameterDisposer {

		/**
		 * The SQL, which won't change when the parameters change.
		 */
		private final String originalSql;

		/**
		 * List of declared SqlParameter objects.
		 */
		private SqlParameters declaredParameters;

		/**
		 * List of bounded SqlParameter objects.
		 */
		private SqlParameters parameters;

		/**
		 * Prepared statement parameters.
		 */
		private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;

		private boolean updatableResults = false;

		private boolean returnGeneratedKeys = false;

		private String[] generatedKeysColumnNames;

		/**
		 * Private constructor - using in builder.
		 *
		 * @param originalSql - SQL expression.
		 */
		private PreparedStatementBuilderImp(String originalSql) {
			this.originalSql = originalSql;
		}

		/**
		 * Return the declaredParameters.
		 * for testing only.
		 */
		@Override
		public SqlParameters getDeclaredParameters() {
			return this.declaredParameters;
		}

		/**
		 * Return the SqlParameterValue from  DeclaredParameters by parameter name.
		 */
		private SqlParameterValue getDeclaredParameterValue(String name) {
			return this.declaredParameters != null ? this.declaredParameters.get(name) : null;
		}

		/**
		 * Return the SqlParameterValue from  DeclaredParameters by parameter name.
		 */
		private SqlParameterValue getDeclaredParameterValue(int index) {
			return this.declaredParameters != null ? this.declaredParameters.get(index) : null;
		}

		/**
		 * Add a new declared parameters.
		 * <p>Order of parameter addition is significant.
		 *
		 * @param types the array of types of parameters to add to the list of declared parameters
		 */
		@Override
		public PreparedStatementBuilderImp declaredParameters(int... types) {
			return declaredParameters(SqlParametersFactory.ofTypes(types));
		}

		/**
		 * Add a new declared parameters.
		 * <p>Order of parameter addition is significant.
		 *
		 * @param declaredParameters the list of parameters to add to the list of declared parameters
		 */
		@Override
		public PreparedStatementBuilderImp declaredParameters(SqlParameters declaredParameters) {
			if (declaredParameters != null) {
				ParsedParametersSql parsedParametersSql = getParsedSql(originalSql);
				if (parsedParametersSql.getNamedParameterCount() != 0) {
					if (!(declaredParameters instanceof MapSqlParameters)) {
						throw new IllegalArgumentException(
								"SQL [" + originalSql + "]: required named parameters for declaration");
					}
				}
			}
			this.declaredParameters = declaredParameters;
			return this;
		}

		/**
		 * Set binding parameters.
		 *
		 * @param parameters the list of parameters.
		 * @return PreparedStatementBuilderImp
		 */
		@Override
		public PreparedStatementBuilderImp bindingParameters(Object[] parameters) throws IllegalArgumentException {
			return bindingParameters(SqlParametersFactory.ofValues(parameters));
		}

		/**
		 * Set binding parameters.
		 *
		 * @param parameters the list of parameters.
		 * @return PreparedStatementBuilderImp
		 * @throws IllegalArgumentException
		 */
		@Override
		public PreparedStatementBuilderParam bindingParameters(Map<String, Object> parameters) throws IllegalArgumentException {
			return bindingParameters(SqlParametersFactory.ofMap(parameters));
		}

		/**
		 * Set binding parameters.
		 *
		 * @param parameters the list of parameters.
		 * @return PreparedStatementBuilderImp
		 */
		@Override
		public PreparedStatementBuilderImp bindingParameters(SqlParameters parameters) throws IllegalArgumentException {
			int paramCount = parameters != null ? parameters.size() : 0;
			ParsedParametersSql parsedParametersSql = getParsedSql(originalSql);
			if (parsedParametersSql.getTotalParameterCount() != 0) {
				if (parsedParametersSql.getNamedParameterCount() != 0) {
					if (!(parameters instanceof MapSqlParameters)) {
						throw new IllegalArgumentException(
								"SQL [" + originalSql + "]: required named parameters for binding");
					}
				} else if (!(parameters instanceof ArraySqlParameters)) {
					throw new IllegalArgumentException(
							"SQL [" + originalSql + "]: required unnamed parameters for binding");
				}
				if (parsedParametersSql.getUniqParameterCount() > paramCount) {
					throw new IllegalArgumentException(
							"SQL [" + originalSql + "]: given " + paramCount +
									" parameters but expected " + parsedParametersSql.getUniqParameterCount());

				}
			}
			this.parameters = parameters;
			return this;
		}

		/**
		 * Convert parameter declarations from an SqlParameterSource to a corresponding SqlBindingParameters.
		 * This is necessary in order to reuse existing methods on JdbcTemplate.
		 * The SqlParameter for a named parameter is placed in the correct position in the
		 * resulting list based on the parsed SQL statement info.
		 *
		 * @param paramSource the source for named parameters
		 */
		public SqlParameters buildSqlParametersArray(SqlParameters paramSource) {
			bindingParameters(paramSource);
			return buildSqlParametersArray();
		}

		/**
		 * Convert parameter declarations from an SqlParameterSource to a corresponding SqlBindingParameters.
		 * This is necessary in order to reuse existing methods on JdbcTemplate.
		 * The SqlParameter for a named parameter is placed in the correct position in the
		 * resulting list based on the parsed SQL statement info.
		 */
		public SqlParameters buildSqlParametersArray() {
			if (this.parameters != null && !this.parameters.isEmpty()) {
				final ArraySqlParameters result = ArraySqlParameters.of();
				ParsedParametersSql parsedParametersSql = getParsedSql(originalSql);
				if (parsedParametersSql.getNamedParameterCount() != 0) {
					parsedParametersSql.getNamedParameterStream()
							.forEach(param -> {
								String name = param.getParameterName();
								SqlParameterValue bindParamValue = this.parameters.get(name);
								assert bindParamValue != null : "SQL [" + originalSql + "]: required named parameter : " + name;
								SqlParameterValue declaredParamValue = getDeclaredParameterValue(name);
								result.addValue(createSqlParameterValue(declaredParamValue, bindParamValue));
							});

				} else {
					for (int index = 0; index < parsedParametersSql.getUnnamedParameterCount(); index++) {
						SqlParameterValue bindParamValue = parameters.get(index);
						assert bindParamValue != null : "SQL [" + originalSql + "]: required unnamed parameter in position: " + index;
						SqlParameterValue declaredParamValue = getDeclaredParameterValue(index);
						result.addValue(createSqlParameterValue(declaredParamValue, bindParamValue));
					}
				}
				if (parsedParametersSql.getTotalParameterCount() != result.size()) {
					throw new IllegalArgumentException(
							"SQL [" + originalSql + "]: given " + result.size() +
									" parameters but expected " + parsedParametersSql.getTotalParameterCount());
				}
				return result;
			} else {
				return null;
			}
		}

		private SqlParameterValue createSqlParameterValue(SqlParameterValue declared, SqlParameterValue binded) {
			return SqlParameterValue.of(
					declared != null && JdbcUtils.TYPE_UNKNOWN == declared.getSqlType() ? declared.getSqlType() : binded.getSqlType()
					, declared != null && declared.getTypeName() != null ? declared.getTypeName() : binded.getTypeName()
					, declared != null && declared.getScale() != null ? declared.getScale() : binded.getScale()
					, binded.getValue()
			);
		}

		@Override
		public String toString() {
			return "PreparedStatementBuilder: sql=[" + originalSql + "]; parameters=" + this.parameters;
		}
		//------------------------------------  PreparedStatementConfig ---------------------------------------

		/**
		 * Set whether to use prepared statements that return a specific type of ResultSet.
		 *
		 * @param resultSetType the ResultSet type
		 * @see java.sql.ResultSet#TYPE_FORWARD_ONLY
		 * @see java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE
		 * @see java.sql.ResultSet#TYPE_SCROLL_SENSITIVE
		 */
		@Override
		public PreparedStatementConfig resultSetType(int resultSetType) {
			this.resultSetType = resultSetType;
			return this;
		}

		/**
		 * Set whether to use prepared statements capable of returning updatable ResultSets.
		 */
		@Override
		public PreparedStatementConfig updatableResults(boolean updatableResults) {
			this.updatableResults = updatableResults;
			return this;
		}

		/**
		 * Set whether prepared statements should be capable of returning auto-generated keys.
		 */
		@Override
		public PreparedStatementConfig returnGeneratedKeys(boolean returnGeneratedKeys) {
			this.returnGeneratedKeys = returnGeneratedKeys;
			return this;
		}

		/**
		 * Set the column names of the auto-generated keys.
		 */
		@Override
		public PreparedStatementConfig generatedKeysColumnNames(String... names) {
			this.generatedKeysColumnNames = names;
			return this;
		}
		//------------------------------------  /PreparedStatementConfig ---------------------------------------

		//------------------------------------  PreparedStatementBuilder ---------------------------------------
		@Override
		public PreparedStatement buildPreparedStatement(Connection con) throws SQLException {
			PreparedStatement ps;
			String preparedSql = this.originalSql;
			ParsedParametersSql parsedParametersSql = getParsedSql(originalSql);
			if (this.parameters != null) {
				if (parsedParametersSql.getNamedParameterCount() != 0) {
					preparedSql = parsedParametersSql.substituteParameters(parameters);
				}
			} else if (parsedParametersSql.getTotalParameterCount() != 0) {
				throw new IllegalArgumentException(
						"SQL [" + originalSql + "]: given 0" +
								" parameters but expected " + parsedParametersSql.getTotalParameterCount());
			}

			if (generatedKeysColumnNames != null || returnGeneratedKeys) {
				if (generatedKeysColumnNames != null) {
					ps = con.prepareStatement(preparedSql, generatedKeysColumnNames);
				} else {
					ps = con.prepareStatement(preparedSql, PreparedStatement.RETURN_GENERATED_KEYS);
				}
			} else if (resultSetType == ResultSet.TYPE_FORWARD_ONLY && !updatableResults) {
				ps = con.prepareStatement(preparedSql);
			} else {
				ps = con.prepareStatement(preparedSql, resultSetType,
						updatableResults ? ResultSet.CONCUR_UPDATABLE : ResultSet.CONCUR_READ_ONLY);
			}
			if (parsedParametersSql.getTotalParameterCount() != 0) {
				setValues(ps);
			}
			return ps;
		}
		//------------------------------------  /PreparedStatementBuilder ---------------------------------------

		//------------------------------------  PreparedStatementSetter ---------------------------------------
		@Override
		public void setValues(PreparedStatement ps) throws SQLException {
			// Set arguments: Does nothing if there are no parameters.
			if (Objects.nonNull(this.parameters)) {
				SqlParameters parametersArray = this.parameters;
				ParsedParametersSql parsedParametersSql = getParsedSql(originalSql);
				if (parsedParametersSql.getNamedParameterCount() != 0) {
					parametersArray = buildSqlParametersArray();
				}
				parametersArray.stream().forEach(
						new Consumer<SqlParameterValue>() {
							int sqlColIndex = 1;

							@Override
							public void accept(SqlParameterValue parameter) {
								Object value = parameter.getValue();
								if (value instanceof Collection && parameter.getSqlType() != Types.ARRAY) {
									Collection<?> entries = (Collection<?>) value;
									for (Object entry : entries) {
										if (entry instanceof Object[]) {
											Object[] valueArray = (Object[]) entry;
											for (Object argValue : valueArray) {
												try {
													StatementCreatorUtils.setParameterValue(ps, sqlColIndex++, parameter, argValue);
												} catch (Exception ex) {
													Throwables.throwIfUnchecked(ex);
													throw new RuntimeException(ex);
												}
											}
										} else {
											try {
												StatementCreatorUtils.setParameterValue(ps, sqlColIndex++, parameter, entry);
											} catch (SQLException ex) {
												Throwables.throwIfUnchecked(ex);
												throw new RuntimeException(ex);
											}
										}
									}
								} else if (value instanceof Object[] || parameter.getSqlType() == Types.ARRAY) {
									assert value instanceof Object[] : "Parameters type by index [" + sqlColIndex + "] is declared as Array, but it is not Array by fact!";
									Object[] valueArray = (Object[]) value;
									for (Object argValue : valueArray) {
										try {
											StatementCreatorUtils.setParameterValue(ps, sqlColIndex++, parameter, argValue);
										} catch (Exception ex) {
											Throwables.throwIfUnchecked(ex);
											throw new RuntimeException(ex);
										}
									}
								} else {
									try {
										StatementCreatorUtils.setParameterValue(ps, sqlColIndex++, parameter, value);
									} catch (SQLException ex) {
										Throwables.throwIfUnchecked(ex);
										throw new RuntimeException(ex);
									}
								}
							}
						});
			}
		}
		//------------------------------------  /PreparedStatementSetter ---------------------------------------

		//------------------------------------  SqlProvider ---------------------------------------
		@Override
		public String getSql() {
			return originalSql;
		}
		//------------------------------------  /SqlProvider ---------------------------------------

		//------------------------------------  ParameterDisposer ---------------------------------------
		@Override
		public void cleanupParameters() {
			StatementCreatorUtils.cleanupParameters(this.parameters);
		}
		//------------------------------------  /ParameterDisposer ---------------------------------------
	}
}


