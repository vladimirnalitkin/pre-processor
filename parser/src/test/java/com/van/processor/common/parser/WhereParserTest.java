package com.van.processor.common.parser;

import com.van.processor.common.exeption.ParseException;
import org.junit.jupiter.api.Test;

import static com.van.processor.common.exeption.ExceptionMessage.INCORRECT_EXPRESSION;
import static org.junit.jupiter.api.Assertions.*;

public class WhereParserTest {

    @Test
    public void parser_const_error() throws ParseException {
		assertThrows(ParseException.class, () ->
				WhereParser.of("a.test + 234")
				, INCORRECT_EXPRESSION);
    }

    @Test
    public void parser_const_ok() throws ParseException {
        Expression expression = WhereParser.of("a.test = 'qw sd '");
        assertNotNull(expression);
        System.out.println(expression);
		assertEquals("(a.test) = ('qw sd ')", expression.toString());
    }

    @Test
    public void parser_value_error() throws ParseException {
		assertThrows(ParseException.class, () ->
				WhereParser.of("a.test - a.wer")
				, INCORRECT_EXPRESSION);
    }

    @Test
    public void parser_and_ok() throws ParseException {
        Expression expression = WhereParser.of("a.test = 'qw sd ' & b.wer = 234");
        assertNotNull(expression);
        System.out.println(expression);
		assertEquals("((a.test) = ('qw sd '))  AND  ((b.wer) = (234))", expression.toString());
    }

    @Test
    public void parser_not_and_ok() throws ParseException {
        Expression expression = WhereParser.of("!(a.test = 'qw sd ') & !(b.wer = 234)");
        assertNotNull(expression);
        System.out.println(expression);
		assertEquals("( NOT  ((a.test) = ('qw sd ')))  AND  ( NOT  ((b.wer) = (234)))", expression.toString());
    }

    @Test
    public void parser_not_eqv_ok() throws ParseException {
        Expression expression = WhereParser.of(" a.test # 'qw sd ' ");
        assertNotNull(expression);
        System.out.println(expression);
		assertEquals("(a.test) != ('qw sd ')", expression.toString());
    }

    @Test
    public void parser_in_ok() throws ParseException {
		assertThrows(ParseException.class, () ->
				WhereParser.of(" a.test ? ['qw sd ', 'asdf']")
				, INCORRECT_EXPRESSION);
    }
}
