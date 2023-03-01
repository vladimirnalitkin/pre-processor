package com.van.processor.jdbc.sql;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.InvalidDataAccessApiUsageException;
import com.van.processor.jdbc.parameter.ArraySqlParameters;
import com.van.processor.jdbc.parameter.MapSqlParameters;
import com.van.processor.jdbc.parameter.SqlParameterValue;
import com.van.processor.jdbc.parameter.SqlParameters;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParsedParametersSqlFactory {
	/**
	 * Default maximum number of entries for this template's SQL cache: 256.
	 */
	private final int DEFAULT_CACHE_LIMIT = 256;

	private volatile int cacheLimit = DEFAULT_CACHE_LIMIT;

	/**
	 * Cache of original SQL String to ParsedSql representation.
	 */
	@SuppressWarnings("serial")
	private final Map<String, ParsedParametersSql> parsedSqlCache =
			new LinkedHashMap<String, ParsedParametersSql>(DEFAULT_CACHE_LIMIT, 0.75f, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<String, ParsedParametersSql> eldest) {
					return size() > getCacheLimit();
				}
			};

	private static final ParsedParametersSqlFactory INSTANCE = new ParsedParametersSqlFactory();

	private ParsedParametersSqlFactory() {
	}

	/**
	 * Specify the maximum number of entries for this template's SQL cache.
	 * Default is 256.
	 */
	public static void setCacheLimit(int cacheLimit) {
		INSTANCE.cacheLimit = cacheLimit;
	}

	/**
	 * Return the maximum number of entries for this template's SQL cache.
	 */
	public static int getCacheLimit() {
		return INSTANCE.cacheLimit;
	}

	/**
	 * Static method for parsing and caching SQL with parameters.
	 *
	 * @param sql - SQL string
	 * @return instance of ParsedParametersSql object
	 */
	public static ParsedParametersSql getParsedSql(String sql) {
		return getParsedSql(sql, null);
	}

	/**
	 * Static method for parsing and caching SQL with parameters.
	 *
	 * @param sql - SQL string
	 * @return instance of ParsedParametersSql object
	 */
	public static ParsedParametersSql getParsedSql(String sql, SqlParameters declaredParams) {
		if (INSTANCE.cacheLimit <= 0) {
			return ParsedParametersSqlImp.of(sql, declaredParams);
		}
		synchronized (INSTANCE) {
			ParsedParametersSql parsedParametersSql = INSTANCE.parsedSqlCache.get(sql);
			if (parsedParametersSql == null) {
				parsedParametersSql = ParsedParametersSqlImp.of(sql, declaredParams);
				INSTANCE.parsedSqlCache.put(sql, parsedParametersSql);
			}
			return parsedParametersSql;
		}
	}

	/**
	 * Parse the SQL statement and locate any placeholders or named parameters.
	 * Named parameters are substituted for a JDBC placeholder and any select list
	 * is expanded to the required number of placeholders.
	 * <p>This is a shortcut version of
	 *
	 * @param sql           the SQL statement
	 * @param bindingParams the binding named parameters
	 * @return the SQL statement with substituted parameters
	 */
	public static String substituteNamedParameters(String sql, Map<String, Object> bindingParams) {
		return getParsedSql(sql).substituteParameters(MapSqlParameters.of(bindingParams));
	}

	/**
	 * Parse the SQL statement and locate any placeholders or named parameters.
	 * Named parameters are substituted for a JDBC placeholder and any select list
	 * is expanded to the required number of placeholders.
	 * <p>This is a shortcut version of
	 *
	 * @param sql           the SQL statement
	 * @param bindingParams the binding named parameters
	 * @return the SQL statement with substituted parameters
	 */
	public static String substituteNamedParameters(String sql, SqlParameters bindingParams) {
		return getParsedSql(sql).substituteParameters(bindingParams);
	}

	/**
	 * Parse the SQL statement and locate any placeholders or named parameters.
	 * Named parameters are substituted for a JDBC placeholder and any select list
	 * is expanded to the required number of placeholders.
	 * <p>This is a shortcut version of
	 *
	 * @param sql            the SQL statement
	 * @param declaredParams the declared named parameters
	 * @param bindingParams  the binding named parameters
	 * @return the SQL statement with substituted parameters
	 */
	public static String substituteNamedParameters(String sql, SqlParameters declaredParams, SqlParameters bindingParams) {
		return getParsedSql(sql, declaredParams).substituteParameters(bindingParams);
	}

	/**
	 * Inner class implemented the ParsedParametersSql interface.
	 */
	public static class ParsedParametersSqlImp implements ParsedParametersSql {

		private final String originalSql;

		private List<ParameterHolder> parameters = new LinkedList<>();

		private int namedParameterCount;

		private int unnamedParameterCount;

		private int totalParameterCount;

		private int uniqParameterCount;


		/**
		 * Create a new instance of the {@link ParsedParametersSql} class.
		 *
		 * @param originalSql the SQL statement that is being (or is to be) parsed
		 */
		ParsedParametersSqlImp(String originalSql) {
			this.originalSql = originalSql;
		}

		/**
		 * Parse the SQL statement and locate any placeholders or named parameters.
		 * Named parameters are substituted for a JDBC placeholder.
		 *
		 * @param sql the SQL statement
		 * @return the parsed statement, represented as ParsedSql instance
		 */
		static ParsedParametersSql of(final String sql) {
			return of(sql, null);
		}

		/**
		 * Parse the SQL statement and locate any placeholders or named parameters.
		 * Named parameters are substituted for a JDBC placeholder.
		 *
		 * @param sql the SQL statement
		 * @return the parsed statement, represented as ParsedSql instance
		 */
		static ParsedParametersSql of(final String sql, final SqlParameters declaredParams) {
			assert sql != null : "SQL must not be null";

			Set<String> namedParameters = new HashSet<>();
			String sqlToUse = sql;
			List<ParameterHolder> parameterList = new ArrayList<>();

			char[] statement = sql.toCharArray();
			int namedParameterCount = 0;
			int unnamedParameterCount = 0;
			int totalParameterCount = 0;

			int escapes = 0;
			int i = 0;
			while (i < statement.length) {
				int skipToPosition = i;
				while (i < statement.length) {
					skipToPosition = NamedParameterUtils.skipCommentsAndQuotes(statement, i);
					if (i == skipToPosition) {
						break;
					} else {
						i = skipToPosition;
					}
				}
				if (i >= statement.length) {
					break;
				}
				char c = statement[i];
				if (c == ':' || c == '&') {
					int j = i + 1;
					if (c == ':' && j < statement.length && statement[j] == ':') {
						// Postgres-style "::" casting operator should be skipped
						i = i + 2;
						continue;
					}
					String parameter = null;
					if (c == ':' && j < statement.length && statement[j] == '{') {
						// :{x} style parameter
						while (statement[j] != '}') {
							j++;
							if (j >= statement.length) {
								throw new InvalidDataAccessApiUsageException("Non-terminated named parameter declaration " +
										"at position " + i + " in statement: " + sql);
							}
							if (statement[j] == ':' || statement[j] == '{') {
								throw new InvalidDataAccessApiUsageException("Parameter name contains invalid character '" +
										statement[j] + "' at position " + i + " in statement: " + sql);
							}
						}
						if (j - i > 2) {
							parameter = sql.substring(i + 2, j);
							namedParameterCount = NamedParameterUtils.addNewNamedParameter(namedParameters, namedParameterCount, parameter);
							totalParameterCount = NamedParameterUtils.addNamedParameter(parameterList, totalParameterCount, escapes, i, j + 1, parameter);
						}
						j++;
					} else {
						while (j < statement.length && !NamedParameterUtils.isParameterSeparator(statement[j])) {
							j++;
						}
						if (j - i > 1) {
							parameter = sql.substring(i + 1, j);
							namedParameterCount = NamedParameterUtils.addNewNamedParameter(namedParameters, namedParameterCount, parameter);
							totalParameterCount = NamedParameterUtils.addNamedParameter(parameterList, totalParameterCount, escapes, i, j, parameter);
						}
					}
					i = j - 1;
				} else {
					if (c == '\\') {
						int j = i + 1;
						if (j < statement.length && statement[j] == ':') {
							// escaped ":" should be skipped
							sqlToUse = sqlToUse.substring(0, i - escapes) + sqlToUse.substring(i - escapes + 1);
							escapes++;
							i = i + 2;
							continue;
						}
					}
					if (c == '?') {
						int j = i + 1;
						if (j < statement.length && (statement[j] == '?' || statement[j] == '|' || statement[j] == '&')) {
							// Postgres-style "??", "?|", "?&" operator should be skipped
							i = i + 2;
							continue;
						}
						unnamedParameterCount++;
						totalParameterCount++;
					}
				}
				i++;
			}
			ParsedParametersSqlImp parsedParametersSql = new ParsedParametersSqlImp(sqlToUse);
			for (ParameterHolder ph : parameterList) {
				parsedParametersSql.addNamedParameter(ph.getParameterName(), ph.getStartIndex(), ph.getEndIndex());
			}
			parsedParametersSql.setNamedParameterCount(namedParameterCount);
			parsedParametersSql.setUnnamedParameterCount(unnamedParameterCount);
			parsedParametersSql.setUniqParameterCount(namedParameters.size() != 0 ? namedParameters.size() : unnamedParameterCount);
			parsedParametersSql.setTotalParameterCount(totalParameterCount);

			if (namedParameterCount > 0 && unnamedParameterCount > 0) {
				throw new InvalidDataAccessApiUsageException(
						"Not allowed to mix named and traditional ? placeholders. You have " +
								namedParameterCount + " named parameter(s) and " +
								unnamedParameterCount + " traditional placeholder(s) in statement: " +
								sql);
			}
			return parsedParametersSql;
		}

		/**
		 * Return the SQL statement that is being parsed.
		 */
		@Override
		public String getOriginalSql() {
			return this.originalSql;
		}


		/**
		 * Add a named parameter parsed from this SQL statement.
		 *
		 * @param parameterName the name of the parameter
		 * @param startIndex    the start index in the original SQL String
		 * @param endIndex      the end index in the original SQL String
		 */
		void addNamedParameter(String parameterName, int startIndex, int endIndex) {
			this.parameters.add(ParameterHolder.of(parameterName, startIndex, endIndex));
		}

		/**
		 * Return all of the parameters as stream.
		 * Repeated occurrences of the same parameter name are included here.
		 */
		@Override
		public Stream<ParameterHolder> getNamedParameterStream() {
			return this.parameters.stream();
		}

		/**
		 * Return the parameter indexes for the specified parameter.
		 *
		 * @param parameterPosition the position of the parameter
		 *                          (as index in the parameter names List)
		 * @return the start index and end index, combined into
		 * a int array of length 2
		 */
		@Override
		public ParameterHolder getNamedParameter(int parameterPosition) {
			return this.parameters.get(parameterPosition);
		}

		/**
		 * Set the count of named parameters in the SQL statement.
		 * Each parameter name counts once; repeated occurrences do not count here.
		 */
		void setNamedParameterCount(int namedParameterCount) {
			this.namedParameterCount = namedParameterCount;
		}

		/**
		 * Return the count of named parameters in the SQL statement.
		 * Each parameter name counts once; repeated occurrences do not count here.
		 */
		@Override
		public int getNamedParameterCount() {
			return this.namedParameterCount;
		}

		/**
		 * Set the count of all of the unnamed parameters in the SQL statement.
		 */
		void setUnnamedParameterCount(int unnamedParameterCount) {
			this.unnamedParameterCount = unnamedParameterCount;
		}

		/**
		 * Return the count of all of the unnamed parameters in the SQL statement.
		 */
		@Override
		public int getUnnamedParameterCount() {
			return this.unnamedParameterCount;
		}

		/**
		 * Set the total count of all of the parameters in the SQL statement.
		 * Repeated occurrences of the same parameter name do count here.
		 */
		void setTotalParameterCount(int totalParameterCount) {
			this.totalParameterCount = totalParameterCount;
		}

		/**
		 * Return the total count of all of the parameters in the SQL statement.
		 * Repeated occurrences of the same parameter name do count here.
		 */
		@Override
		public int getTotalParameterCount() {
			return this.totalParameterCount;
		}

		/**
		 * Set the unique count of parameters in the SQL statement.
		 */
		void setUniqParameterCount(int uniqParameterCount) {
			this.uniqParameterCount = uniqParameterCount;
		}

		/**
		 * Return the unique count of parameters in the SQL statement.
		 */
		@Override
		public int getUniqParameterCount() {
			return this.uniqParameterCount;
		}

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
		public String substituteParameters(@Nullable SqlParameters paramSource) {
			String originalSql = this.getOriginalSql();
			final StringBuilder actualSql = new StringBuilder(originalSql.length());
			final AtomicInteger lastIndex = new AtomicInteger(0);
			this.getNamedParameterStream().forEach(parameter -> {
				String paramName = parameter.getParameterName();
				int startIndex = parameter.getStartIndex();
				int endIndex = parameter.getEndIndex();
				actualSql.append(originalSql, lastIndex.get(), startIndex);
				if (paramSource != null && paramSource.hasValue(paramName)) {
					Object value = paramSource.getValue(paramName);
					if (value instanceof SqlParameterValue) {
						value = ((SqlParameterValue) value).getValue();
					}
					if (value instanceof Collection) {
						Iterator<?> entryIter = ((Collection<?>) value).iterator();
						int k = 0;
						while (entryIter.hasNext()) {
							if (k > 0) {
								actualSql.append(DEF_DELIMITER);
							}
							k++;
							Object entryItem = entryIter.next();
							if (entryItem instanceof Object[]) {
								Object[] expressionList = (Object[]) entryItem;
								actualSql.append('(');
								for (int m = 0; m < expressionList.length; m++) {
									if (m > 0) {
										actualSql.append(DEF_DELIMITER);
									}
									actualSql.append(DEF_JDBC_PARAMETER);
								}
								actualSql.append(')');
							} else {
								actualSql.append(DEF_JDBC_PARAMETER);
							}
						}
					} else {
						actualSql.append(DEF_JDBC_PARAMETER);
					}
				} else {
					actualSql.append(DEF_JDBC_PARAMETER);
				}
				lastIndex.set(endIndex);
			});
			actualSql.append(originalSql, lastIndex.get(), originalSql.length());
			return actualSql.toString();
		}

		@Override
		public String toString() {
			return "ParsedParametersSqlImp{" +
					"originalSql='" + originalSql + '\'' +
					", parameters=" + parameters +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof ParsedParametersSqlImp)) return false;
			ParsedParametersSqlImp that = (ParsedParametersSqlImp) o;
			return originalSql.equals(that.originalSql) &&
					parameters.equals(that.parameters);
		}

		@Override
		public int hashCode() {
			return Objects.hash(originalSql, parameters);
		}
	}
}
