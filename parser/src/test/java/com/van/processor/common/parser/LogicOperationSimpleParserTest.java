package com.van.processor.common.parser;

import com.van.processor.common.exeption.ParseException;
import org.junit.jupiter.api.Test;

import static com.van.processor.common.exeption.ExceptionMessage.INCORRECT_EXPRESSION;
import static org.junit.jupiter.api.Assertions.*;

public class LogicOperationSimpleParserTest {
	@Test
	public void parser_const_ok() throws ParseException {
		Expression expression = SimpleParser.of("a.test = 'qw sd ''asd'  & b.ert = 34");
		assertEquals("((a.test) = ('qw sd ''asd'))  AND  ((b.ert) = (34))", expression.toString());
	}

	@Test
	public void parser_const_error() throws ParseException {
		assertThrows(ParseException.class, () ->
						SimpleParser.of("a.test = 'qw sd ' 'asd'  & b.ert = 34")
				, INCORRECT_EXPRESSION);
	}

	@Test
	public void parser_where_ok() throws ParseException {
		Expression expression = SimpleParser.of("a.test = 'qw sd ' & b.ert = 34");
		assertNotNull(expression);
		assertEquals("((a.test) = ('qw sd '))  AND  ((b.ert) = (34))", expression.toString());
	}

	@Test
	public void parser_more_ok() throws ParseException {
		Expression expression = SimpleParser.of("a.test = 'qw sd ' & b.ert > 34");
		assertNotNull(expression);
		assertEquals("((a.test) = ('qw sd '))  AND  ((b.ert) > (34))", expression.toString());
	}

	@Test
	public void parser_not_equal_ok() throws ParseException {
		Expression expression = SimpleParser.of("a.test # 'qw sd '");
		assertNotNull(expression);
		assertEquals("(a.test) != ('qw sd ')", expression.toString());
	}

	@Test
	public void parser_not_equal_error() {
		assertThrows(ParseException.class, () ->
						SimpleParser.of("a.test ! = 'qw sd '")
				, INCORRECT_EXPRESSION);
	}

	@Test
	public void parser_not_ok() throws ParseException {
		Expression expression = SimpleParser.of("!(werwer = wew)");
		assertNotNull(expression);
		assertEquals(" NOT  ((werwer) = (wew))", expression.toString());
	}

	@Test
	public void parser_not2_ok() throws ParseException {
		Expression expression = SimpleParser.of("b.qwe = wer & !(a.test = 'qw sd ')");
		assertNotNull(expression);
		assertEquals("((b.qwe) = (wer))  AND  ( NOT  ((a.test) = ('qw sd ')))", expression.toString());
	}

	@Test
	public void parser_not3_ok() throws ParseException {
		Expression expression = SimpleParser.of("!(a.test = 'qw sd ')");
		assertNotNull(expression);
		assertEquals(" NOT  ((a.test) = ('qw sd '))", expression.toString());
	}

	@Test
	public void parser_minus_ok() throws ParseException {
		Expression expression = SimpleParser.of("-wewe");
		assertNotNull(expression);
		assertEquals("- (wewe)", expression.toString());
	}

	@Test
	public void parser_minus2_ok() throws ParseException {
		Expression expression = SimpleParser.of("-(wewe+1)");
		assertNotNull(expression);
		assertEquals("- ((wewe) + (1))", expression.toString());
	}

	@Test
	public void parser_where_func_ok() throws ParseException {
		Expression expression = SimpleParser.of("a.test = 'qw sd ' & b.ert = qwe()");
		assertNotNull(expression);
		assertEquals("((a.test) = ('qw sd '))  AND  ((b.ert) = (qwe))", expression.toString());
	}

	//@Test
	// TO DO: not work properly
	/*public void parser_where_func_with_param_ok() throws ParseException {
		Expression expression = SimpleParser.of("a.test = 'qw sd ' & b.ert = qwe(sdf, 'swwert')");
		assertNotNull(expression);
		System.out.println(expression);
		assertEquals("((a.test) = ('qw sd ''asd'))  AND  ((b.ert) = (34))", expression.toString());
	}*/

	@Test
	public void parser_where_error() throws ParseException {
		assertThrows(ParseException.class, () ->
						SimpleParser.of("a.test = 'qw sd  & b.ert = c.34")
				, INCORRECT_EXPRESSION);
	}

	@Test
	public void parser_where_semicolon_error() throws ParseException {
		assertThrows(ParseException.class, () ->
						SimpleParser.of("a.test = 'qw sd'; select * from users")
				, INCORRECT_EXPRESSION);
	}

	@Test
	public void parser_where_select_ok() throws ParseException {
		assertThrows(ParseException.class, () ->
						SimpleParser.of("a.test= 'qw sd ' & b.ert = 34 | a.test = (select *, t2.wer from table2 t2 where ta.key = 234)")
				, INCORRECT_EXPRESSION);
	}
}
