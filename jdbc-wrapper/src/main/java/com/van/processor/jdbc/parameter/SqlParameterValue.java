package com.van.processor.jdbc.parameter;

import com.van.processor.common.Nullable;

import java.sql.Types;
import java.util.Objects;

import static com.van.processor.jdbc.util.JdbcUtils.TYPE_UNKNOWN;

/**
 * The SqlParameterType + parameter value.
 */
public class SqlParameterValue extends SqlParameterType {

	public static final SqlParameterValue SqlParameterValueEmpty = of(null);

	@Nullable
	private final Object value;

	/**
	 * Copy constructor.
	 *
	 * @param otherParamValue the SqlParameter object to copy from
	 */
	protected SqlParameterValue(SqlParameterValue otherParamValue) {
		super(otherParamValue.getSqlType(), otherParamValue.getTypeName(), otherParamValue.getScale());
		this.value = otherParamValue.getValue();
	}

	/**
	 * Constructor.
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 * @param scale   the number of digits after the decimal point
	 *                (for DECIMAL and NUMERIC types)
	 * @param value   the value object
	 */
	protected SqlParameterValue(int sqlType, String typeName, Integer scale, @Nullable Object value) {
		super(sqlType, typeName, scale);
		this.value = value;
	}

	/**
	 * Create a new SqlParameterValue, supplying the SQL type.
	 *
	 * @param value the value object
	 */
	public static SqlParameterValue of(@Nullable Object value) {
		return new SqlParameterValue(TYPE_UNKNOWN, null, null, value);
	}

	/**
	 * Create a new SqlParameterValue, supplying the SQL type.
	 *
	 * @param sqlParameterType sqlParameterType
	 * @param value the value object
	 */
	public static SqlParameterValue of(SqlParameterType sqlParameterType, @Nullable Object value) {
		return new SqlParameterValue(sqlParameterType.getSqlType(), sqlParameterType.getTypeName(), sqlParameterType.getScale(), value);
	}

	/**
	 * Create a new SqlParameterValue, supplying the SQL type.
	 *
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 * @param value   the value object
	 */
	public static SqlParameterValue of(int sqlType, @Nullable Object value) {
		return new SqlParameterValue(sqlType, null, null, value);
	}

	/**
	 * Create a new SqlParameterValue, supplying the SQL type.
	 *
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 * @param value    the value object
	 */
	public static SqlParameterValue of(int sqlType, String typeName, @Nullable Object value) {
		return new SqlParameterValue(sqlType, typeName, null, value);
	}

	/**
	 * Create a new SqlParameterValue, supplying the SQL type.
	 *
	 * @param sqlType the SQL type of the parameter according to {@code java.sql.Types}
	 * @param scale   the number of digits after the decimal point
	 *                (for DECIMAL and NUMERIC types)
	 * @param value   the value object
	 */
	public static SqlParameterValue of(int sqlType, int scale, @Nullable Object value) {
		return of(sqlType, null, scale, value);
	}

	/**
	 * Create a new SqlParameterValue, supplying the SQL type.
	 *
	 * @param sqlType  the SQL type of the parameter according to {@code java.sql.Types}
	 * @param typeName the type name of the parameter (optional)
	 * @param scale    the number of digits after the decimal point
	 *                 (for DECIMAL and NUMERIC types)
	 * @param value    the value object
	 */
	public static SqlParameterValue of(int sqlType, String typeName, Integer scale, @Nullable Object value) {
		return new SqlParameterValue(sqlType, typeName, scale, value);
	}

	/**
	 * Create a new SqlParameterValue based on the given SqlParameter declaration.
	 *
	 * @param declaredParam the declared SqlParameter to define a value for
	 * @param value         the value object
	 */
	public SqlParameterValue(SqlParameter declaredParam, @Nullable Object value) {
		super(declaredParam);
		this.value = value;
	}

	/**
	 * Return the value object that this parameter value holds.
	 */
	@Nullable
	public Object getValue() {
		return this.value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SqlParameterValue)) return false;
		if (!super.equals(o)) return false;
		SqlParameterValue that = (SqlParameterValue) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), value);
	}

	@Override
	public String toString() {
		return "SqlParameterValue{" +
				" sqlType=" + getSqlType() +
				", typeName='" + getTypeName() + '\'' +
				", scale=" + getScale() +
				", value=" + getValue() +
				'}';
	}
}
