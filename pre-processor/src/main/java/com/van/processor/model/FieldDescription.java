package com.van.processor.model;

import java.util.Objects;

public class FieldDescription {
    private final String nameInJava;
    private final String type;
    private final String nameInDb;
    private final RefEntityClass refEntityClass;

    private FieldDescription(String nameInJava, String type, String nameInDb, RefEntityClass refEntityClass) {
        this.nameInJava = nameInJava;
        this.type = type;
        this.nameInDb = nameInDb;
        this.refEntityClass = refEntityClass;
    }

    public String getNameInJava() {
        return nameInJava;
    }

    public String getType() {
        return type;
    }

    public String getNameInDb() {
        return nameInDb;
    }

    public RefEntityClass getRefEntityClass() {
        return refEntityClass;
    }

    public static FieldDescriptionBuilder builder() {
        return new FieldDescriptionBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldDescription)) return false;
        FieldDescription that = (FieldDescription) o;
        return nameInJava.equals(that.nameInJava) &&
                type.equals(that.type) &&
                nameInDb.equals(that.nameInDb) &&
                refEntityClass.equals(that.refEntityClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameInJava, type, nameInDb, refEntityClass);
    }

    @Override
    public String toString() {
        return "FieldDescription(nameInJava = " + this.nameInJava + ", type = " + this.type
                + ", nameInDb = " + this.nameInDb + ", refTable = " + this.refEntityClass + ")";
    }

    public static class FieldDescriptionBuilder {
        private String nameInJava;
        private String type;
        private String nameInDb;
        private RefEntityClass refEntityClass = null;

        FieldDescriptionBuilder() {
        }

        public FieldDescriptionBuilder nameInJava(String nameInJava) {
            this.nameInJava = nameInJava;
            return this;
        }

        public FieldDescriptionBuilder type(String type) {
            this.type = type;
            return this;
        }

        public FieldDescriptionBuilder nameInDb(String nameInDb) {
            this.nameInDb = nameInDb;
            return this;
        }

        public FieldDescriptionBuilder refTable(RefEntityClass refEntityClass) {
            this.refEntityClass = refEntityClass;
            return this;
        }

        public FieldDescription build() {
            return new FieldDescription(this.nameInJava, this.type, this.nameInDb, this.refEntityClass);
        }
    }
}
