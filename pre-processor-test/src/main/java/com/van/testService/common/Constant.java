package com.van.testService.common;

public abstract class Constant {
	public static final String ID_PARAM_URL = "/{id}";
	public static final String BASE_URL = "/api";

	public static final String ADDRESS_SERVICE_TAG = "address_service";
	public static final String ADDRESS_URL = "/address";

	public static final String ARTICLE_SERVICE_TAG = "article_service";
	public static final String ARTICLE_URL = BASE_URL + "/articles";

	public static final String ARTICLE_COMMENT_SERVICE_TAG = "article_comment_service";
	public static final String ARTICLE_COMMENT_URL = ARTICLE_URL + ID_PARAM_URL + "/comments";
}