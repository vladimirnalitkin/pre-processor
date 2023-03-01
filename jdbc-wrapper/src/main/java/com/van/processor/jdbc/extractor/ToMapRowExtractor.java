package com.van.processor.jdbc.extractor;

import com.van.processor.common.LinkedCaseInsensitiveMap;
import com.van.processor.common.Nullable;
import com.van.processor.jdbc.util.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

public class ToMapRowExtractor implements RowExtractor<Map<String, Object>> {

    @Override
    @Nullable
    public Map<String, Object> extractData(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> mapOfColumnValues = createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            mapOfColumnValues.putIfAbsent(JdbcUtils.lookupColumnName(rsmd, i), JdbcUtils.getResultSetValue(rs, i));
        }
        return mapOfColumnValues;
    }

    /**
     * Create a Map instance to be used as column map.
     * <p>By default, a linked case-insensitive Map will be created.
     *
     * @param columnCount the column count, to be used as initial
     *                    capacity for the Map
     * @return the new Map instance
     * @see LinkedCaseInsensitiveMap
     */
    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap<>(columnCount);
    }

}
