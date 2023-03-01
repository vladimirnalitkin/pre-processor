package com.van.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.van.processor.common.Constant.UN_PROVIDED;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface RepositoryOnItem {
    /**
     * (Optional) Mandatory filter on all(select, update, delete) operations. On insert we don't have WHERE.
     * @return bean name
     */
    String allOperFilterBeanName() default UN_PROVIDED;

	/**
	 * (Optional) Bean name which has a function to be apply before all operations On Item.
	 * @return bean name
	 */
	String beforeAllOperBeanName() default UN_PROVIDED;

	/**
	 * (Optional) Bean name which has a function to be apply before Create On Item.
	 * @return bean name
	 */
	String beforeCreateBeanName() default UN_PROVIDED;

	/**
	 * (Optional)Bean name which has a function to be apply after Create On Item.
	 * @return bean name
	 */
	String afterCreateBeanName() default UN_PROVIDED;

	/**
	 * (Optional) Bean name which has a function to be apply before Update On Item.
	 * @return bean name
	 */
	String beforeUpdateBeanName() default UN_PROVIDED;

	/**
	 * (Optional)Bean name which has a function to be apply after Update On Item.
	 * @return bean name
	 */
	String afterUpdateBeanName() default UN_PROVIDED;

	/**
	 * (Optional) Bean name which has a function to be apply before Delete On Item.
	 * @return bean name
	 */
	String beforeDeleteBeanName() default UN_PROVIDED;
}
