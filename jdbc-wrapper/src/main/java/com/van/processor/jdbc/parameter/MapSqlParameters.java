package com.van.processor.jdbc.parameter;

import com.google.common.collect.Maps;
import com.van.processor.common.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation binding parameters based on Map.
 */
public class MapSqlParameters implements SqlParameters {

	private final Map<String, SqlParameterValue> parameterMap = new HashMap<>();

	/**
	 * Create an empty MapSqlParameterSource,
	 * with values to be added via {@code addValue}.
	 *
	 * @see #addValue(String, Object)
	 */
	private MapSqlParameters() {
	}

	/**
	 * Create a new MapSqlParameterSource, with one value
	 * comprised of the supplied arguments.
	 *
	 * @see #addValue(String, Object)
	 */
	public static MapSqlParameters of() {
		return new MapSqlParameters();
	}

	/**
	 * Create a new MapSqlParameterSource, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @see #addValue(String, Object)
	 */
	public static MapSqlParameters of(String paramName, @Nullable Object value) {
		return new MapSqlParameters().addValue(paramName, value);
	}

	/**
	 * Create a new MapSqlParameterSource, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @param sqlType   the SQL type of the parameter according to {@code java.sql.Types}
	 * @see #addValue(String, int, Object)
	 */
	public static MapSqlParameters of(String paramName, int sqlType, @Nullable Object value) {
		return new MapSqlParameters().addValue(paramName, sqlType, value);
	}

	/**
	 * Create a new MapSqlParameterSource, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @param sqlType   the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName  the type name of the parameter (optional)
	 * @see #addValue(String, int, String, Object)
	 */
	public static MapSqlParameters of(String paramName, int sqlType, String typeName, @Nullable Object value) {
		return new MapSqlParameters().addValue(paramName, sqlType, typeName, value);
	}

	/**
	 * Create a new MapSqlParameterSource, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @param sqlType   the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName  the type name of the parameter (optional)
	 * @param scale     the number of digits after the decimal point
	 *                  (for DECIMAL and NUMERIC types)
	 * @see #addValue(String, int, String, Integer, Object)
	 */
	public static MapSqlParameters of(String paramName, int sqlType, String typeName, Integer scale, @Nullable Object value) {
		return new MapSqlParameters().addValue(paramName, sqlType, typeName, scale, value);
	}

	/**
	 * Create a new MapSqlParameterSource based on a Map.
	 *
	 * @param values a Map holding existing parameter values (can be {@code null})
	 */
	public static MapSqlParameters of(@Nullable Map<String, ?> values) {
		return new MapSqlParameters().addValues(values);
	}

	@Override
	public boolean isEmpty() {
		return parameterMap.isEmpty();
	}

	@Override
	public int size() {
		return parameterMap.entrySet().size();
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameters addValue(String paramName, @Nullable Object value) {
		return addValue(paramName, SqlParameterValue.of(value));
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @param sqlType   the SQL type of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameters addValue(String paramName, int sqlType, @Nullable Object value) {
		return addValue(paramName, SqlParameterValue.of(sqlType, value));
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @param sqlType   the SQL type of the parameter
	 * @param typeName  the type name of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameters addValue(String paramName, int sqlType, String typeName, @Nullable Object value) {
		return addValue(paramName, SqlParameterValue.of(sqlType, typeName, value));
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param paramName the name of the parameter
	 * @param value     the value of the parameter
	 * @param sqlType   the SQL type of the parameter
	 * @param typeName  the type name of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameters addValue(String paramName, int sqlType, String typeName, Integer scale, @Nullable Object value) {
		return addValue(paramName, SqlParameterValue.of(sqlType, typeName, scale, value));
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param paramName the name of the parameter
	 * @param value     value of parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	@Override
	public MapSqlParameters addValue(String paramName, SqlParameterValue value) {
		assert paramName != null : "Parameter name must not be null";
		parameterMap.put(paramName, value);
		return this;
	}

	/**
	 * Add a Map of parameters to this parameter source.
	 *
	 * @param values a Map holding existing parameter values (can be {@code null})
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public MapSqlParameters addValues(@Nullable Map<String, ?> values) {
		if (values != null) {
			values.forEach(this::addValue);
		}
		return this;
	}

	/**
	 * Expose the current parameter values as read-only Map.
	 */
	public Map<String, Object> getValuesMap() {
		return Maps.transformValues(this.parameterMap, val -> {
			assert val != null;
			return val.getValue();
		});
	}

	@Override
	public boolean hasValue(String paramName) {
		assert paramName != null : "Parameter name must not be null";
		return this.parameterMap.containsKey(paramName);
	}

	@Override
	@Nullable
	public Object getValue(String paramName) {
		if (!hasValue(paramName)) {
			throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
		}
		return this.parameterMap.get(paramName).getValue();
	}

	@Override
	public SqlParameterValue get(String paramName) {
		if (!hasValue(paramName)) {
			throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
		}
		return this.parameterMap.get(paramName);
	}

	@Override
	@Nullable
	public String[] getNames() {
		return !isEmpty() ? this.parameterMap.keySet().toArray(new String[0]) : new String[0];
	}

	@Override
	public Stream<String> namesStream() {
		return this.parameterMap.keySet().stream();
	}

	@Override
	@Nullable
	public Object[] getValues() {
		return !isEmpty() ? this.parameterMap.values().toArray(new Object[0]) : new Object[0];
	}

	@Override
	@Nullable
	public List<SqlParameter> getList() throws IllegalArgumentException {
		return !isEmpty() ? stream()
				.map(parameter -> SqlParameter.of(UN_NAME_PARAMETER_NAME, parameter))
				.collect(Collectors.toList()) : EMPTY_PARAMS;
	}

	@Override
	public Stream<SqlParameterValue> stream() {
		return this.parameterMap.values().stream();
	}
}

