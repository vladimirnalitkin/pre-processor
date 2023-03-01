package com.van.processor.common.exeption;

public class ParseException extends RuntimeException {
    public ParseException(String errorMessage) {
        super(errorMessage);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}