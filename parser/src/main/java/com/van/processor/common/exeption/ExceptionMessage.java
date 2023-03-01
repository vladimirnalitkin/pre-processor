package com.van.processor.common.exeption;

public interface ExceptionMessage {
    String QUOTATION_CLOSE_INCORRECTLY_MESSAGE = "quotation marks are closed incorrectly !";
    String PROHIBITED_SYMBOLS_MESSAGE = "prohibited symbols {%s} are using inside expression !";
    String INCORRECT_EXPRESSION = " Expression incorrect !";
    String INCORRECT_WERE_EXPRESSION = "In select statement the 'where' expression is incorrect!";
}
