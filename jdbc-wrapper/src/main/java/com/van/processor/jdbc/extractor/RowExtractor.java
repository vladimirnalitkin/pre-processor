package com.van.processor.jdbc.extractor;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convert each rows of Result Set to the collection of particular class.
 * @param <T> - type of class of result.
 */
@FunctionalInterface
public interface RowExtractor<T> {
    @Nullable
    T extractData(ResultSet rs, int rowNum) throws SQLException, DataAccessException;
}
