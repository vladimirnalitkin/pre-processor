package com.van.processor.jdbc.statement;

import com.van.processor.exeption.DataAccessException;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {
    T doInStatement(Statement stmt) throws SQLException, DataAccessException;
}