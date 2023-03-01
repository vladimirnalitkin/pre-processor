package com.van.processor.jdbc.sql;

import com.van.processor.jdbc.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Interface for setTypeValue.
 */
@FunctionalInterface
public interface SqlTypeValue {

    /**
     * Constant that indicates an unknown (or unspecified) SQL type.
     * Passed into {@code setTypeValue} if the original operation method
     * does not specify a SQL type.
     * @see java.sql.Types
     */
    int TYPE_UNKNOWN = JdbcUtils.TYPE_UNKNOWN;


    /**
     * Set the type value on the given PreparedStatement.
     * @param ps the PreparedStatement to work on
     * @param paramIndex the index of the parameter for which we need to set the value
     * @param sqlType the SQL type of the parameter we are setting
     * @param typeName the type name of the parameter (optional)
     * @throws SQLException if a SQLException is encountered while setting parameter values
     * @see java.sql.Types
     * @see java.sql.PreparedStatement#setObject
     */
    void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName)
            throws SQLException;

}
