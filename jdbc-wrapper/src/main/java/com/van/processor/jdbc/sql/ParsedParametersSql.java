package com.van.processor.jdbc.sql;

import com.van.processor.common.Nullable;
import com.van.processor.jdbc.parameter.SqlParameters;

import java.util.stream.Stream;

public interface ParsedParametersSql {
    char DEF_JDBC_PARAMETER = '?';
    String DEF_DELIMITER = ", ";

	/**
	 * Return the original SQL.
	 * @return String
	 */
    String getOriginalSql();

	/**
	 * Only named parameters can be streaming.
	 * @return stream of named parameters.
	 */
	Stream<ParameterHolder> getNamedParameterStream();

	/**
	 * Only named parameter keep in ParameterHolder class.
	 * @param parameterPosition position
	 * @return ParameterHolder
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
    ParameterHolder getNamedParameter(int parameterPosition);

	/**
	 * Return the count of named parameters in the SQL statement.
	 * Each parameter name counts once; repeated occurrences do not count here.
	 */
	int getNamedParameterCount();

	/**
	 * Return the count of all of the unnamed parameters in the SQL statement.
	 */
    int getUnnamedParameterCount();

	/**
	 * Return the total count of all of the parameters in the SQL statement.
	 * Repeated occurrences of the same parameter name do count here.
	 */
	int getTotalParameterCount();

	/**
	 * Return the count of uniq parameters in the SQL statement.
	 */
	int getUniqParameterCount();

	/**
	 * Parse the SQL statement and locate any placeholders or named parameters. Named
	 * parameters are substituted for a JDBC placeholder, and any select list is expanded
	 * to the required number of placeholders. Select lists may contain an array of
	 * objects, and in that case the placeholders will be grouped and enclosed with
	 * parentheses. This allows for the use of "expression lists" in the SQL statement
	 * like: <br /><br />
	 * {@code select id, name, state from table where (name, age) in (('John', 35), ('Ann', 50))}
	 * <p>The parameter values passed in are used to determine the number of placeholders to
	 * be used for a select list. Select lists should be limited to 100 or fewer elements.
	 * A larger number of elements is not guaranteed to be supported by the database and
	 * is strictly vendor-dependent.
	 *
	 * @param paramSource the source for named parameters
	 * @return the SQL statement with substituted parameters
	 */
    String substituteParameters(@Nullable SqlParameters paramSource);

}
