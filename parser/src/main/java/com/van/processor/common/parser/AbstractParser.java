package com.van.processor.common.parser;

import com.van.processor.common.exeption.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.van.processor.common.Constant.SPACE;
import static com.van.processor.common.exeption.ExceptionMessage.INCORRECT_EXPRESSION;
import static com.van.processor.common.exeption.ExceptionMessage.PROHIBITED_SYMBOLS_MESSAGE;
import static com.van.processor.common.parser.Operation.OperationDef.*;
import static com.van.processor.common.parser.Operation.BinaryType.BINARY;

abstract class AbstractParser {
    private static final Logger log = LoggerFactory.getLogger(AbstractParser.class);

    static void initSet(final Set<Integer> result, String initValues) {
        initValues.chars().forEach(result::add);
    }

    abstract boolean prohibit(int c);

    abstract void operationsCheck(LinkedList<NodeList.Node<Expression>> opsList) throws ParseException;

    /**
     * Main internal method.
     *
     * @param inputExp
     * @return Expression
     */
    Expression parse(String inputExp) throws ParseException {
        final AtomicReference<StringBuilder> tokenItem = new AtomicReference<>(new StringBuilder(0));
        final LinkedList<NodeList.Node<Expression>> opsList = new LinkedList<>();
        final NodeList<Expression> tokens = new NodeList<>();
        final AtomicInteger depth = new AtomicInteger(0);
        final AtomicInteger balance = new AtomicInteger(0);

        log.debug(String.format("original exp = `%s`", inputExp));

        inputExp.trim().chars().forEach(c -> {
                    if (39 == c) { //39 -> '
                        if (balance.get() > 0) {
                            balance.decrementAndGet();
                        } else {
                            balance.incrementAndGet();
                        }
                    }
                    if (balance.get() != 0) {
                        //add constant
                        addToLiteral((char) c, tokenItem);
                    } else {
                        Optional<Operation.OperationDef> operationType = Operation.OperationDef.of((char) c);
                        // if Operation
                        if (operationType.isPresent()) {
                            if (OPEN_BRACKET.equals((char) c)) {
                                depth.incrementAndGet();
                            } else if (CLOSE_BRACKET.equals((char) c)) {
                                depth.decrementAndGet();
                            } else {
                                // save previos token
                                saveLiteral(tokenItem, tokens);
                                Expression operation = Expression.createOperation((char) c, operationType.get(), depth.get());
                                opsList.add(tokens.addLast(operation));
                            }
                        } else if (SPACE == c) {
                            // is Space
                            saveLiteral(tokenItem, tokens);
                        } else if (prohibit(c)) {
                            // if prohibit symbol
                            throw new ParseException(String.format(PROHIBITED_SYMBOLS_MESSAGE, (char) c));
                        } else {
                            //add literal
                            addToLiteralTrim((char) c, tokenItem);
                        }
                    }
                    //tokens.print();
                }
        );
        //if ' is not close
        if (balance.get() != 0) {
            throw new ParseException(INCORRECT_EXPRESSION);
        }
        // save previos token
        saveLiteral(tokenItem, tokens);
        log.debug("tokens list :");
        tokens.print();
        // make sure higher priority op types are looked at first
        opsList.sort(NodeList.Node::compareTo);
        operationsCheck(opsList);
        return reduceByOperation(tokens, opsList);
    }

    private void saveLiteral(AtomicReference<StringBuilder> token, NodeList<Expression> tokens) {
        if (token.get().length() > 0) {
            tokens.addLast(Expression.createLiteral(token.get()));
        }
        token.set(new StringBuilder(0));
    }

    private void addToLiteral(char c, AtomicReference<StringBuilder> token) {
        token.get().append(c);
    }

    private void addToLiteralTrim(char c, AtomicReference<StringBuilder> token) {
        if (32 != c) {
            token.get().append(c);
        }
    }

    private Expression reduceByOperation(NodeList<Expression> tokens, LinkedList<NodeList.Node<Expression>> opsList) throws ParseException {
        log.debug("Reduce start");
        while (!opsList.isEmpty()) {
            NodeList.Node<Expression> curOperationNode = opsList.poll();
            Expression curOperation = curOperationNode.get();
            if (BINARY.equals(curOperation.getOperationType().getBinaryType())) {
                NodeList.Node<Expression> leftNode = curOperationNode.getPrev();
                if (leftNode != null) {
                    Expression left = leftNode.get();
                    if (ExpressionType.LITERAL.equals(left.getExpressionType()) || ExpressionType.EXPRESSION.equals(left.getExpressionType())) {
                        curOperation.setLeft(left);
                    } else {
                        throw new ParseException(INCORRECT_EXPRESSION);
                    }
                    tokens.unlink(leftNode);
                    log.debug("unlink leftNode");
                    tokens.print();
                }
            }
            NodeList.Node<Expression> rightNode = curOperationNode.getNext();
            if (rightNode != null) {
                Expression right = rightNode.get();
                if (ExpressionType.LITERAL.equals(right.getExpressionType()) || ExpressionType.EXPRESSION.equals(right.getExpressionType())) {
                    curOperation.setRight(right);
                } else {
                    throw new ParseException(INCORRECT_EXPRESSION);
                }
                tokens.unlink(rightNode);
                log.debug("unlink rightNode");
                tokens.print();
                curOperation.setExpressionType(ExpressionType.EXPRESSION);
            } else {
                throw new ParseException(INCORRECT_EXPRESSION);
            }
            tokens.print();
        }
        log.debug("Reduce end");
        tokens.print();

        if (tokens.size() == 1) {
            return tokens.getFirstItem();
        } else {
            throw new ParseException(INCORRECT_EXPRESSION);
        }
    }
}
