package com.van.processor.common.parser;

import com.van.processor.common.exeption.ParseException;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import static com.van.processor.common.exeption.ExceptionMessage.INCORRECT_WERE_EXPRESSION;
import static com.van.processor.common.parser.Operation.LogicalType.COMPARATOR;

public class SimpleParser extends AbstractParser {
    private static final Logger log = LoggerFactory.getLogger(SimpleParser.class);

    private static final SimpleParser INSTANCE = new SimpleParser(";:");
    private final Set<Integer> prohibitSplitters = new HashSet<>();

    private SimpleParser(String prohibitValues) {
        initSet(prohibitSplitters, prohibitValues);
    }

    @Override
    boolean prohibit(int c) {
        return prohibitSplitters.contains(c);
    }

    @Override
    void operationsCheck(LinkedList<NodeList.Node<Expression>> opsList) throws ParseException {
    }

    /**
     * Main method - using instead of constructor.
     *
     * @param expression String
     * @return Expression
     */
    public static Expression of(String expression) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        Expression result = SimpleParser.INSTANCE.parse(expression);
        watch.stop();
        log.debug("Time Elapsed: " + watch.getTime());
        return result;
    }
}
