package com.van.processor.common.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.van.processor.common.parser.Operation.BinaryType.*;
import static com.van.processor.common.parser.Operation.LogicalType.*;

public class Operation implements Comparable<Operation> {
    private final OperationDef operationDef;
    private int depth;

    public Operation(OperationDef operationDef) {
        this.operationDef = operationDef;
        this.depth = 0;
    }

    public Operation(OperationDef operationDef, int depth) {
        this.operationDef = operationDef;
        this.depth = depth;
    }

    static OperationBuilder builder() {
        return new OperationBuilder();
    }

    OperationDef getOperationDef() {
        return operationDef;
    }

    int getDepth() {
        return depth;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return depth == operation.depth &&
                operationDef == operation.operationDef;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationDef, depth);
    }

    @Override
    public int compareTo(Operation o) {
        if (o == null) return 0;
        return getOperationDef() != null && o.getOperationDef() != null ?
                (o.getDepth() * 1000 + o.getOperationDef().getPriority()) - (getDepth() * 1000 + getOperationDef().getPriority()) : 0;
    }

    public static class OperationBuilder {
        private OperationDef operationDef;
        private int depth;

        OperationBuilder() {
        }

        OperationBuilder operationType(OperationDef operationDef) {
            this.operationDef = operationDef;
            return this;
        }

        OperationBuilder depth(int depth) {
            this.depth = depth;
            return this;
        }

        public Operation build() {
            return new Operation(this.operationDef, this.depth);
        }

        @java.lang.Override
        public String toString() {
            return String.format("OperationBuilder(operationType = %s , type = s%)", this.operationDef, this.depth);
        }
    }

    /**
     * Operation type.
     */
    public enum OperationDef {
        NONE(0, Character.MIN_VALUE, "", UNARY, SPLITER),
        OPEN_BRACKET(0, '(', "(", UNARY, SPLITER),
        CLOSE_BRACKET(0, ')', ")", UNARY, SPLITER),

        PLUS(0, '+', "+", BINARY, ARITHMETIC),
        MINUS(0, '-', "-", BINARY, ARITHMETIC),
        MUL(1, '*', "*", BINARY, ARITHMETIC),
        DIV(1, '/', "/", BINARY, ARITHMETIC),

        EQV(2, '=', "=", BINARY, COMPARATOR),
        NOT_EQV(2, '#', "!=", BINARY, COMPARATOR),
        MORE(1, '>', ">", BINARY, COMPARATOR),
        LESS(1, '<', "<", BINARY, COMPARATOR),

        AND(0, '&', " AND ", BINARY, LOGIC),
        OR(0, '|', " OR ", BINARY, LOGIC),
        IN(0, '?', " IN ", BINARY, LOGIC),
        NOT(5, '!', " NOT ", UNARY, LOGIC);

        private final int priority;
        private final char name;
        private final String strToString;
        private final BinaryType binaryType;
        private final LogicalType logicalType;

        private static final Map<Character, OperationDef> MAP = new HashMap<>();

        static {
            for (OperationDef item : OperationDef.values()) {
                MAP.put(item.name, item);
            }
        }

        OperationDef(int priority, char name, String strToString, BinaryType binaryType, LogicalType logicalType) {
            this.priority = priority;
            this.name = name;
            this.strToString = strToString;
            this.binaryType = binaryType;
            this.logicalType = logicalType;
        }

        public int getPriority() {
            return priority;
        }

        public char getName() {
            return name;
        }

        public BinaryType getBinaryType() {
            return binaryType;
        }

        public LogicalType getLogicalType() {
            return logicalType;
        }

        public boolean equals(char c) {
            return name == c;
        }

        @Override
        public String toString() {
            return strToString;
        }

        public static Optional<OperationDef> of(char token) {
            return Optional.ofNullable(MAP.get(token));
        }
    }

    public enum BinaryType {
        UNARY, BINARY;
    }

    public enum LogicalType {
        ARITHMETIC, LOGIC, SPLITER, COMPARATOR;
    }
}
