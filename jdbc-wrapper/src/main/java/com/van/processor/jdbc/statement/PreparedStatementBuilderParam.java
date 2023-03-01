package com.van.processor.jdbc.statement;

import com.van.processor.exeption.InvalidDataAccessApiUsageException;
import com.van.processor.jdbc.parameter.SqlParameters;

import java.util.Map;

public interface PreparedStatementBuilderParam {
	/**
	 * Return the declaredParameters.
	 * for testing only.
	 */
	SqlParameters getDeclaredParameters();

	/**
	 * Add a new declared parameters.
	 * <p>Order of parameter addition is significant.
	 *
	 * @param types the array of types of parameters to add to the list of declared parameters
	 */
	PreparedStatementBuilderParam declaredParameters(int... types);

	/**
	 * Add a new declared parameters.
	 * <p>Order of parameter addition is significant.
	 *
	 * @param declaredParameters the list of parameters to add to the list of declared parameters
	 */
	PreparedStatementBuilderParam declaredParameters(SqlParameters declaredParameters);

	/**
	 * Set binding parameters.
	 *
	 * @param parameters the list of parameters.
	 * @return PreparedStatementBuilderImp
	 */
	PreparedStatementBuilderParam bindingParameters(Object[] parameters) throws IllegalArgumentException;

	/**
	 * Set binding parameters.
	 *
	 * @param parameters the list of parameters.
	 * @return PreparedStatementBuilderImp
	 */
	PreparedStatementBuilderParam bindingParameters(Map<String, Object> parameters) throws IllegalArgumentException;

	/**
	 * Set binding parameters.
	 *
	 * @param parameters the list of parameters.
	 * @return PreparedStatementBuilderImp
	 */
	PreparedStatementBuilderParam bindingParameters(SqlParameters parameters) throws IllegalArgumentException;
}
