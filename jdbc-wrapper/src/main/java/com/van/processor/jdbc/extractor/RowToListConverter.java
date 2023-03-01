package com.van.processor.jdbc.extractor;

import com.van.processor.common.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowToListConverter <T> implements ResultExtractor<List<T>> {

    private final RowExtractor<T> rowExtractor;

    private final int rowCount;

    /**
     * Create a new RowToListConverter.
     * @param rowExtractor the RowMapper which creates an object for each row
     */
    public RowToListConverter(RowExtractor<T> rowExtractor) {
        this(rowExtractor, 0);
    }

    /**
     * Create a new RowToListConverter.
     * @param rowExtractor the RowExtractor which creates an object for each row
     * @param rowCount the number of expected rows
     * (just used for optimized collection handling)
     */
    public RowToListConverter(RowExtractor<T> rowExtractor, int rowCount) {
        assert rowExtractor!=null : "RowExtractor is required";
        this.rowExtractor = rowExtractor;
        this.rowCount = rowCount;
    }


    @Override
    @Nullable
    public List<T> extractData(ResultSet rs) throws SQLException {
        List<T> results = (this.rowCount > 0 ? new ArrayList<>(this.rowCount) : new ArrayList<>());
        int rowNum = 0;
        while (rs.next()) {
            results.add(this.rowExtractor.extractData(rs, rowNum++));
        }
        return results;
    }

}
