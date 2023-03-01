package com.van.processor.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum FetchType {

    /** Defines that data can be lazily fetched. */
    LAZY("lazy"),

    /** Defines that data must be eagerly fetched. */
    EAGER("eager"),

    /** Defines that data must be manual fetched in parallel. */
    MANUAL_IN_PARALLEL("manualInParallel"),

    /** Defines that data must be manual fetched. */
    MANUAL("manual");

    private final String name;

    private static final Map<String, FetchType> MAP = new HashMap<>();
    static {
        for (FetchType item : FetchType.values()) {
            MAP.put(item.name, item);
        }
    }

    FetchType(String name) {
        this.name = name;
    }

    public static Optional<FetchType> of(String token) {
        return Optional.ofNullable(MAP.get(token));
    }
}
