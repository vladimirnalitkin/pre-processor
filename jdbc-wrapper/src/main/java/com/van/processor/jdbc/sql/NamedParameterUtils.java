package com.van.processor.jdbc.sql;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.InvalidDataAccessApiUsageException;
import com.van.processor.jdbc.parameter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class NamedParameterUtils {

	public final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Set of characters that qualify as comment or quotes starting characters.
	 */
	private static final String[] START_SKIP = new String[]{"'", "\"", "--", "/*"};

	/**
	 * Set of characters that at are the corresponding comment or quotes ending characters.
	 */
	private static final String[] STOP_SKIP = new String[]{"'", "\"", "\n", "*/"};

	/**
	 * Set of characters that qualify as parameter separators,
	 * indicating that a parameter name in a SQL String has ended.
	 */
	private static final String PARAMETER_SEPARATORS = "\"':&,;()|=+-*%/\\<>^";

	/**
	 * An index with separator flags per character code.
	 * Technically only needed between 34 and 124 at this point.
	 */
	private static final boolean[] separatorIndex = new boolean[128];

	static {
		for (char c : PARAMETER_SEPARATORS.toCharArray()) {
			separatorIndex[c] = true;
		}
	}

	//-------------------------------------------------------------------------
	// Core methods used by NamedParameterJdbcTemplate and SqlQuery/SqlUpdate
	//-------------------------------------------------------------------------

	static int addNamedParameter(List<ParameterHolder> parameterList, int totalParameterCount, int escapes, int i, int j, String parameter) {
		parameterList.add(ParameterHolder.of(parameter, i - escapes, j - escapes));
		totalParameterCount++;
		return totalParameterCount;
	}

	static int addNewNamedParameter(Set<String> namedParameters, int namedParameterCount, String parameter) {
		if (!namedParameters.contains(parameter)) {
			namedParameters.add(parameter);
			namedParameterCount++;
		}
		return namedParameterCount;
	}

	/**
	 * Skip over comments and quoted names present in an SQL statement.
	 *
	 * @param statement character array containing SQL statement
	 * @param position  current position of statement
	 * @return next position to process after any comments or quotes are skipped
	 */
	static int skipCommentsAndQuotes(char[] statement, int position) {
		for (int i = 0; i < START_SKIP.length; i++) {
			if (statement[position] == START_SKIP[i].charAt(0)) {
				boolean match = true;
				for (int j = 1; j < START_SKIP[i].length(); j++) {
					if (statement[position + j] != START_SKIP[i].charAt(j)) {
						match = false;
						break;
					}
				}
				if (match) {
					int offset = START_SKIP[i].length();
					for (int m = position + offset; m < statement.length; m++) {
						if (statement[m] == STOP_SKIP[i].charAt(0)) {
							boolean endMatch = true;
							int endPos = m;
							for (int n = 1; n < STOP_SKIP[i].length(); n++) {
								if (m + n >= statement.length) {
									// last comment not closed properly
									return statement.length;
								}
								if (statement[m + n] != STOP_SKIP[i].charAt(n)) {
									endMatch = false;
									break;
								}
								endPos = m + n;
							}
							if (endMatch) {
								// found character sequence ending comment or quote
								return endPos + 1;
							}
						}
					}
					// character sequence ending comment or quote not found
					return statement.length;
				}
			}
		}
		return position;
	}


	/**
	 * Convert a bindingParams according parsedParametersSql.
	 *
	 * @param parsedParametersSql the parsed SQL statement
	 * @param bindingParams       the source for named parameters
	 * @param declaredParams      the List of declared SqlParameter objects
	 *                            (may be {@code null}). If specified, the parameter metadata will
	 *                            be built into the value array in the form of SqlParameterValue objects.
	 * @return SqlBindingParameters
	 */
	public static SqlParameters preparedBindingNamedParameters(ParsedParametersSql parsedParametersSql
			, SqlParameters bindingParams
			, @Nullable SqlParameters declaredParams) {
		namedAndUnnamedParameterNotMix(parsedParametersSql);
		assert bindingParams != null : "Binding parameters should not be null";
		final SqlParameters result = SqlParametersFactory.ofSize(parsedParametersSql.getNamedParameterCount());
		parsedParametersSql.getNamedParameterStream().forEach(parameterHolder -> {
			String paramName = parameterHolder.getParameterName();
			try {
				result.addValue(null,
						SqlParameterValue.of(Objects.requireNonNull((declaredParams != null ? declaredParams : bindingParams).get(paramName))
								, bindingParams.getValue(paramName))
				);
			} catch (IllegalArgumentException ex) {
				throw new InvalidDataAccessApiUsageException(
						"No value supplied for the SQL parameter '" + paramName + "': " + ex.getMessage());
			}
		});
		return result;
	}

	/**
	 * Determine whether a parameter name ends at the current position,
	 * that is, whether the given character qualifies as a separator.
	 */
	static boolean isParameterSeparator(char c) {
		return (c < 128 && separatorIndex[c]) || Character.isWhitespace(c);
	}

	static void namedAndUnnamedParameterNotMix(ParsedParametersSql parsedParametersSql) {
		if (parsedParametersSql.getNamedParameterCount() > 0 && parsedParametersSql.getUnnamedParameterCount() > 0) {
			throw new InvalidDataAccessApiUsageException(
					"Not allowed to mix named and traditional ? placeholders. You have " +
							parsedParametersSql.getNamedParameterCount() + " named parameter(s) and " +
							parsedParametersSql.getUnnamedParameterCount() + " traditional placeholder(s) in statement: " +
							parsedParametersSql.getOriginalSql());
		}
	}
}

