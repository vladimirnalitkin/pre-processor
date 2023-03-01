package com.van.processor.jdbc.parameter;

import com.van.processor.common.Nullable;
import com.van.processor.jdbc.util.JdbcUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArraySqlParameters implements SqlParameters {
	private static final int EXT_SIZE = 5;
	private SqlParameterValue[] parameters;
	private int lastEmptyPosition = 0;

	private ArraySqlParameters() {
		parameters = new SqlParameterValue[EXT_SIZE];
	}

	private ArraySqlParameters(int size) {
		parameters = new SqlParameterValue[size];
	}

	@Override
	public boolean hasValue(String paramName) {
		throw new UnsupportedOperationException("Array Sql parameters does not support hasValue function");
	}

	@Override
	public Object getValue(String paramName) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Array Sql parameters does not support getValue by name");
	}

	@Override
	public SqlParameterValue get(int parameterPosition) throws IllegalArgumentException {
		if (parameterPosition > -1 && lastEmptyPosition > parameterPosition) {
			return parameters[parameterPosition];
		} else {
			throw new IllegalArgumentException("No value registered on position [" + parameterPosition + "]");
		}
	}

	@Override
	public Object getValue(int parameterPosition) throws IllegalArgumentException {
		SqlParameterValue sqlParameterValue = get(parameterPosition);
		return sqlParameterValue != null ? sqlParameterValue.getValue() : null;
	}

	@Override
	public boolean isEmpty() {
		return lastEmptyPosition == 0;
	}

	@Override
	public int size() {
		return lastEmptyPosition;
	}

	@Override
	@Nullable
	public String[] getNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nullable
	public Object[] getValues() {
		if (!isEmpty()) {
			final int length = size();
			final Object[] result = new Object[length];
			for (int i = 0; i < length; i++) {
				result[i] = parameters[i] != null ? parameters[i].getValue() : null;
			}
			return result;
		} else {
			return new Object[0];
		}
	}

	@Override
	@Nullable
	public List<SqlParameter> getList() throws IllegalArgumentException {
		return !isEmpty() ? stream()
				.map(parameter -> SqlParameter.of((String) null, parameter))
				.collect(Collectors.toList())
				: EMPTY_PARAMS;
	}

	/**
	 * Create a new ArraySqlBindingParameters.
	 */
	public static ArraySqlParameters of() {
		return new ArraySqlParameters();
	}

	/**
	 * Create a new ArraySqlBindingParameters.
	 */
	public static ArraySqlParameters ofSize(int size) {
		return new ArraySqlParameters(size);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param value the value of the parameter
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters of(@Nullable Object value) {
		return new ArraySqlParameters().addValue(value);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param value   the value of the parameter
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters of(int sqlType, @Nullable Object value) {
		return new ArraySqlParameters().addValue(sqlType, value);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param value    the value of the parameter
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters of(int sqlType, String typeName, @Nullable Object value) {
		return new ArraySqlParameters().addValue(sqlType, typeName, value);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with one value
	 * comprised of the supplied arguments.
	 *
	 * @param value    the value of the parameter
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 * @param scale    scale of paramater
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters of(int sqlType, String typeName, Integer scale, @Nullable Object value) {
		return new ArraySqlParameters().addValue(sqlType, typeName, scale, value);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with array of values
	 * comprised of the supplied arguments.
	 *
	 * @param values the values of the parameters
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters ofValues(Object... values) {
		return new ArraySqlParameters().addValues(values);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with array of values and types
	 * comprised of the supplied arguments.
	 *
	 * @param values the values of the parameters
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters of(Object[] values, int[] sqlTypes) {
		return new ArraySqlParameters().addValues(values, sqlTypes);
	}

	/**
	 * Create a new ArraySqlBindingParameters, with array of values and types
	 * comprised of the supplied arguments.
	 *
	 * @param sqlTypes the types of the parameters
	 * @see #addValue(SqlParameterValue)
	 */
	public static ArraySqlParameters ofTypes(int... sqlTypes) {
		return new ArraySqlParameters().addTypes(sqlTypes);
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param value the value of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	public ArraySqlParameters addValue(@Nullable Object value) {
		return (value instanceof SqlParameterValue) ? addValue((SqlParameterValue) value) : addValue(SqlParameterValue.of(value));
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param value   the value of the parameter
	 * @param sqlType the SQL type of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	public ArraySqlParameters addValue(int sqlType, @Nullable Object value) {
		return addValue(sqlType, null, null, value);
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param value    the value of the parameter
	 * @param sqlType  the SQL type of the parameter
	 * @param typeName the type name of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	public ArraySqlParameters addValue(int sqlType, String typeName, @Nullable Object value) {
		return addValue(sqlType, typeName, null, value);
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param value    the value of the parameter
	 * @param sqlType  the SQL type of the parameter
	 * @param typeName the type name of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	public ArraySqlParameters addValue(int sqlType, String typeName, Integer scale, @Nullable Object value) {
		return addValue(SqlParameterValue.of(sqlType, typeName, scale, value));
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param value the value of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public ArraySqlParameters addValue(SqlParameterValue value) {
		if (lastEmptyPosition == parameters.length) {
			parameters = Arrays.copyOf(parameters, parameters.length + EXT_SIZE);
		}
		parameters[lastEmptyPosition++] = value;
		return this;
	}

	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param value the value of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	@Override
	public ArraySqlParameters addValue(String name, SqlParameterValue value) {
		return addValue(value);
	}

	/**
	 * Add a Array of parameters to this parameter source.
	 *
	 * @param values parameter values (can be {@code null})
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	public ArraySqlParameters addValues(Object[] values) {
		if (values != null) {
			for (Object value : values) {
				addValue(value);
			}
		}
		return this;
	}

	/**
	 * Add a Array of parameters to this parameter source.
	 *
	 * @param values parameter values (can be {@code null})
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 * @see #addValue(SqlParameterValue)
	 */
	public ArraySqlParameters addValues(Object[] values, int[] sqlTypes) {
		if (values != null || sqlTypes != null) {
			if (values != null && sqlTypes != null) {
				int i = 0;
				if (values.length > sqlTypes.length) {
					for (Object value : values) {
						addValue(i < sqlTypes.length ? sqlTypes[i++] : JdbcUtils.TYPE_UNKNOWN, value);
					}
				} else {
					for (int type : sqlTypes) {
						addValue(type, i < values.length ? values[i++] : null);
					}
				}

			} else if (values != null) {
				addValues(values);
			} else {
				addTypes(sqlTypes);
			}
		}
		return this;
	}

	/**
	 * Add a Array of types of parameters to this parameter source.
	 *
	 * @param sqlTypes array of parameters type
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	public ArraySqlParameters addTypes(int... sqlTypes) {
		if (sqlTypes != null) {
			for (int type : sqlTypes) {
				addValue(type, null);
			}
		}
		return this;
	}

	@Override
	public Stream<SqlParameterValue> stream() {
		return Arrays.stream(parameters, 0, lastEmptyPosition);
	}
}
