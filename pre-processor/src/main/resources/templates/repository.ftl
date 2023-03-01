epackage ${packageName};

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.van.processor.domain.ApplyOnItem;
import com.van.processor.domain.Fetch;
import com.van.processor.domain.FetchType;
import com.van.processor.domain.FilterFunction;
import com.van.processor.exeption.DataAccessException;
import com.van.processor.exeption.InvalidDataAccessApiUsageException;
import com.van.processor.jdbc.JdbcOperations;
import com.van.processor.jdbc.extractor.ResultExtractor;
import com.van.processor.jdbc.parameter.MapSqlParameters;
import com.van.processor.jdbc.statement.PreparedStatementBuilder;
import com.van.processor.jdbc.util.GeneratedKeyHolder;
import com.van.processor.jdbc.util.KeyHolder;
import com.van.processor.jdbc.util.NumberUtils;
import com.van.processor.repository.AbstractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ${simpleClassName}Repository extends AbstractRepository<${simpleClassName}, ${idField.type?cap_first}> {
    private static final Logger log = LoggerFactory.getLogger(${simpleClassName}Repository.class);

   <#list subEntities as entity>
    @Autowired
    private ${entity.refEntityClass.entityClassNameShortName?cap_first}Repository ${entity.nameInJava?uncap_first}Repository;
   </#list>

    public ${simpleClassName}Repository(
           @Autowired(required = true) JdbcOperations jdbcOperations
           , @Autowired(required = false) <#if mandatoryFilter != "">@Qualifier("${mandatoryFilter}")</#if> FilterFunction mandatoryfilter
           , @Autowired(required = false) <#if onItemBeforeAny != "">@Qualifier("${onItemBeforeAny}")</#if> ApplyOnItem<${simpleClassName}> applyOnItemBeforeAny
           , @Autowired(required = false) <#if onItemBeforeCreate != "">@Qualifier("${onItemBeforeCreate}")</#if> ApplyOnItem<${simpleClassName}> applyOnItemBeforeCreate
           , @Autowired(required = false) <#if onItemBeforeUpdate != "">@Qualifier("${onItemBeforeUpdate}")</#if> ApplyOnItem<${simpleClassName}> applyOnItemBeforeUpdate
           , @Autowired(required = false) <#if onItemBeforeDelete != "">@Qualifier("${onItemBeforeDelete}")</#if> ApplyOnItem<${simpleClassName}> applyOnItemBeforeDelete
           , @Autowired(required = false) <#if onItemAfterCreate != "">@Qualifier("${onItemAfterCreate}")</#if> ApplyOnItem<${simpleClassName}> applyOnItemAfterCreate
           , @Autowired(required = false) <#if onItemAfterUpdate != "">@Qualifier("${onItemAfterUpdate}")</#if> ApplyOnItem<${simpleClassName}> applyOnItemAfterUpdate
           ) {
       super(jdbcOperations , mandatoryfilter, applyOnItemBeforeAny
       , applyOnItemBeforeCreate, applyOnItemBeforeUpdate, applyOnItemBeforeDelete
       , applyOnItemAfterCreate, applyOnItemAfterUpdate);
    }

    private final String selectSql = "SELECT" +
    <#list selectFields as field>
            " ${field.nameInDb} as ${field.nameInJava}<#sep>,</#sep>" +
    </#list>
            " FROM ${tableName} %s";

    private final String insertSql = "INSERT INTO ${tableName}("+
    <#list insertFields as field>
            " ${field.nameInDb}<#sep>,</#sep>" +
    </#list>
            ") VALUES (" +
     <#list insertFields as field>
            " :${field.nameInDb}<#sep>,</#sep>" +
    </#list>
            ")";

    private final String updateSql = "UPDATE ${tableName} SET"+
    <#list insertFields as field>
            " ${field.nameInDb}=:${field.nameInDb}<#sep>,</#sep>" +
    </#list>
            " %s AND ${idField.nameInDb}=:${idField.nameInDb} ";

    private final String deleteSql = "DELETE FROM ${tableName} %s";

    protected ${simpleClassName} extractDataFromRS(ResultSet rs, int i) throws DataAccessException {
       try {
            ${simpleClassName} result = new ${simpleClassName}();
           <#list selectFields as field>
             <#switch field.type?cap_first>
             <#case "Integer">
             <#case "Int">
                result.set${field.nameInJava?cap_first}(rs.getInt("${field.nameInJava}"));
                <#break>
             <#default>
                result.set${field.nameInJava?cap_first}(rs.get${field.type?cap_first}("${field.nameInJava}"));
             </#switch>
           </#list>
            return result;
         } catch (SQLException ex) {
            throw new InvalidDataAccessApiUsageException("extractDataFromRS", ex);
         }
    }

    @Override
    public String getSelectSql(){
      return selectSql;
    }

    @Override
    public String getDeleteSql(){
      return deleteSql;
    }

    @Override
    protected void enrichSubEntity(List<${simpleClassName}> source, Fetch fetch) throws DataAccessException {
        log.debug("enrichSubEntity fetch = " + fetch.toString());
        if (!FetchType.LAZY.equals(fetch.getType())) {
            List<${idField.type?cap_first}> ids = source.stream().map(e-> e.get${idField.nameInJava?cap_first}()).collect(Collectors.toList());
            if (log.isDebugEnabled()) {
                ids.forEach(item -> log.debug(item.toString()));
            }
         <#list subEntities as entity>
            Map<${idField.type?cap_first}, List<${entity.refEntityClass.entityClassNameShortName?cap_first}>> ${entity.nameInJava?uncap_first}Map = ${entity.nameInJava?uncap_first}Repository.getGroupBy${simpleClassName}(ids, Fetch.of());
            if (log.isDebugEnabled()) {
              log.debug(${entity.nameInJava?uncap_first}Map.toString());
            }
            source.forEach(entity ->
             entity.set${entity.nameInJava?cap_first}(${entity.nameInJava?uncap_first}Map.get(entity.get${idField.nameInJava?cap_first}())<#if entity.refEntityClass.type == 'Object'>.get(0)</#if>)
            );
         </#list>
        }
    }

    @Override
    public <S extends ${simpleClassName}> Optional<${idField.type?cap_first}> save(S ${simpleClassName?uncap_first}Entity) {
        return Optional.empty();
    }

    @Override
    public <S extends ${simpleClassName}> Iterable<Optional<${idField.type?cap_first}>> saveAll(Iterable<S> entities) {
        return null;
    }

    <#list subEntities as entity>
    public Map<${entity.type?cap_first}, List<${simpleClassName}>> getGroupBy${entity.refEntityClass.entityClassNameShortName?cap_first}(List<${entity.type?cap_first}> ids, Fetch fetch) throws DataAccessException {
        final String selectSql = "SELECT ${entity.refEntityClass.fieldForMerge}," +
            <#list selectFields as field>
                    " ${field.nameInDb} as ${field.nameInJava}<#sep>,</#sep>" +
            </#list>
                    " FROM ${tableName} %s";
        final String sql = prepareStatement(selectSql, String.format(" AND ${entity.refEntityClass.fieldForMerge} in (:%s)", IDS_NAME));
        log.debug("getGroupBy${entity.nameInJava} sql = " + sql);
        Map<${entity.type?cap_first}, List<${simpleClassName}>> result = jdbcOperations.queryForObject(sql, Collections.singletonMap(IDS_NAME, ids)
                , new ResultExtractor<Map<${entity.type?cap_first}, List<${simpleClassName}>>>() {
                    @Override
                    public Map<${entity.type?cap_first}, List<${simpleClassName}>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        Multimap<${entity.type?cap_first}, ${simpleClassName}> map = ArrayListMultimap.create();
                        int i = 0;
                        while (rs.next()) {
                            ${simpleClassName} entity = extractDataFromRS(rs, i);
                        <#switch entity.type?cap_first>
                          <#case "Integer">
                          <#case "Int">
                            map.put(rs.getInt("${entity.refEntityClass.fieldForMerge}"), entity);
                            <#break>
                          <#default>
                             map.put(rs.get${entity.type?cap_first}("${entity.refEntityClass.fieldForMerge}"), entity);
                        </#switch>
                            i++;
                        }
                        return (Map<${entity.type?cap_first}, List<${simpleClassName}>>) (Map<${entity.type?cap_first}, ?>) map.asMap();
                    }
                }
        );
        return result;
    }

    </#list>

    @Override
    public Optional<${idField.type?cap_first}> create(final ${simpleClassName} ${simpleClassName?uncap_first}Entity) {
        log.debug(String.format("${simpleClassName}Repository.create entity=%s", ${simpleClassName?uncap_first}Entity));
      <#list RunBeforeCreate as func>
        ${simpleClassName?uncap_first}Entity.${func}();
      </#list>
        if(applyOnItemBeforeAny != null) {
         applyOnItemBeforeAny.apply(${simpleClassName?uncap_first}Entity);
        }
        if(applyOnItemBeforeCreate != null) {
         applyOnItemBeforeCreate.apply(${simpleClassName?uncap_first}Entity);
        }
        KeyHolder holder = new GeneratedKeyHolder();
        int count = jdbcOperations.update(insertSql, MapSqlParameters.of()
            <#list insertFields as field>
               .addValue("${field.nameInDb}", ${simpleClassName?uncap_first}Entity.get${field.nameInJava?cap_first}())
            </#list>
            , holder);
         log.debug("${simpleClassName}Repository.create count = " + count);
      <#switch idField.type?cap_first>
        <#case "Integer">
        <#case "Int">
           ${idField.type?cap_first} newId = holder.getKey().intValue();
           <#break>
        <#default>
           ${idField.type?cap_first} newId = holder.getKey().${idField.type?uncap_first}Value();
      </#switch>
        log.debug(String.format("${simpleClassName}Repository.create return id=%s ", newId));
        return Optional.ofNullable(newId);
    }

    @Override
    public Optional<${idField.type?cap_first}> update(final ${simpleClassName} ${simpleClassName?uncap_first}Entity) {
        log.debug("${simpleClassName}Repository.update entity = %s", ${simpleClassName?uncap_first}Entity);
      <#list RunBeforeUpdate as func>
        ${simpleClassName?uncap_first}Entity.${func}();
      </#list>
        if(applyOnItemBeforeAny != null) {
          applyOnItemBeforeAny.apply(${simpleClassName?uncap_first}Entity);
        }
        if(applyOnItemBeforeUpdate != null) {
          applyOnItemBeforeUpdate.apply(${simpleClassName?uncap_first}Entity);
        }
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = prepareStatement(updateSql, "");
        log.debug("${simpleClassName}Repository.update sql = " + sql);
         int count = jdbcOperations.update(sql, MapSqlParameters.of()
                        <#list insertFields as field>
                           .addValue("${field.nameInDb}", ${simpleClassName?uncap_first}Entity.get${field.nameInJava?cap_first}())
                        </#list>
                        , holder);
        log.debug("${simpleClassName}Repository.update count = " + count);
  <#switch idField.type?cap_first>
     <#case "Integer">
     <#case "Int">
        ${idField.type?cap_first} newId = holder.getKey()!=null ? holder.getKey().intValue() : null;
        <#break>
     <#default>
        ${idField.type?cap_first} newId = holder.getKey()!=null ? holder.getKey().${idField.type?uncap_first}Value() : null;
   </#switch>
        log.debug("${simpleClassName}Repository.update return id=%s ", newId);
        return Optional.ofNullable(newId);
    }

    @Override
    public int delete(${simpleClassName} ${simpleClassName?uncap_first}Entity) {
        log.debug("${simpleClassName}Repository.delete entity = %s", ${simpleClassName?uncap_first}Entity);
        return cascadeDelete(Collections.singletonList(${simpleClassName?uncap_first}Entity.get${idField.nameInJava?cap_first}()));
    }

    @Override
    public int deleteAll(Iterable<? extends ${simpleClassName}> ${simpleClassName?uncap_first}Entities) {
        log.debug("${simpleClassName}Repository.deleteAll entities = %s", ${simpleClassName?uncap_first}Entities);
        List<${idField.type?cap_first}> ids = new ArrayList<>();
        Iterator ${simpleClassName?uncap_first}Iterator = ${simpleClassName?uncap_first}Entities.iterator();
        while(${simpleClassName?uncap_first}Iterator.hasNext()){
          ids.add(((${simpleClassName})${simpleClassName?uncap_first}Iterator.next()).get${idField.nameInJava?cap_first}());
        }
        return cascadeDelete(ids);
    }
}
