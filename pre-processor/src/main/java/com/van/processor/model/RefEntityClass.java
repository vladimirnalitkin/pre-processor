package com.van.processor.model;

import java.util.Objects;

public class RefEntityClass {
    private final String entityClassName;
    private final String fieldForMerge;
    private final String entityClassNameShortName;
    private final String type; // = List/Set/Collection or Object

    private RefEntityClass(String entityClassName, String fieldForMerge, String entityClassNameShortName, String type) {
        this.entityClassName = entityClassName;
        this.fieldForMerge = fieldForMerge;
        this.entityClassNameShortName = entityClassNameShortName;
        this.type = type;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getFieldForMerge() {
        return fieldForMerge;
    }

    public String getEntityClassNameShortName() {
        return entityClassNameShortName;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefEntityClass)) return false;
        RefEntityClass that = (RefEntityClass) o;
        return entityClassName.equals(that.entityClassName) &&
                fieldForMerge.equals(that.fieldForMerge) &&
                entityClassNameShortName.equals(that.entityClassNameShortName) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityClassName, fieldForMerge, entityClassNameShortName, type);
    }

    @Override
    public String toString() {
        return "RefEntityClass{" +
                "entityClassName='" + entityClassName + '\'' +
                ", fieldForMerge='" + fieldForMerge + '\'' +
                ", entityClassNameShortName='" + entityClassNameShortName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static RefEntityClass of(String tableName, String fieldForMerge, String entityClassNameShortName, String type) {
        return new RefEntityClass(tableName, fieldForMerge, entityClassNameShortName, type);
    }
}
