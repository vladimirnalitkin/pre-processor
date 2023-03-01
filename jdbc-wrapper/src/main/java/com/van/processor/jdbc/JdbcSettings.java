package com.van.processor.jdbc;

interface JdbcSettings {

	boolean isIgnoreWarnings();

	void setIgnoreWarnings(boolean ignoreWarnings);

	int getFetchSize();

	void setFetchSize(int fetchSize);

	int getMaxRows();

	void setMaxRows(int maxRows);

	int getQueryTimeout();

	void setQueryTimeout(int queryTimeout);

	boolean isSkipResultsProcessing();

	void setSkipResultsProcessing(boolean skipResultsProcessing);

	boolean isSkipUndeclaredResults();

	void setSkipUndeclaredResults(boolean skipUndeclaredResults);

	boolean isResultsMapCaseInsensitive();

	void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive);

	/**
	 * Specify the maximum number of entries for this template's SQL cache.
	 * Default is 256.
	 */
	void setCacheLimit(int cacheLimit);

	/**
	 * Return the maximum number of entries for this template's SQL cache.
	 */
	int getCacheLimit();
}
