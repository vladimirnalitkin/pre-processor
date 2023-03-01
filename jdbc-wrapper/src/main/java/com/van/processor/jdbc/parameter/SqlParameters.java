package com.van.processor.jdbc.parameter;

import com.van.processor.common.Nullable;
import com.van.processor.jdbc.util.JdbcUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Parameters for binding in SQL statement.
 */
public interface SqlParameters {
	String UN_NAME_PARAMETER_NAME = "Parameter #";
	List<SqlParameter> EMPTY_PARAMS = new ArrayList<>();

	/**
	 * Constant that indicates an unknown (or unspecified) SQL type.
	 * To be returned from {@code getType} when no specific SQL type known.
	 *
	 * @see java.sql.Types
	 */
	int TYPE_UNKNOWN = JdbcUtils.TYPE_UNKNOWN;


	/**
	 * Add a parameter to this parameter source.
	 *
	 * @param name name of parameter
	 * @param value the value of the parameter
	 * @return a reference to this parameter source,
	 * so it's possible to chain several calls together
	 */
	SqlParameters addValue(String name, SqlParameterValue value);

	/**
	 * Determine whether there is a value for the specified named parameter.
	 *
	 * @param paramName the name of the parameter
	 * @return whether there is a value defined
	 */
	boolean hasValue(String paramName);

	/**
	 * Return the parameter value for the requested named parameter.
	 *
	 * @param paramName the name of the parameter
	 * @return the value of the specified parameter
	 * @throws IllegalArgumentException if there is no value for the requested parameter
	 */
	@Nullable
	Object getValue(String paramName) throws IllegalArgumentException;

	/**
	 * Return the parameter value for the requested parameter position.
	 *
	 * @param parameterPosition the name of the parameter
	 * @return the value of the specified parameter
	 * @throws IllegalArgumentException if there is no value for the requested parameter
	 */
	@Nullable
	default Object getValue(int parameterPosition) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the SqlParameterValue by the index.
	 *
	 * @param parameterPosition the index of the parameter
	 * @return SqlParameterValue,
	 * or {@code null} if not known
	 */
	@Nullable
	default SqlParameterValue get(int parameterPosition) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the SqlParameterValue by the name.
	 *
	 * @param paramName the name of the parameter
	 * @return SqlParameterValue,
	 * or {@code null} if not known
	 */
	@Nullable
	default SqlParameterValue get(String paramName) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the List of SqlParameters.
	 *
	 * @return List<SqlParameter>,
	 * or {@code null} if not known
	 */
	@Nullable
	default List<SqlParameter> getList() throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the stream of SqlParameters.
	 *
	 * @return Stream<SqlParameter>,
	 * or {@code null} if not known
	 */
	default Stream<SqlParameterValue> stream() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Extract all available parameter names if possible.
	 * <p>This is an optional operation, primarily for use with
	 *
	 * @return the array of parameter names, or {@code null} if not determinable
	 * @since 5.0.3
	 */
	@Nullable
	default String[] getNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the stream of SqlParameters names.
	 *
	 * @return Stream<SqlParameter>,
	 * or {@code null} if not known
	 */
	default Stream<String> namesStream() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Extract all values.
	 */
	@Nullable
	default Object[] getValues() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Is it empty or not.
	 *
	 * @return yes/no
	 */
	default boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the number of paramaters.
	 *
	 * @return int
	 */
	int size();
}

