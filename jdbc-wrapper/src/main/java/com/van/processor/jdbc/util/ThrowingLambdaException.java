package com.van.processor.jdbc.util;

@FunctionalInterface
public interface ThrowingLambdaException<T, E extends Exception> {
	void accept(T t) throws E;
}

