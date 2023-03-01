package com.van.processor.jdbc.parameter;

import com.van.processor.common.Nullable;

import java.util.Map;
import java.util.Objects;

public abstract class SqlParametersFactory {
	/**
	 * Create a new ArraySqlBindingParameters, with array of values
	 * comprised of the supplied arguments.
	 *
	 * @param values the values of the parameters
	 */
	public static SqlParameters ofValues(Object... values) {
		return ArraySqlParameters.ofValues(values);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with array of values
	 * comprised of the supplied arguments.
	 *
	 * @param values the values of the parameters
	 */
	public static SqlParameters ofArray(Object[] values, int[] sqlTypes) {
		return ArraySqlParameters.of(values, sqlTypes);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with array of types
	 * comprised of the supplied arguments.
	 *
	 * @param sqlTypes the values of the parameters
	 */
	public static SqlParameters ofTypes(int... sqlTypes) {
		return ArraySqlParameters.ofTypes(sqlTypes);
	}

	/**
	 * Create a new MapSqlParameterSource based on a Map.
	 *
	 * @param values a Map holding existing parameter values (can be {@code null})
	 */
	public static SqlParameters ofMap(@Nullable Map<String, ?> values) {
		return Objects.isNull(values) ? null : MapSqlParameters.of(values);
	}

	/**
	 * Create a new ArraySqlBindingParameters.
	 */
	public static SqlParameters ofSize(int size) {
		return ArraySqlParameters.ofSize(size);
	}

	/**
	 * Create a new MapSqlParameterSource based on a name, value.
	 * @param paramName - name in Map
	 * @param value - value in Map
	 * @return SqlParameters
	 */
	public static SqlParameters ofMap(String paramName, @Nullable Object value) {
		return MapSqlParameters.of(paramName, value);
	}

}
