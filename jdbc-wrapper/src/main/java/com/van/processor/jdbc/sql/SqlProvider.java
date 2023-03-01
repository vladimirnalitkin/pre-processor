package com.van.processor.jdbc.sql;

@FunctionalInterface
public interface SqlProvider {

    /**
     * Return the SQL string for this object, i.e.
     * typically the SQL used for creating statements.
     * @return the SQL string, or {@code null}
     */
    String getSql();

}
