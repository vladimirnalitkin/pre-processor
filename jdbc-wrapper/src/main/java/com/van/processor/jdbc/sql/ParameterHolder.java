package com.van.processor.jdbc.sql;

public class ParameterHolder {
	private final String parameterName;
	private final int startIndex;
	private final int endIndex;

	private ParameterHolder(String parameterName, int startIndex, int endIndex) {
		this.parameterName = parameterName;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public int getEndIndex() {
		return this.endIndex;
	}

	public static ParameterHolder of(String parameterName, int startIndex, int endIndex) {
		return new ParameterHolder(parameterName, startIndex, endIndex);
	}

}
