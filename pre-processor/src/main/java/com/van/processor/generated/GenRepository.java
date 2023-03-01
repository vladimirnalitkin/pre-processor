package com.van.processor.generated;

import com.van.processor.domain.Fetch;
import com.van.processor.domain.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenRepository<T, ID> {

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    default Optional<T> getById(ID id) {
        return getById(id, Fetch.of());
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    Optional<T> getById(ID id, Fetch fetchType);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    boolean existsById(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    default List<T> getAll() {
        return getAll(RequestParam.of(), Fetch.of());
    }

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    default List<T> getAll(RequestParam param) {
        return getAll(param, Fetch.of());
    }

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    default List<T> getAll(Fetch fetchType) {
        return getAll(RequestParam.of(), fetchType);
    }

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    List<T> getAll(RequestParam param, Fetch fetchType);

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     */
    List<T> getAllById(Iterable<ID> ids, Fetch fetchType);

    /**
     * For internal using.
     *
     * @param ids
     * @param fetchType
     * @return Map
     */
    Map<ID, List<T>> getMapById(Iterable<ID> ids, Fetch fetchType);

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    <S extends T> Optional<ID> save(S entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    <S extends T> Iterable<Optional<ID>> saveAll(Iterable<S> entities);

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    Optional<ID> create(final T entity);

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    Optional<ID> update(final T entity);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    int deleteById(ID id);

    /**
     * Deletes a given entity.
     *
     * @param entity
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    int delete(final T entity);

    /**
     * Deletes the given entities.
     *
     * @param entities
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    int deleteAll(Iterable<? extends T> entities);

    /**
     * Deletes all entities managed by the repository.
     */
    int deleteAll();

}
