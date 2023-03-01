package com.van.processor.jdbc.parameter;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapSqlParametersTest {
	private final String paramName1 = "par1";
	private final String value1 = "val1";
	private final String paramName2 = "par2";
	private final String value2 = "val2";
	private final int value_int1 = 17;
	private final String paramName3 = "par3";

	@Test
	public void test_with_null_param_name() {
		assertThrows(AssertionError.class, () ->
				MapSqlParameters.of(null, value1), "Parameter name must not be null");
	}

	//isEmpty
	@Test
	public void test_isEmpty_ok() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of();
		assertTrue(mapSqlBindingParameters.isEmpty());
	}

	@Test
	public void test_isEmpty_null_ok() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(null);
		assertTrue(mapSqlBindingParameters.isEmpty());
	}

	@Test
	public void test_isEmpty_not() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, null);
		assertFalse(mapSqlBindingParameters.isEmpty());
	}

	//size
	@Test
	public void test_size_empty() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of();
		assertEquals(0, mapSqlBindingParameters.size());
	}

	@Test
	public void test_size_not_empty() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, value1);
		assertEquals(1, mapSqlBindingParameters.size());
		mapSqlBindingParameters.addValue(paramName2, value2);
		assertEquals(2, mapSqlBindingParameters.size());
		mapSqlBindingParameters.addValue(paramName2, value2);
		assertEquals(2, mapSqlBindingParameters.size());
	}

	//getValuesMap
	@Test
	public void test_getValuesMap_with_null_value() {
		Map<String, Object> expected = Collections.singletonMap(paramName1, (Object) null);
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, null);
		assertEquals(expected, mapSqlBindingParameters.getValuesMap());
	}

	@Test
	public void test_getValuesMap_with_not_null_value() {
		Map<String, Object> expected = Collections.singletonMap(paramName1, value1);
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, value1);
		assertEquals(expected, mapSqlBindingParameters.getValuesMap());
	}

	@Test
	public void test_getValuesMap_with_map_value() {
		Map<String, Object> expected = Collections.unmodifiableMap(new HashMap<String, Object>() {
			{
				put(paramName1, null);
				put(paramName2, value1);
				put(paramName3, value_int1);
			}
		});
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(expected);
		assertEquals(3, mapSqlBindingParameters.size());
		assertEquals(expected, mapSqlBindingParameters.getValuesMap());
		assertEquals(value1, mapSqlBindingParameters.getValue(paramName2));
	}

	//getValue
	@Test
	public void test_getValue_null() {
		Object expected = null;
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, null);
		assertEquals(expected, mapSqlBindingParameters.getValue(paramName1));
	}

	@Test
	public void test_getValue_not_null() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, value1);
		assertEquals(value1, mapSqlBindingParameters.getValue(paramName1));
	}

	@Test
	public void test_getValue_not_null_2() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, value_int1, value1, value_int1, value2);
		assertEquals(value2, mapSqlBindingParameters.getValue(paramName1));
	}

	@Test
	public void test_getValue_negative() {
		MapSqlParameters mapSqlBindingParameters = MapSqlParameters.of(paramName1, value_int1, value1, value_int1, value2);
		assertThrows(IllegalArgumentException.class, () ->
				mapSqlBindingParameters.getValue(paramName2), "No value registered for key '" + paramName2 + "'");
	}
}
