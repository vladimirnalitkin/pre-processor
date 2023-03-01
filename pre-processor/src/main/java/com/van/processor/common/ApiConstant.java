package com.van.processor.common;

public abstract class ApiConstant {
    public final static String  DEF_PRODUCER = "application/json";
    public final static String  DEF_CONSUMER = "application/json";
    public final static String  RESPONSE_LIST = "List";

    public final static String  URL_DELIMITER = "/";
    public final static String  URL_ID = "{id}";
    public final static String  ID_PARAM = "id";

    public final static String  SUCCESS_HTTP_STATUS = "Success";
    public final static String  BAD_REQUEST = "Bad Request";
    public final static String  UNAUTHORIZED_HTTP_STATUS = "Unauthorized";
    public final static String  FORBIDDEN_HTTP_STATUS = "Forbidden";
    public final static String  NOT_FOUND_HTTP_STATUS = "Not Found";
    public final static String  FAILURE_HTTP_STATUS = "Failure";

    public final static String  OAUTH = "OAuth2";
    public final static String  READ_SCROPE = "read:sys";
    public final static String  READ_SCROPE_DESC = "read only";
}
