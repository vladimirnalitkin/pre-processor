package com.van.processor.jdbc.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementBuilder {
    PreparedStatement buildPreparedStatement(Connection con) throws SQLException;
}