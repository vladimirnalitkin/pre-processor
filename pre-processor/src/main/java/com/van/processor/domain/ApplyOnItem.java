package com.van.processor.domain;

@FunctionalInterface
public interface ApplyOnItem<T> {
	void apply(T item);
}
