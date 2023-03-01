package com.van.processor.jdbc.sql;

import com.van.processor.jdbc.parameter.MapSqlParameters;
import com.van.processor.jdbc.parameter.SqlParameters;
import com.van.processor.jdbc.parameter.SqlParametersFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParsedParametersSqlTest {

	//substituteParameters
	@Test
	public void testSubstituteNamedParametersArray() {
		String expectedSql = "select 'first name' from artists where id = ?, ?, ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		String newSql = psql.substituteParameters(MapSqlParameters.of("id", Arrays.asList(23, 45, 77)));
		assertEquals(expectedSql, newSql);
	}

	@Test
	public void testSubstituteNamedParametersArray2() {
		String expectedSql = "select 'first name' from artists where id = ?, ?, ? and test = ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = :id and test = :ver and quote = 'exsqueeze me?'";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		String newSql = psql.substituteParameters(MapSqlParameters.of("id", Arrays.asList(23, 45, 77)));
		assertEquals(expectedSql, newSql);
	}

	@Test
	public void testSubstituteParametersArray() {
		String expectedSql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		String newSql = psql.substituteParameters(SqlParametersFactory.ofTypes(23, 45, 77));
		assertEquals(expectedSql, newSql);
	}

	@Test
	public void testSubstituteParametersArray2() {
		String expectedSql = "select 'first name' from artists where id = ?, ?, ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = ?, ?, ? and quote = 'exsqueeze me?'";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		String newSql = psql.substituteParameters(SqlParametersFactory.ofTypes(23, 45, 77));
		assertEquals(expectedSql, newSql);
	}

	@Test
	public void testParseSql1() {
		String sql = "xxx :a yyyy :b :c :a zzzzz";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals("xxx ? yyyy ? ? ? zzzzz", psql.substituteParameters(null));
		assertEquals("a", psql.getNamedParameter(0).getParameterName());
		assertEquals("c", psql.getNamedParameter(2).getParameterName());
		assertEquals("a", psql.getNamedParameter(3).getParameterName());
		assertEquals(4, psql.getTotalParameterCount());
		assertEquals(3, psql.getNamedParameterCount());
	}

	@Test
	public void testParseSql2() {
		String sql2 = "xxx &a yyyy :a zzzzz";
		ParsedParametersSql psql2 = ParsedParametersSqlFactory.getParsedSql(sql2);
		assertEquals("xxx ? yyyy ? zzzzz", psql2.substituteParameters(null));
		assertEquals(2, psql2.getTotalParameterCount());
		assertEquals(1, psql2.getNamedParameterCount());
	}

	@Test
	public void testParseSql3() {
		String sql3 = "xxx &ä+:ö" + '\t' + ":ü%10 yyyy :a zzzzz";
		ParsedParametersSql psql3 = ParsedParametersSqlFactory.getParsedSql(sql3);
		assertEquals("ä", psql3.getNamedParameter(0).getParameterName());
		assertEquals("ö", psql3.getNamedParameter(1).getParameterName());
		assertEquals("ü", psql3.getNamedParameter(2).getParameterName());
	}

	@Test
	public void testSubstituteNamedParameters() {
		MapSqlParameters namedParams = MapSqlParameters.of();
		namedParams.addValue("a", "a").addValue("b", "b").addValue("c", "c");
		assertEquals("xxx ? ? ?", ParsedParametersSqlFactory.substituteNamedParameters("xxx :a :b :c", namedParams));
		assertEquals("xxx ? ? ? xx ? ?", ParsedParametersSqlFactory.substituteNamedParameters("xxx :a :b :c xx :a :a", namedParams));
	}

	@Test
	public void testSubstituteNamedParametersWithStringContainingQuotes() {
		String expectedSql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		String newSql = ParsedParametersSqlFactory.substituteNamedParameters(sql, MapSqlParameters.of());
		assertEquals(expectedSql, newSql);
	}

	@Test
	public void testParseSqlStatementWithStringContainingQuotes() {
		String expectedSql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(expectedSql, parsedSql.substituteParameters(null));
	}

	@Test
	public void testParseSqlContainingComments() {
		String sql1 = "/*+ HINT */ xxx /* comment ? */ :a yyyy :b :c :a zzzzz -- :xx XX\n";
		ParsedParametersSql psql1 = ParsedParametersSqlFactory.getParsedSql(sql1);
		assertEquals("/*+ HINT */ xxx /* comment ? */ ? yyyy ? ? ? zzzzz -- :xx XX\n", psql1.substituteParameters(null));
		MapSqlParameters paramMap = MapSqlParameters.of();
		paramMap.addValue("a", "a");
		paramMap.addValue("b", "b");
		paramMap.addValue("c", "c");
		SqlParameters params = NamedParameterUtils.preparedBindingNamedParameters(psql1, paramMap, null);
		assertEquals(4, params.size());
		assertEquals("a", params.getValue(0));
		assertEquals("b", params.getValue(1));
		assertEquals("c", params.getValue(2));
		assertEquals("a", params.getValue(3));

		String sql2 = "/*+ HINT */ xxx /* comment ? */ :a yyyy :b :c :a zzzzz -- :xx XX";
		ParsedParametersSql psql2 = ParsedParametersSqlFactory.getParsedSql(sql2);
		assertEquals("/*+ HINT */ xxx /* comment ? */ ? yyyy ? ? ? zzzzz -- :xx XX", psql2.substituteParameters(null));

		String sql3 = "/*+ HINT */ xxx /* comment ? */ :a yyyy :b :c :a zzzzz /* :xx XX*";
		ParsedParametersSql psql3 = ParsedParametersSqlFactory.getParsedSql(sql3);
		assertEquals("/*+ HINT */ xxx /* comment ? */ ? yyyy ? ? ? zzzzz /* :xx XX*", psql3.substituteParameters(null));
		String sql4 = "/*+ HINT */ xxx /* comment :a ? */ :a yyyy :b :c :a zzzzz /* :xx XX*";
		ParsedParametersSql psql4 = ParsedParametersSqlFactory.getParsedSql(sql4);
		Map<String, String> parameters = Collections.singletonMap("a", "0");
		assertEquals("/*+ HINT */ xxx /* comment :a ? */ ? yyyy ? ? ? zzzzz /* :xx XX*", psql4.substituteParameters(MapSqlParameters.of(parameters)));
	}

	@Test
	public void testParseSqlStatementWithPostgresCasting() {
		String expectedSql = "select 'first name' from artists where id = ? and birth_date=?::timestamp";
		String sql = "select 'first name' from artists where id = :id and birth_date=:birthDate::timestamp";
		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(expectedSql, parsedSql.substituteParameters(null));
	}

	@Test
	public void testParseSqlStatementWithPostgresContainedOperator() {
		String expectedSql = "select 'first name' from artists where info->'stat'->'albums' = ?? ? and '[\"1\",\"2\",\"3\"]'::jsonb ?? '4'";
		String sql = "select 'first name' from artists where info->'stat'->'albums' = ?? :album and '[\"1\",\"2\",\"3\"]'::jsonb ?? '4'";
		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(1, parsedSql.getTotalParameterCount());
		assertEquals(expectedSql, parsedSql.substituteParameters(null));
	}

	@Test
	public void testParseSqlStatementWithPostgresAnyArrayStringsExistsOperator() {
		String expectedSql = "select '[\"3\", \"11\"]'::jsonb ?| '{1,3,11,12,17}'::text[]";
		String sql = "select '[\"3\", \"11\"]'::jsonb ?| '{1,3,11,12,17}'::text[]";
		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(0, parsedSql.getTotalParameterCount());
		assertEquals(expectedSql, parsedSql.substituteParameters(null));
	}

	@Test
	public void testParseSqlStatementWithPostgresAllArrayStringsExistsOperator() {
		String expectedSql = "select '[\"3\", \"11\"]'::jsonb ?& '{1,3,11,12,17}'::text[] AND ? = 'Back in Black'";
		String sql = "select '[\"3\", \"11\"]'::jsonb ?& '{1,3,11,12,17}'::text[] AND :album = 'Back in Black'";
		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(1, parsedSql.getTotalParameterCount());
		assertEquals(expectedSql, parsedSql.substituteParameters(null));
	}

	@Test
	public void testParseSqlStatementWithEscapedColon() {
		String expectedSql = "select '0\\:0' as a, foo from bar where baz < DATE(? 23:59:59) and baz = ?";
		String sql = "select '0\\:0' as a, foo from bar where baz < DATE(:p1 23\\:59\\:59) and baz = :p2";

		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(2, parsedSql.getTotalParameterCount());
		assertEquals("p1", parsedSql.getNamedParameter(0).getParameterName());
		assertEquals("p2", parsedSql.getNamedParameter(1).getParameterName());
		String finalSql = parsedSql.substituteParameters(null);
		assertEquals(expectedSql, finalSql);
	}

	@Test
	public void testParseSqlStatementWithBracketDelimitedParameterNames() {
		String expectedSql = "select foo from bar where baz = b??z";
		String sql = "select foo from bar where baz = b:{p1}:{p2}z";

		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(2, parsedSql.getNamedParameterCount());
		assertEquals("p1", parsedSql.getNamedParameter(0).getParameterName());
		assertEquals("p2", parsedSql.getNamedParameter(1).getParameterName());
		String finalSql = parsedSql.substituteParameters(null);
		assertEquals(expectedSql, finalSql);
	}

	@Test
	public void testParseSqlStatementWithEmptyBracketsOrBracketsInQuotes() {
		String expectedSql = "select foo from bar where baz = b:{}z";
		String sql = "select foo from bar where baz = b:{}z";
		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(0, parsedSql.getNamedParameterCount());
		String finalSql = parsedSql.substituteParameters(null);
		assertEquals(expectedSql, finalSql);
	}

	@Test
	public void testParseSqlStatementWithEmptyBracketsOrBracketsInQuotes2() {
		String expectedSql = "select foo from bar where baz = 'b:{p1}z'";
		String sql = "select foo from bar where baz = 'b:{p1}z'";

		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(0, parsedSql.getNamedParameterCount());
		String finalSql = parsedSql.substituteParameters(null);
		assertEquals(expectedSql, finalSql);
	}

	@Test
	public void testParseSqlStatementWithSingleLetterInBrackets() {
		String expectedSql = "select foo from bar where baz = b?z";
		String sql = "select foo from bar where baz = b:{p}z";

		ParsedParametersSql parsedSql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(1, parsedSql.getNamedParameterCount());
		assertEquals("p", parsedSql.getNamedParameter(0).getParameterName());
		String finalSql = parsedSql.substituteParameters(null);
		assertEquals(expectedSql, finalSql);
	}

	@Test
	public void testParseSqlStatementWithLogicalAnd() {
		String expectedSql = "xxx & yyyy";
		ParsedParametersSql ParsedParametersSql = ParsedParametersSqlFactory.getParsedSql(expectedSql);
		String finalSql = ParsedParametersSql.substituteParameters(null);
		assertEquals(expectedSql, finalSql);
	}

	@Test
	public void testSubstituteNamedParametersWithLogicalAnd() {
		String expectedSql = "xxx & yyyy";
		String finalSql = ParsedParametersSqlFactory.substituteNamedParameters(expectedSql, MapSqlParameters.of());
		assertEquals(expectedSql, finalSql);
	}

	@Test  // SPR-3173
	public void testVariableAssignmentOperator() {
		String expectedSql = "x := 1";
		String finalSql = ParsedParametersSqlFactory.substituteNamedParameters(expectedSql, MapSqlParameters.of());
		assertEquals(expectedSql, finalSql);
	}

	@Test  // SPR-8280
	public void testParseSqlStatementWithQuotedSingleQuote() {
		String sql = "SELECT ':foo'':doo', :xxx FROM DUAL";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(1, psql.getTotalParameterCount());
		assertEquals("xxx", psql.getNamedParameter(0).getParameterName());
	}

	@Test
	public void testParseSqlStatementWithQuotesAndCommentBefore() {
		String sql = "SELECT /*:doo*/':foo', :xxx FROM DUAL";
		ParsedParametersSql psql = ParsedParametersSqlFactory.getParsedSql(sql);
		assertEquals(1, psql.getTotalParameterCount());
		assertEquals("xxx", psql.getNamedParameter(0).getParameterName());
	}

	@Test
	public void testParseSqlStatementWithQuotesAndCommentAfter() {
		String sql2 = "SELECT ':foo'/*:doo*/, :xxx FROM DUAL";
		ParsedParametersSql psql2 = ParsedParametersSqlFactory.getParsedSql(sql2);
		assertEquals(1, psql2.getTotalParameterCount());
		assertEquals("xxx", psql2.getNamedParameter(0).getParameterName());
	}

	@Test
	public void testParseSqlStatement_Un_Names() {
		String sql2 = "SELECT * FROM DUAL WHERE id=? and we=?";
		ParsedParametersSql psql2 = ParsedParametersSqlFactory.getParsedSql(sql2);
		assertEquals(2, psql2.getTotalParameterCount());
		assertEquals(2, psql2.getUnnamedParameterCount());
		assertEquals(0, psql2.getNamedParameterCount());
	}

	@Test
	public void testParseSqlStatement_Names() {
		String sql2 = "SELECT * FROM DUAL WHERE id=:id and we=:we";
		ParsedParametersSql psql2 = ParsedParametersSqlFactory.getParsedSql(sql2);
		assertEquals(2, psql2.getTotalParameterCount());
		assertEquals(0, psql2.getUnnamedParameterCount());
		assertEquals(2, psql2.getNamedParameterCount());
	}
}
