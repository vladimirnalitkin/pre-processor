package com.van.processor.jdbc.sql;

public interface DisposableSqlTypeValue extends SqlTypeValue {
    void cleanup();
}
