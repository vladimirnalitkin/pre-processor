package com.van.processor.jdbc.parameter;

@FunctionalInterface
public interface ParameterDisposer {
    void cleanupParameters();
}
