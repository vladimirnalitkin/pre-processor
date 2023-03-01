package com.van.processor.jdbc.statement;

public interface PreparedStatementConfig {
	/**
	 * Set whether to use prepared statements that return a specific type of ResultSet.
	 *
	 * @param resultSetType the ResultSet type
	 * @see java.sql.ResultSet#TYPE_FORWARD_ONLY
	 * @see java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE
	 * @see java.sql.ResultSet#TYPE_SCROLL_SENSITIVE
	 */
	PreparedStatementConfig resultSetType(int resultSetType);

	/**
	 * Set whether to use prepared statements capable of returning updatable ResultSets.
	 */
	PreparedStatementConfig updatableResults(boolean updatableResults);

	/**
	 * Set whether prepared statements should be capable of returning auto-generated keys.
	 */
	PreparedStatementConfig returnGeneratedKeys(boolean returnGeneratedKeys);

	/**
	 * Set the column names of the auto-generated keys.
	 */
	PreparedStatementConfig generatedKeysColumnNames(String... names);
}
