package ${packageName};

import com.van.processor.common.exeption.ParseException;
import com.van.processor.domain.RequestParam;
import com.van.processor.generated.GenRepository;
import com.van.processor.generated.GenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
<#if security == true>
import org.springframework.security.access.prepost.PreAuthorize;
</#if>
import org.springframework.stereotype.Service;

import java.util.Collection;

import java.util.Optional;

@Service
public class ${simpleClassName}Service implements GenService<${simpleClassName}, ${idField.type}> {
    private static final Logger log = LoggerFactory.getLogger(${simpleClassName}Service.class);

    @Autowired
    private GenRepository<${simpleClassName}, ${idField.type}> repository;

    @Override
    <#if security == true>
    @PreAuthorize("hasAuthority('${simpleClassNameUpper}_READ')")
    </#if>
    public Optional<${simpleClassName}> get(${idField.type} id) {
        log.debug("${simpleClassName}Service.get start");
        return repository.getById(id);
    }

    @Override
    <#if security == true>
    @PreAuthorize("hasAuthority('${simpleClassNameUpper}_READ')")
    </#if>
    public Collection<${simpleClassName}> getAll(String filter) throws ParseException {
        log.debug("${simpleClassName}Service.getAll filter='" + filter + "'");
        return repository.getAll(RequestParam.of(filter, null));
    }

    @Override
    <#if security == true>
    @PreAuthorize("hasAuthority('${simpleClassNameUpper}_CREATE')")
    </#if>
    public Optional<${idField.type}> create(${simpleClassName} item) {
        log.debug("${simpleClassName}Service.create start");
        return repository.create(item);
    }

    @Override
    <#if security == true>
    @PreAuthorize("hasAuthority('${simpleClassNameUpper}_UPDATE')")
    </#if>
    public Optional<${idField.type}> update(${simpleClassName} item) {
          log.debug("${simpleClassName}Service.update start");
          return repository.update(item);
    }

    @Override
    <#if security == true>
    @PreAuthorize("hasAuthority('${simpleClassNameUpper}_DELETE')")
    </#if>
    public void deleteById(${idField.type} id) {
        log.debug("${simpleClassName}Service.deleteById start");
        repository.deleteById(id);
    }

    @Override
    <#if security == true>
    @PreAuthorize("hasAuthority('${simpleClassNameUpper}_DELETE')")
    </#if>
    public void delete(${simpleClassName} item) {
        log.debug("${simpleClassName}Service.delete start");
        repository.delete(item);
    }
}
