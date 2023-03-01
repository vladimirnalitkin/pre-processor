package com.van.processor.model;

import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityDescription {
    private final String tableName;
    private final Optional<FieldDescription> idField;
    private final List<FieldDescription> selectFields;
    private final List<FieldDescription> insertFields;
    private final Multimap<String, String> mapByRunMethods;
    private final Boolean hasRestControllerOnItem;
    private final Boolean hasRepositoryOnItem;

    public EntityDescription(String tableName
            , Optional<FieldDescription> idField
            , List<FieldDescription> selectFields
            , List<FieldDescription> insertFields
            , Multimap<String, String> mapByRunMethods
            , Boolean hasRestControllerOnItem
            , Boolean hasRepositoryOnItem) {
        this.tableName = tableName;
        this.idField = idField;
        this.selectFields = selectFields;
        this.insertFields = insertFields;
        this.mapByRunMethods = mapByRunMethods;
        this.hasRestControllerOnItem = hasRestControllerOnItem;
        this.hasRepositoryOnItem = hasRepositoryOnItem;
    }

    public String getTableName() {
        return tableName;
    }

    public Optional<FieldDescription> getIdField() {
        return idField;
    }

    public List<FieldDescription> getSelectFields() {
        return selectFields;
    }

    public List<FieldDescription> getInsertFields() {
        return insertFields;
    }

    public Multimap<String, String> getMapByRunMethods() {
        return mapByRunMethods;
    }

    public Boolean getHasRestControllerOnItem() {
        return hasRestControllerOnItem;
    }

    public Boolean getHasRepositoryOnItem() {
        return hasRepositoryOnItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityDescription)) return false;
        EntityDescription that = (EntityDescription) o;
        return tableName.equals(that.tableName) &&
                selectFields.equals(that.selectFields) &&
                insertFields.equals(that.insertFields) &&
                mapByRunMethods.equals(that.mapByRunMethods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, idField, selectFields, insertFields, mapByRunMethods);
    }

    @Override
    public String toString() {
        return "EntityDescription{" +
                " tableName='" + tableName + "\n" +
                ", idField=" + idField + "\n" +
                ", selectFields=" + selectFields + "\n" +
                ", insertFields=" + insertFields + "\n" +
                ", mapByRunMethods=" + mapByRunMethods + "\n" +
                ", hasRestControllerOnItem=" + hasRestControllerOnItem + "\n" +
                ", hasRepositoryOnItem=" + hasRepositoryOnItem + "\n" +
                '}';
    }

    public static EntityDescription of(String tableName
            , Optional<FieldDescription> idField
            , List<FieldDescription> selectFields
            , List<FieldDescription> insertFields
            , Multimap<String, String> mapByRunMethods
            , Boolean hasRestControllerOnItem
            , Boolean hasRepositoryOnItem) {
        return new EntityDescription(tableName, idField, selectFields, insertFields, mapByRunMethods, hasRestControllerOnItem, hasRepositoryOnItem);
    }

}
