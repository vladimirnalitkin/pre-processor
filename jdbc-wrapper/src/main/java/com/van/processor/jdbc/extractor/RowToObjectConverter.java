package com.van.processor.jdbc.extractor;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowToObjectConverter <T> implements ResultExtractor<T> {
	private final RowExtractor<T> rowExtractor;

	/**
	 * Create a new RowToListConverter.
	 * @param rowExtractor the RowMapper which creates an object for each row
	 */
	public RowToObjectConverter(RowExtractor<T> rowExtractor) {
		this(rowExtractor, 0);
	}

	/**
	 * Create a new RowToListConverter.
	 * @param rowExtractor the RowExtractor which creates an object for each row
	 * @param rowCount the number of expected rows
	 * (just used for optimized collection handling)
	 */
	public RowToObjectConverter(RowExtractor<T> rowExtractor, int rowCount) {
		assert rowExtractor!=null : "RowExtractor is required";
		this.rowExtractor = rowExtractor;
	}

	@Override
	@Nullable
	public T extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next()) {
			return this.rowExtractor.extractData(rs, 0);
		} else {
			return null;
		}
	}

}
