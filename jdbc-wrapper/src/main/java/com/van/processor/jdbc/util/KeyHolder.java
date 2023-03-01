package com.van.processor.jdbc.util;

import com.van.processor.exeption.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.Map;

/**
 * Key holder.
 */
public interface KeyHolder {

    Number getKey() throws InvalidDataAccessApiUsageException;

    Map<String, Object> getKeys() throws InvalidDataAccessApiUsageException;

    List<Map<String, Object>> getKeyList();

    void setKeyList(List<Map<String, Object>> keyList);
}

