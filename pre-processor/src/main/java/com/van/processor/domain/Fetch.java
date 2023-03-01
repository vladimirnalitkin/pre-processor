package com.van.processor.domain;

import java.util.Arrays;
import java.util.List;

public class Fetch {
    private final FetchType type;
    private final List<String> subEntities;

    public Fetch(FetchType type, List<String> subEntities) {
        this.type = type;
        this.subEntities = subEntities;
    }

    public FetchType getType() {
        return type;
    }

    public List<String> getSubEntities() {
        return subEntities;
    }

    public static Fetch of() {
        return of(FetchType.LAZY, (String) null);
    }

    public static Fetch of(FetchType type) {
        return of(type, (String) null);
    }

    public static Fetch of(FetchType type, String... subEntitiesArray) {
        return new Fetch(type, Arrays.asList(subEntitiesArray));
    }

    @Override
    public String toString() {
        return "Fetch{" +
                "type=" + type +
                ", subEntities=" + subEntities +
                '}';
    }
}
