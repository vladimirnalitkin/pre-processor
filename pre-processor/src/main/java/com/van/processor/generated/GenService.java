package com.van.processor.generated;

import com.van.processor.common.exeption.ParseException;

import java.util.Collection;
import java.util.Optional;

public interface GenService<T, ID> {

    Optional<T> get(ID id);

    Collection<T> getAll(String filter) throws ParseException;

    Optional<ID> create(T item);

    Optional<ID> update(T item);

    void deleteById(ID id);

    void delete(T item);

}
