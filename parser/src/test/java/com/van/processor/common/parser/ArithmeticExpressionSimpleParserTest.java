package com.van.processor.common.parser;

import com.van.processor.common.exeption.ParseException;
import org.junit.jupiter.api.Test;

import static com.van.processor.common.exeption.ExceptionMessage.INCORRECT_EXPRESSION;
import static org.junit.jupiter.api.Assertions.*;

public class ArithmeticExpressionSimpleParserTest {
	@Test
	public void parser_simple1_ok() throws ParseException {
		Expression expression = SimpleParser.of("2 + 1");
		assertNotNull(expression);
		System.out.println(expression);
	}

	@Test
	public void parser_simple1_error() {
		assertThrows(ParseException.class, () ->
				SimpleParser.of("2 4 + 1 - 'wer wqer'"), INCORRECT_EXPRESSION);
	}

	@Test
	public void parser_simple11_ok() throws ParseException {
		Expression expression = SimpleParser.of("24 + (1 - 'wer wqer')");
		assertNotNull(expression);
		assertEquals("(24) + ((1) - ('wer wqer'))", expression.toString());
	}

	@Test
	public void parser_simple2_ok() throws ParseException {
		Expression expression = SimpleParser.of("( 2 + 1  ) - rt5 ");
		assertNotNull(expression);
		assertEquals("((2) + (1)) - (rt5)", expression.toString());
	}

	@Test
	public void parser_simple3_ok() throws ParseException {
		Expression expression = SimpleParser.of(" (2 + 1) - 12 * 'asdw ' + (2345 / 45 - 2 ) ");
		assertNotNull(expression);
		assertEquals("(((2) + (1)) - ((12) * ('asdw '))) + (((2345) / (45)) - (2))", expression.toString());
	}

	@Test
	public void parser_double_quots_ok() throws ParseException {
		Expression expression = SimpleParser.of(" '''asdw '' ' + 3");
		assertNotNull(expression);
		assertEquals("('''asdw '' ') + (3)", expression.toString());
	}

	@Test
	public void parser_double_quots_error() throws ParseException {
		assertThrows(ParseException.class, () ->
				SimpleParser.of(" '''asdw ' ' + 3"), INCORRECT_EXPRESSION);
	}

}
