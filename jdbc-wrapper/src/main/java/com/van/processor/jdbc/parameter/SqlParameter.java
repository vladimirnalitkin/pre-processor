package com.van.processor.jdbc.parameter;

import com.van.processor.common.Nullable;
import com.van.processor.jdbc.util.JdbcUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The SqlParameterType + parameter name.
 */
public class SqlParameter extends SqlParameterValue {

	// The name of the parameter, if any
	@Nullable
	private final String name;

	/**
	 * Copy constructor.
	 *
	 * @param otherParam the SqlParameter object to copy from
	 */
	public SqlParameter(SqlParameter otherParam) {
		this(otherParam.getName(), (SqlParameterValue) otherParam);
	}

	/**
	 * Constructor.
	 *
	 * @param name              - name of parameter
	 * @param sqlParameterValue - the sqlParameterValue object
	 */
	private SqlParameter(@Nullable String name, SqlParameterValue sqlParameterValue) {
		super(sqlParameterValue);
		this.name = name;
	}

	/**
	 * Constructor.
	 *
	 * @param name     - parameter name
	 * @param sqlType  - parameter type
	 * @param typeName - parameter type name
	 * @param scale    - parameter scale
	 * @param value    - parameter value
	 */
	private SqlParameter(@Nullable String name, Integer sqlType, @Nullable String typeName, @Nullable Integer scale, Object value) {
		super(sqlType, typeName, scale, value);
		this.name = name;
	}

	/**
	 * Create a new anonymous SqlParameter, supplying the SQL type.
	 */
	public static SqlParameter of(@Nullable String name, SqlParameterValue sqlParameterValue) {
		return new SqlParameter(name, sqlParameterValue);
	}

	/**
	 * Create a new anonymous SqlParameter, supplying the SQL type.
	 */
	public static SqlParameter of() {
		return new SqlParameter(null, SqlParameterValueEmpty);
	}

	/**
	 * Create a new anonymous SqlParameter, supplying the SQL type.
	 *
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 */
	public static SqlParameter of(int sqlType) {
		return new SqlParameter(null, sqlType, null, null, null);
	}

	/**
	 * Create a new anonymous SqlParameter, supplying the SQL type.
	 *
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 */
	public static SqlParameter of(int sqlType, @Nullable String typeName) {
		return new SqlParameter(null, sqlType, typeName, null, null);
	}

	/**
	 * Create a new anonymous SqlParameter, supplying the SQL type.
	 *
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 * @param scale   the number of digits after the decimal point
	 *                (for DECIMAL and NUMERIC types)
	 */
	public static SqlParameter of(int sqlType, int scale) {
		return new SqlParameter(null, sqlType, null, scale, null);
	}

	/**
	 * Create a new SqlParameter, supplying name and SQL type.
	 *
	 * @param name name of the parameter, as used in input and output maps
	 */
	public static SqlParameter of(String name) {
		return new SqlParameter(name, SqlParameterValueEmpty);
	}

	/**
	 * Create a new SqlParameter, supplying name and SQL type.
	 *
	 * @param name    name of the parameter, as used in input and output maps
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 */
	public static SqlParameter of(String name, int sqlType) {
		return new SqlParameter(name, sqlType, null, null, null);
	}

	/**
	 * Create a new SqlParameter, supplying name and SQL type.
	 *
	 * @param name     name of the parameter, as used in input and output maps
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 */
	public static SqlParameter of(String name, int sqlType, @Nullable String typeName) {
		return new SqlParameter(name, sqlType, typeName, null, null);
	}

	/**
	 * Create a new SqlParameter, supplying name and SQL type.
	 *
	 * @param name    name of the parameter, as used in input and output maps
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 * @param scale   the number of digits after the decimal point
	 *                (for DECIMAL and NUMERIC types)
	 */
	public static SqlParameter of(String name, int sqlType, int scale) {
		return new SqlParameter(name, sqlType, null, scale, null);
	}

	/**
	 * Create a new SqlParameter, supplying name and SQL type.
	 *
	 * @param name     name of the parameter, as used in input and output maps
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 * @param scale    the number of digits after the decimal point
	 *                 (for DECIMAL and NUMERIC types)
	 */
	public static SqlParameter of(String name, int sqlType, String typeName, int scale, Object value) {
		return new SqlParameter(name, sqlType, typeName, scale, value);
	}

	/**
	 * Return the name of the parameter, or {@code null} if anonymous.
	 */
	@Nullable
	public String getName() {
		return this.name;
	}

	/**
	 * Return whether this parameter holds input values that should be set
	 * before execution even if they are {@code null}.
	 * <p>This implementation always returns {@code true}.
	 */
	public boolean isInputValueProvided() {
		return true;
	}

	/**
	 * Return whether this parameter is an implicit return parameter used during the
	 * results processing of {@code CallableStatement.getMoreResults/getUpdateCount}.
	 * <p>This implementation always returns {@code false}.
	 */
	public boolean isResultsParameter() {
		return false;
	}


	/**
	 * Convert a list of JDBC types, as defined in {@code java.sql.Types},
	 * to a List of SqlParameter objects as used in this package.
	 */
	public static List<SqlParameter> sqlTypesToAnonymousParameterList(@Nullable int... types) {
		if (types == null) {
			return new LinkedList<>();
		}
		List<SqlParameter> result = new ArrayList<>(types.length);
		for (int type : types) {
			result.add(SqlParameter.of(type));
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SqlParameter)) return false;
		if (!super.equals(o)) return false;
		SqlParameter that = (SqlParameter) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name);
	}

	@Override
	public String toString() {
		return "SqlParameter{" +
				", name=" + getName() +
				", sqlType=" + getSqlType() +
				", typeName='" + getTypeName() + '\'' +
				", scale=" + getScale() + '\'' +
				'}';
	}
}

