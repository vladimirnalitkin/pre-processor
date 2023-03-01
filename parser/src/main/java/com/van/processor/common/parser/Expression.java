package com.van.processor.common.parser;

import java.util.Optional;

import static com.van.processor.common.Constant.SPACE;
import static com.van.processor.common.parser.ExpressionType.LITERAL;
import static com.van.processor.common.parser.ExpressionType.OPERATION;
import static com.van.processor.common.parser.Operation.OperationDef.*;

public class Expression implements Comparable<Expression> {
    public static final Expression EMPTY = createLiteral(Character.MIN_VALUE);
    private ExpressionType expressionType;
    private final StringBuilder item;
    // Operation if it is ExpressionType#Operation.
    private Operation operation;
    private Expression left;
    private Expression right;

    private Expression(char c, ExpressionType expressionType, Operation operation) {
        this.item = new StringBuilder(0);
        if (Character.MIN_VALUE != c) {
            this.item.append(c);
        }
        this.expressionType = expressionType;
        this.operation = operation;
    }

    private Expression(StringBuilder sb, ExpressionType expressionType, Operation operation) {
        this.item = sb;
        this.expressionType = expressionType;
        this.operation = operation;
    }

    public static Expression of(char c) {
        Optional<Operation.OperationDef> operationType = Operation.OperationDef.of(c);
        return new Expression(c
                , operationType.isPresent() ? OPERATION : LITERAL
                , Operation.builder().operationType(operationType.orElse(NONE)).depth(0).build()
        );
    }

    public static Expression createLiteral(char c) {
        return new Expression(c
                , LITERAL
                , Operation.builder().operationType(NONE).depth(0).build()
        );
    }

    public static Expression createLiteral(StringBuilder sb) {
        return new Expression(sb
                , LITERAL
                , Operation.builder().operationType(NONE).depth(0).build()
        );
    }

    public static Expression createOperation(char c) {
        return of(c);
    }

    public static Expression createOperation(char c, Operation.OperationDef operationDef, int depth) {
        return new Expression(c
                , OPERATION
                , Operation.builder().operationType(operationDef).depth(depth).build()
        );
    }

    public boolean isEmpty() {
        return this.item.length() == 0;
    }

    public String getItem() {
        return this.item.toString();
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public Operation.OperationDef getOperationType() {
        return operation != null ? operation.getOperationDef() : null;
    }

    public int getDepth() {
        return operation != null ? operation.getDepth() : 0;
    }

    public void setDepth(int depth) {
        if (operation != null) {
            operation.setDepth(depth);
        }
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public String toString() {
        String result;
        switch (expressionType) {
            case LITERAL:
            case CONSTANT:
                result = getItem();
                break;
            case OPERATION:
                result = operation.getOperationDef().toString();
                break;
            case EXPRESSION:
                result = (left != null ? OPEN_BRACKET + left.toString() + CLOSE_BRACKET + SPACE : "")
                        + operation.getOperationDef()
                        + (right != null ? "" + SPACE + OPEN_BRACKET + right.toString() + CLOSE_BRACKET : "");
                break;
            default:
                result = "unknown";
                break;
        }
        return result;
    }

    @Override
    public int compareTo(Expression o) {
        return operation != null && o != null && o.operation != null ? operation.compareTo(o.operation) : 0;
    }
}
