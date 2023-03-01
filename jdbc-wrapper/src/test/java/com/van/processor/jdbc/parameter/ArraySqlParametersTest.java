package com.van.processor.jdbc.parameter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArraySqlParametersTest {
	private final String value1 = "val1";
	private final String value2 = "val2";

	private final int value_int1 = 12;
	private final int value_int2 = 13;

	//isEmpty
	@Test
	public void test_isEmpty_ok() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of();
		assertTrue(arraySqlBindingParameters.isEmpty());
	}

	@Test
	public void test_isEmpty_not() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(null);
		assertFalse(arraySqlBindingParameters.isEmpty());
	}

	//size
	@Test
	public void test_size_empty() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of();
		assertEquals(0, arraySqlBindingParameters.size());
	}

	@Test
	public void test_size_not_empty() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.ofValues(value_int1, value1, value2);
		assertEquals(3, arraySqlBindingParameters.size());
	}

	//ofSize
	@Test
	public void test_ofsize() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.ofSize(value_int1);
		assertEquals(0, arraySqlBindingParameters.size());
		arraySqlBindingParameters.addValue(value_int2);
		assertEquals(1, arraySqlBindingParameters.size());
	}

	//getValue
	@Test
	public void test_getValue_null() {
		Object expected = null;
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(null);
		assertEquals(expected, arraySqlBindingParameters.getValue(0));
	}

	@Test
	public void test_ofValues_null() {
		Object expected = null;
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.ofValues((Object) null);
		assertEquals(expected, arraySqlBindingParameters.getValue(0));
	}

	@Test
	public void test_getValue_not_null_by_pos() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(value1);
		assertEquals(value1, arraySqlBindingParameters.getValue(0));
	}

	@Test
	public void test_getValue_not_null_by_pos2() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.ofValues(value_int1, value1);
		assertEquals(value1, arraySqlBindingParameters.getValue(1));
	}

	//getValue
	@Test
	public void test_getValue_by_name() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(value1);
		assertThrows(UnsupportedOperationException.class, () ->
				arraySqlBindingParameters.getValue(value1), "Array Sql parameters does not support getValue by name");
	}

	//hasValue
	@Test
	public void test_hasValue() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(value1);
		assertThrows(UnsupportedOperationException.class, () ->
				arraySqlBindingParameters.hasValue(value1), "Array Sql parameters does not support hasValue function");
	}

	//get
	@Test
	public void test_get_ok() {
		SqlParameterValue expected = SqlParameterValue.of(null);
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(null);
		assertEquals(expected, arraySqlBindingParameters.get(0));
	}

	//get
	@Test
	public void test_get_negative() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(null);
		assertThrows(IllegalArgumentException.class, () ->
				arraySqlBindingParameters.get(1), "No value registered on position [1]");
	}

	@Test
	public void test_get_negative_minus() {
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(null);
		assertThrows(IllegalArgumentException.class, () ->
				arraySqlBindingParameters.get(-1), "No value registered on position [-1]");
	}

	//getValues
	@Test
	public void test_getValues_null() {
		Object[] expected = new Object[]{null};
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(null);
		assertArrayEquals(expected, arraySqlBindingParameters.getValues());
	}

	@Test
	public void test_getValues_not_null() {
		Object[] expected = new Object[]{value1, value2};
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(value1);
		arraySqlBindingParameters.addValue(value2);
		assertArrayEquals(expected, arraySqlBindingParameters.getValues());
	}

	@Test
	public void test_getValues_not_null_2() {
		Object[] expected = new Object[]{value1, value2, value1, value2, value1, value2, value1, value2};
		SqlParameters arraySqlBindingParameters = ArraySqlParameters.ofValues(expected);
		assertArrayEquals(expected, arraySqlBindingParameters.getValues());
	}

	//ofTypes
	@Test
	public void test_ofTypes_not_null() {
		SqlParameterValue expected = SqlParameterValue.of(value_int2, null);
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.ofTypes(value_int1, value_int2);
		assertEquals(expected, arraySqlBindingParameters.get(1));
	}

	//of
	@Test
	public void test_of_not_null() {
		SqlParameterValue expected = SqlParameterValue.of(value_int1, "Type", 7, value1);
		ArraySqlParameters arraySqlBindingParameters = ArraySqlParameters.of(value_int1, "Type", 7, value1);
		assertEquals(expected, arraySqlBindingParameters.get(0));
	}

}
