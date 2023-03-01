package com.van.processor.domain;

import com.google.common.base.Strings;
import com.van.processor.common.parser.Expression;
import com.van.processor.common.exeption.ParseException;
import com.van.processor.common.parser.WhereParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.Serializable;

public class Filter implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    private static final long serialVersionUID = 5737181232123323905L;

    private static final Filter UNFILTERED = Filter.by(Expression.EMPTY);

    private final Expression expression;

    public Filter(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    /**
     * Returns a {@link Filter} instances representing no sorting setup at all.
     *
     * @return
     */
    public static Filter unfiltered() {
        return UNFILTERED;
    }

    /**
     * Creates a new {@link Sort} for the given expression.
     *
     * @param expression string
     * @return
     */
    public static Filter by(String expression) throws ParseException {
        return Strings.isNullOrEmpty(expression) ? UNFILTERED : new Filter(WhereParser.of(expression));
    }

    /**
     * Creates a new {@link Filter} for the given {@link Expression}.
     *
     * @param expression must not be {@literal null}.
     * @return
     */
    public static Filter by(Expression expression) {

        Assert.notNull(expression, "Expressions must not be null!");

        return new Filter(expression);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + expression.hashCode();

        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Filter)) {
            return false;
        }

        Filter that = (Filter) obj;

        return this.expression.equals(that.getExpression());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s", expression);
    }
}
