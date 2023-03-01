package com.van.processor.repository;

import com.google.common.base.Strings;
import com.van.processor.domain.ApplyOnItem;
import com.van.processor.domain.Fetch;
import com.van.processor.domain.FilterFunction;
import com.van.processor.domain.RequestParam;
import com.van.processor.exeption.DataAccessException;
import com.van.processor.generated.GenRepository;
import com.van.processor.jdbc.JdbcOperations;
import com.van.processor.jdbc.extractor.ResultExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractRepository<T, ID> implements GenRepository<T, ID> {
	/**
	 * Logger available to subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	protected static final String IDS_NAME = "ids";
	protected static final String ID_NAME = "id";
	protected static final String IDS_FILTER = " AND ID in (:" + IDS_NAME + ")";
	protected static final String ID_FILTER = " AND ID = :" + ID_NAME;
	protected static final String DEF_WHERE = " WHERE 1=1 ";

	protected final JdbcOperations jdbcOperations;
	protected final FilterFunction mandatoryfilter;
	protected final ApplyOnItem<T> applyOnItemBeforeAny;
	protected final ApplyOnItem<T> applyOnItemBeforeCreate;
	protected final ApplyOnItem<T> applyOnItemBeforeUpdate;
	protected final ApplyOnItem<T> applyOnItemBeforeDelete;
	protected final ApplyOnItem<T> applyOnItemAfterCreate;
	protected final ApplyOnItem<T> applyOnItemAfterUpdate;

	public AbstractRepository(JdbcOperations jdbcOperations, FilterFunction mandatoryfilter
			, ApplyOnItem<T> applyOnItemBeforeAny
			, ApplyOnItem<T> applyOnItemBeforeCreate, ApplyOnItem<T> applyOnItemBeforeUpdate, ApplyOnItem<T> applyOnItemBeforeDelete
			, ApplyOnItem<T> applyOnItemAfterCreate, ApplyOnItem<T> applyOnItemAfterUpdate) {
		this.jdbcOperations = jdbcOperations;
		this.mandatoryfilter = mandatoryfilter;
		this.applyOnItemBeforeAny = applyOnItemBeforeAny;
		this.applyOnItemBeforeCreate = applyOnItemBeforeCreate;
		this.applyOnItemBeforeUpdate = applyOnItemBeforeUpdate;
		this.applyOnItemBeforeDelete = applyOnItemBeforeDelete;
		this.applyOnItemAfterCreate = applyOnItemAfterCreate;
		this.applyOnItemAfterUpdate = applyOnItemAfterUpdate;
	}

	protected abstract String getSelectSql();

	protected abstract String getDeleteSql();

	protected abstract void enrichSubEntity(List<T> source, Fetch fetch) throws DataAccessException;

	protected abstract T extractDataFromRS(ResultSet rs, int i) throws DataAccessException;

	//protected abstract Map<String, List<T>> getGroupBy(List<String> ids, String mergFieldName , Fetch fetch) throws DataAccessException;

	protected List<T> query(String sql, Map<String, ?> params, Fetch fetch) throws DataAccessException {
		List<T> result = jdbcOperations.query(sql, params, this::extractDataFromRS);
		if (log.isDebugEnabled()) {
			log.debug("result before enrich -> " + result);
		}
		if (result != null && result.size() > 0) {
			enrichSubEntity(result, fetch);
		}
		if (log.isDebugEnabled()) {
			log.debug("result -> " + result);
		}
		return result;
	}

	@Override
	public Optional<T> getById(ID id, Fetch fetch) throws DataAccessException {
		List<T> result = null;
		if (id != null) {
			String sql = prepareStatement(getSelectSql(), ID_FILTER);
			log.debug("get sql = " + sql);
			result = query(sql, Collections.singletonMap(ID_NAME, id), fetch);
		}
		if ((result == null) || result.isEmpty()) {
			log.debug(String.format("Not found record with id = %s", id));
			return Optional.empty();
		}
		return Optional.ofNullable(result.get(0));
	}

	@Override
	public boolean existsById(ID id) throws DataAccessException {
		return getById(id, Fetch.of()).isPresent();
	}

	@Override
	public Map<ID, List<T>> getMapById(Iterable<ID> ids, Fetch fetchType) {
		final Map<ID, List<T>> result = new HashMap<>();

		return jdbcOperations.queryForObject(
				prepareStatement(getSelectSql(), IDS_FILTER)
				, Collections.singletonMap("ids", Collections.singletonList(ids))
				, new ResultExtractor<Map<ID, List<T>>>() {
					@Override
					public Map<ID, List<T>> extractData(ResultSet rs) throws SQLException, DataAccessException {
						return result;
					}
				}
		);
	}

	@Override
	public List<T> getAll(RequestParam param, Fetch fetch) {
		log.debug("getAll param=" + param);
		String sql = prepareStatement(getSelectSql(), (Objects.nonNull(param) && param.isFiltered() ? " AND " + param.getFilter().toString() : ""))
				+ " LIMIT " + param.getPageSize()
				+ " OFFSET " + param.getOffset();
		log.debug("getAll sql=" + sql);
		return query(sql, (Map<String, ?>) null, fetch);
	}

	@Override
	public List<T> getAllById(Iterable<ID> ids, Fetch fetch) {
		return query(
				prepareStatement(getSelectSql(), IDS_FILTER)
				, Collections.singletonMap(IDS_NAME, Collections.singletonList(ids))
				, fetch
		);
	}

	@Override
	public <S extends T> Optional<ID> save(S entity) {
		return Optional.empty();
	}

	@Override
	public <S extends T> Iterable<Optional<ID>> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public abstract Optional<ID> create(final T entity);

	@Override
	public abstract Optional<ID> update(final T entity);

	@Override
	public int deleteById(ID id) {
		return cascadeDelete(Collections.<ID>singletonList(id));
	}

	@Override
	public abstract int delete(T entity);

	@Override
	public abstract int deleteAll(Iterable<? extends T> entities);

	@Override
	public int deleteAll() {
		return jdbcOperations.update(prepareStatement(getDeleteSql()));
	}

	protected int cascadeDelete(List<ID> ids) {
		log.debug("ids = " + ids);
		int count = 0;
		if (!Objects.isNull(ids) && !ids.isEmpty()) {
			count = jdbcOperations.update(prepareStatement(getDeleteSql(), IDS_FILTER), Collections.singletonMap(IDS_NAME, ids));
		}
		return count;
	}

	protected String prepareStatement(String sql, String filter) {
		String sqlFilter = DEF_WHERE + filter + (mandatoryfilter != null ? " AND (" + ((FilterFunction) mandatoryfilter).getFilter() + ") " : "");
		return String.format(sql, Strings.isNullOrEmpty(sqlFilter) ? "" : sqlFilter);
	}

	protected String prepareStatement(String sql) {
		return prepareStatement(sql, "");
	}

    /*
   @Override
   public Map<Long, List<${simpleClassName}>> getMapById(Iterable<${idField.type}> ids, Fetch fetchType) {
       Map<Long, List<${simpleClassName}>> result = new HashMap<>();
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           con = getDataSource().getConnection();
           String sql = prepareStatement(getSelectSql(), IDS_FILTER);
           log.debug("get sql = " + sql);
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           int i = 1;
           while (rs.next()) {
               ${simpleClassName} item = mapRow(rs, i);
               List<${simpleClassName}> list = result.getOrDefault(item.get${idField.nameInJava?cap_first}(), new LinkedList<>());
               list.add(item);
               i++;
           }
       } catch (SQLException ex) {
           JdbcUtils.closeStatement(ps);
           ps = null;
           DataSourceUtils.releaseConnection(con, getDataSource());
           con = null;
           throw new RuntimeException("sql exeption", ex);
       } finally {
           JdbcUtils.closeStatement(ps);
           DataSourceUtils.releaseConnection(con, getDataSource());
       }
       return result;
   }
*/

}

