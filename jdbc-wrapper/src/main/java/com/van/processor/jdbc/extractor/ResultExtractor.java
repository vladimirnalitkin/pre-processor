package com.van.processor.jdbc.extractor;

import com.van.processor.common.Nullable;
import com.van.processor.exeption.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Some aggregation for ResultSet which returning one object as result.
 * Should be care about empty ResultSet.
 * @param <T> - type of return object.
 */
@FunctionalInterface
public interface ResultExtractor<T> {
    @Nullable
    T extractData(ResultSet rs) throws SQLException, DataAccessException;
}
