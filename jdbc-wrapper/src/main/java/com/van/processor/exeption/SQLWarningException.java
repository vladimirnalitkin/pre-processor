package com.van.processor.exeption;

import java.sql.SQLWarning;

public class SQLWarningException extends UncategorizedDataAccessException {

    /**
     * Constructor for SQLWarningException.
     * @param msg the detail message
     * @param ex the JDBC warning
     */
    public SQLWarningException(String msg, SQLWarning ex) {
        super(msg, ex);
    }

    /**
     * Return the underlying SQLWarning.
     */
    public SQLWarning SQLWarning() {
        return (SQLWarning) getCause();
    }

}