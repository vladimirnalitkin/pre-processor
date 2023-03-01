package ${packageName};

import com.google.common.base.Strings;
import com.van.processor.generated.GenController;
import com.van.processor.generated.GenService;

//import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Collection;

import static com.van.processor.common.ApiConstant.*;

@RestController
@RequestMapping("${urlName}")
/*@Api(value = "${simpleClassName}_Service", consumes = "${simpleClassName} operations",
        authorizations = {@Authorization(value = OAUTH,
                scopes = {@AuthorizationScope(scope = READ_SCROPE, description = READ_SCROPE_DESC)})}
)*/
public class ${simpleClassName}Controller implements GenController<${simpleClassName}, ${idField.type}> {
    private static final Logger log = LoggerFactory.getLogger(${simpleClassName}Controller.class);

    @Autowired
    GenService<${simpleClassName}, ${idField.type}> service;

    @GetMapping
    @ResponseBody
    @Override
    public ResponseEntity<Collection<${simpleClassName}>> getByFilter(@RequestParam(required = false, value = "filter") final String filter) {
        log.debug("${simpleClassName}Controller.getByFilter filter='" + filter + "'");
        try {
           return ResponseEntity.ok(service.getAll(Strings.isNullOrEmpty(filter) ? filter :  URLDecoder.decode(filter, "UTF-8")));
        } catch (Exception exp) {
           log.error("${simpleClassName}Controller.getByFilter filter='" + filter + "' - with error : " + exp.getMessage());
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "${simpleClassName}Controller.getByFilter error !", exp);
        }
    }

    @GetMapping(URL_DELIMITER + URL_ID)
    @ResponseBody
    @Override
    public ResponseEntity<${simpleClassName}> getById(@PathVariable(ID_PARAM) ${idField.type} id){
      log.debug("${simpleClassName}Controller.getById id = %s", id);
      return service.get(id)
                .map(item -> {
                        log.debug("${simpleClassName}Controller.getById return item = %s", item);
                        return ResponseEntity.ok().body(item);
                    })
                .orElseGet(() -> {
                    log.debug("${simpleClassName}Controller.getById return notFound for id = %s", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @ResponseBody
    @Override
    public ResponseEntity<Long> post(@RequestBody final ${simpleClassName} item){
         log.debug("${simpleClassName}Controller.post item = %s", item);
         return service.create(item)
                  .map(retId -> {
                      log.debug("${simpleClassName}Controller.post return id = %s", retId);
                      HttpHeaders headers = new HttpHeaders();
                      URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().
                               path("/{retId}").buildAndExpand(retId).toUri();
                      headers.setLocation(uri);
                      log.debug("${simpleClassName}Controller.post return uri = %s", uri);
                      return new ResponseEntity<Long>(retId, headers, HttpStatus.CREATED);
                  })
                  .orElseGet(() -> {
                      log.debug("${simpleClassName}Controller.post return badRequest");
                      return ResponseEntity.badRequest().build();
                    });
    }

    @PutMapping
    @ResponseBody
    @Override
    public ResponseEntity<Long> put(@RequestBody final ${simpleClassName} item){
        log.debug("${simpleClassName}Controller.put item = %s", item);
        return service.update(item)
                .map(retId -> {
                        log.debug("${simpleClassName}Controller.put return id=%s", retId);
                        return ResponseEntity.ok().body(retId);
                    })
                .orElseGet(() -> {
                        log.debug("${simpleClassName}Controller.put return badRequest");
                        return ResponseEntity.badRequest().build();
                    });
    }

    @DeleteMapping(URL_DELIMITER + URL_ID)
    @Override
    public void deleteById(@PathVariable(ID_PARAM) ${idField.type} id){
        log.debug("${simpleClassName}Controller.deleteById id = %s", id);
        service.deleteById(id);
    }

    @DeleteMapping
    @Override
    public void delete(@RequestBody final ${simpleClassName} item){
        log.debug("${simpleClassName}Controller.delete item = %s", item);
        service.delete(item);
    }

}
