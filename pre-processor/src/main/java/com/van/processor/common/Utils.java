package com.van.processor.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class Utils {
    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public final static String TYPE_OBJECT = "Object";
    public final static String DELIMITER_POINT = ".";
    public final static String DELIMITER_GENERAL = "<";
    public final static String DELIMITER_GENERAL_CLOSE = ">";

    private static Map<String, String> JAVA_TO_SQL = new HashMap<>();

    static {
        JAVA_TO_SQL.put("Boolean", "Boolean");
        JAVA_TO_SQL.put("boolean", "Boolean");

        JAVA_TO_SQL.put("Byte", "Byte");
        JAVA_TO_SQL.put("byte", "Byte");

        JAVA_TO_SQL.put("Short", "Short");
        JAVA_TO_SQL.put("short", "Short");

        JAVA_TO_SQL.put("Integer", "Int");
        JAVA_TO_SQL.put("int", "Int");

        JAVA_TO_SQL.put("Long", "Long");
        JAVA_TO_SQL.put("long", "Long");

        JAVA_TO_SQL.put("Float", "Float");
        JAVA_TO_SQL.put("float", "Float");

        JAVA_TO_SQL.put("Double", "Double");
        JAVA_TO_SQL.put("double", "Double");

        JAVA_TO_SQL.put("BigDecimal", "BigDecimal");
        JAVA_TO_SQL.put("String", "String");
        JAVA_TO_SQL.put("Date", "Date");
        JAVA_TO_SQL.put("Timestamp", "Timestamp");
    }

    public static String getLastWord(String input, String wordSeparator) {
        boolean inputIsOnlyOneWord = !StringUtils.contains(input, wordSeparator);
        if (inputIsOnlyOneWord) {
            return input;
        }
        return StringUtils.substringAfterLast(input, wordSeparator);
    }

    public static String getLastWord(String input) {
        return getLastWord(input, DELIMITER_POINT);
    }

    public static String getClassOfGeneric(String input) {
        String result = getLastWord(input, DELIMITER_GENERAL);
        return !StringUtils.contains(result, DELIMITER_GENERAL_CLOSE) ? result : result.substring(0, result.indexOf(DELIMITER_GENERAL_CLOSE));
    }

    public static String getCollectioType(String input) {
        String result = TYPE_OBJECT;
        if(StringUtils.contains(input, DELIMITER_GENERAL)) {
            result = getLastWord(input.substring(0, input.indexOf(DELIMITER_GENERAL)));
        }
        return result;
    }

    public static String javaToSqlTypeConvert(String type) {
        return JAVA_TO_SQL.getOrDefault(type, "String");
    }

}
