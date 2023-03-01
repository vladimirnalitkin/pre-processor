package com.van.processor.jdbc.parameter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlParametersFactoryTest {
	private final String value1 = "val1";
	private final String value2 = "val2";

	private final int value_int1 = 12;
	private final int value_int2 = 13;

	//ofValues
	@Test
	public void test_isEmpty_ok() {
		SqlParameters sqlParameters = SqlParametersFactory.ofValues();
		assertEquals(0, sqlParameters.size());
		assertTrue(sqlParameters.isEmpty());
	}

	@Test
	public void test_isEmpty_null() {
		SqlParameters sqlParameters = SqlParametersFactory.ofValues((Object[]) null);
		assertEquals(0, sqlParameters.size());
		assertTrue(sqlParameters.isEmpty());
	}

	@Test
	public void test_isEmpty_null_inside_array() {
		SqlParameters sqlParameters = SqlParametersFactory.ofValues((Object) null);
		assertFalse(sqlParameters.isEmpty());
		assertEquals(1, sqlParameters.size());
	}

	@Test
	public void test_size_not_empty() {
		SqlParameters sqlParameters = SqlParametersFactory.ofValues(value_int1, value1, value2);
		assertFalse(sqlParameters.isEmpty());
		assertEquals(3, sqlParameters.size());
	}

	@Test
	public void test_ofValues_Objects() {
		Object[] params = new Object[] {23, 45, 77};
		SqlParameters sqlParameters = SqlParametersFactory.ofValues(params);
		assertFalse(sqlParameters.isEmpty());
		assertEquals(3, sqlParameters.size());
	}

	@Test
	public void test_ofValues_List() {
		List<Integer> expectValue = Arrays.asList(23, 45, 77);
		SqlParameters sqlParameters = SqlParametersFactory.ofValues(expectValue);
		assertFalse(sqlParameters.isEmpty());
		assertEquals(1, sqlParameters.size());
	}

	//ofTypes
	@Test
	public void test_ofTypes_empty() {
		SqlParameters sqlParameters = SqlParametersFactory.ofTypes((int[]) null);
		assertEquals(0, sqlParameters.size());
		assertTrue(sqlParameters.isEmpty());
	}

	@Test
	public void test_ofTypes_not_empty() {
		SqlParameters sqlParameters = SqlParametersFactory.ofTypes(1, 3, 5, 7);
		assertEquals(4, sqlParameters.size());
		assertFalse(sqlParameters.isEmpty());
	}

	//ofArray
	@Test
	public void test_size_empty() {
		SqlParameters sqlParameters = SqlParametersFactory.ofArray(null, null);
		assertEquals(0, sqlParameters.size());
		assertTrue(sqlParameters.isEmpty());
	}

	//ofSize
	@Test
	public void test_ofSize() {
		SqlParameters sqlParameters = SqlParametersFactory.ofSize(value_int2);
		assertEquals(0, sqlParameters.size());
		assertTrue(sqlParameters.isEmpty());
	}

}
