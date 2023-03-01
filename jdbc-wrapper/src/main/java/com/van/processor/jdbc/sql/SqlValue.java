package com.van.processor.jdbc.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlValue {

    /**
     * Set the value on the given PreparedStatement.
     * @param ps the PreparedStatement to work on
     * @param paramIndex the index of the parameter for which we need to set the value
     * @throws SQLException if a SQLException is encountered while setting parameter values
     */
    void setValue(PreparedStatement ps, int paramIndex)	throws SQLException;

    /**
     * Clean up resources held by this value object.
     */
    void cleanup();

}
