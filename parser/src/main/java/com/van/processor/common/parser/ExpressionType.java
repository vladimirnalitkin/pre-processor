package com.van.processor.common.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ExpressionType {
    LITERAL("Literal"),
    FUNCTION("Function"),
    OPERATION("Operation"),
    CONSTANT("Constant"),
    EXPRESSION("Expression");

    private final String name;
    private static final Map<String, ExpressionType> MAP = new HashMap<>();
    static {
        for (ExpressionType item : ExpressionType.values()) {
            MAP.put(item.name, item);
        }
    }

    ExpressionType(String name) {
        this.name = name;
    }

    public static Optional<ExpressionType> of(String token) {
        return Optional.ofNullable(MAP.get(token));
    }
}
