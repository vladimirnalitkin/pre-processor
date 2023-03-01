package com.van.processor.common;

public abstract class Constant {
	public final static char SPACE = ' ';
	public final static char APOSTROPHE = '\\';

    public final static String  REPOSITORY_ON_ITEM_ANNOTATION = "com.van.processor.annotation.RepositoryOnItem";
    public final static String  REST_CONTROLLER_ON_ITEM_ANNOTATION = "com.van.processor.annotation.RestControllerOnItem";
    public final static String  ENTITY_ANNOTATION = "javax.persistence.Entity";

    public final static String  DEFAULT_ENCODING = "utf-8";
    public final static String  REPOSITORY_PREFIX = "Repository";
    public final static String  SERVICE_PREFIX = "Service";
    public final static String  CONTROLLER_PREFIX = "Controller";

    public final static String  MAP_PACKAGE_NAME = "packageName";
    public final static String  MAP_SIMPLE_CLASS_NAME = "simpleClassName";
    public final static String  MAP_SIMPLE_CLASS_NAME_UPPER = "simpleClassNameUpper";
    public final static String  MAP_BUILDER_CLASS_NAME = "builderClassName";
    public final static String  MAP_BUILDER_SIMPLE_CLASS_NAME = "builderSimpleClassName";

    public final static String  MAP_HTTP_URL_NAME = "urlName";
    public final static String  MAP_SECURITY_NAME = "security";

    public final static String  PARAM_TABLE_NAME = "tableName";
    public final static String  PARAM_ID_FIELD = "idField";
    public final static String  PARAM_SELECT_FIELDS_NAME = "selectFields";
    public final static String  PARAM_SUB_ENTITIES = "subEntities";
    public final static String  PARAM_INSERT_FIELDS_NAME = "insertFields";
    public final static String  PARAM_MANDATORY_FILTER_NAME = "mandatoryFilter";
	public final static String PARAM_ON_ITEM_BEFORE_ANY_NAME = "onItemBeforeAny";
	public final static String PARAM_ON_ITEM_BEFORE_CREATE_NAME = "onItemBeforeCreate";
	public final static String PARAM_ON_ITEM_BEFORE_UPDATE_NAME = "onItemBeforeUpdate";
	public final static String PARAM_ON_ITEM_BEFORE_DELETE_NAME = "onItemBeforeDelete";
	public final static String  PARAM_ON_ITEM_AFTER_CREATE_NAME = "onItemAfterCreate";
	public final static String  PARAM_ON_ITEM_AFTER_UPDATE_NAME = "onItemAfterUpdate";

    public final static String  PARAM_RUN_BEFORE_CREATE_NAME = "RunBeforeCreate";
    public final static String  PARAM_RUN_BEFORE_UPDATE_NAME = "RunBeforeUpdate";

    public final static String  PLUS_ID = "_ID";

	public final static String UN_PROVIDED = "un_provided";
}
