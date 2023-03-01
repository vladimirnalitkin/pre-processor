package com.van.processor.generated;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

import static com.van.processor.common.ApiConstant.*;

/**
 @author van
 GET${urlName}
 GET${urlName}/filter?name=like('we%')
 GET${urlName}/{id}
 POST${urlName}
 PUT${urlName}
 DELETE${urlName}
 DELETE${urlName}/{id}
 HEAD${urlName}/{id}
 */

public interface GenController<T, ID> {
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_HTTP_STATUS),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 401, message = UNAUTHORIZED_HTTP_STATUS),
            @ApiResponse(code = 403, message = FORBIDDEN_HTTP_STATUS),
            @ApiResponse(code = 404, message = NOT_FOUND_HTTP_STATUS),
            @ApiResponse(code = 500, message = FAILURE_HTTP_STATUS)})
    ResponseEntity<Collection<T>> getByFilter(final String filter);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_HTTP_STATUS),
            @ApiResponse(code = 401, message = UNAUTHORIZED_HTTP_STATUS),
            @ApiResponse(code = 403, message = FORBIDDEN_HTTP_STATUS),
            @ApiResponse(code = 404, message = NOT_FOUND_HTTP_STATUS),
            @ApiResponse(code = 500, message = FAILURE_HTTP_STATUS)})
    @ResponseBody
    ResponseEntity<T> getById(ID id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_HTTP_STATUS),
            @ApiResponse(code = 401, message = UNAUTHORIZED_HTTP_STATUS),
            @ApiResponse(code = 403, message = FORBIDDEN_HTTP_STATUS),
            @ApiResponse(code = 404, message = NOT_FOUND_HTTP_STATUS),
            @ApiResponse(code = 500, message = FAILURE_HTTP_STATUS)})
    @ResponseBody
    ResponseEntity<ID> post(final T item);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_HTTP_STATUS),
            @ApiResponse(code = 401, message = UNAUTHORIZED_HTTP_STATUS),
            @ApiResponse(code = 403, message = FORBIDDEN_HTTP_STATUS),
            @ApiResponse(code = 404, message = NOT_FOUND_HTTP_STATUS),
            @ApiResponse(code = 500, message = FAILURE_HTTP_STATUS)})
    @ResponseBody
    ResponseEntity<ID> put(final T item);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_HTTP_STATUS),
            @ApiResponse(code = 401, message = UNAUTHORIZED_HTTP_STATUS),
            @ApiResponse(code = 403, message = FORBIDDEN_HTTP_STATUS),
            @ApiResponse(code = 404, message = NOT_FOUND_HTTP_STATUS),
            @ApiResponse(code = 500, message = FAILURE_HTTP_STATUS)})
    void deleteById(ID id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_HTTP_STATUS),
            @ApiResponse(code = 401, message = UNAUTHORIZED_HTTP_STATUS),
            @ApiResponse(code = 403, message = FORBIDDEN_HTTP_STATUS),
            @ApiResponse(code = 404, message = NOT_FOUND_HTTP_STATUS),
            @ApiResponse(code = 500, message = FAILURE_HTTP_STATUS)})
    void delete(final T item);
}
