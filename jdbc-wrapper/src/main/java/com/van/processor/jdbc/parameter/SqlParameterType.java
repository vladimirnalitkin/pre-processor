package com.van.processor.jdbc.parameter;

import com.van.processor.common.Nullable;
import com.van.processor.jdbc.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.JDBCType;
import java.util.Objects;

import static com.van.processor.jdbc.util.JdbcUtils.TYPE_UNKNOWN_NAME;

/**
 * Parent class for any Sql parameters.
 */
public abstract class SqlParameterType {
	private static final Logger log = LoggerFactory.getLogger(SqlParameterType.class);

	// SQL type constant from {@code java.sql.Types}
	private final int sqlType;

	// Used for types that are user-named like: STRUCT, DISTINCT, JAVA_OBJECT, named array types
	@Nullable
	private final String typeName;

	// The scale to apply in case of a NUMERIC or DECIMAL type, if any
	@Nullable
	private final Integer scale;

	protected SqlParameterType(int sqlType, @Nullable String typeName, @Nullable Integer scale) {
		String typeNameLocal = JdbcUtils.TYPE_UNKNOWN == sqlType ? TYPE_UNKNOWN_NAME : typeName;
		try {
			JDBCType jdbcType =  JDBCType.valueOf(sqlType);
			typeNameLocal = (jdbcType != null) ? jdbcType.getName() : typeNameLocal;
		} catch (IllegalArgumentException exp) {
			log.debug(exp.getLocalizedMessage());
		}
		this.sqlType = sqlType;
		this.typeName = typeNameLocal;
		this.scale = scale;
	}

	protected SqlParameterType(SqlParameterType otherParam) {
		assert otherParam != null : "SqlParameter object must not be null";
		this.sqlType = otherParam.sqlType;
		this.typeName = otherParam.typeName;
		this.scale = otherParam.scale;
	}

	/**
	 * Return the SQL type of the parameter.
	 */
	public int getSqlType() {
		return sqlType;
	}

	/**
	 * Return the type name of the parameter, if any.
	 */
	@Nullable
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Return the scale of the parameter, if any.
	 */
	@Nullable
	public Integer getScale() {
		return scale;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SqlParameterType)) return false;
		SqlParameterType that = (SqlParameterType) o;
		return sqlType == that.sqlType &&
				(Objects.equals(typeName, that.typeName)) &&
				(Objects.equals(scale, that.scale));
	}

	@Override
	public int hashCode() {
		return Objects.hash(sqlType, typeName, scale);
	}

	@Override
	public String toString() {
		return "SqlParameterType{" +
				"sqlType=" + sqlType +
				", typeName='" + typeName + '\'' +
				", scale=" + scale +
				'}';
	}
}
