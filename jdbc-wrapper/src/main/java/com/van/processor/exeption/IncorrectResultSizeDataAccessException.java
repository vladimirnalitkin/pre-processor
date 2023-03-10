package com.van.processor.exeption;

public class IncorrectResultSizeDataAccessException extends DataAccessException {
    private final int expectedSize;
    private final int actualSize;

    public IncorrectResultSizeDataAccessException(int expectedSize) {
        super("Incorrect result size: expected " + expectedSize);
        this.expectedSize = expectedSize;
        this.actualSize = -1;
    }

    public IncorrectResultSizeDataAccessException(int expectedSize, int actualSize) {
        super("Incorrect result size: expected " + expectedSize + ", actual " + actualSize);
        this.expectedSize = expectedSize;
        this.actualSize = actualSize;
    }

    public int getExpectedSize() {
        return this.expectedSize;
    }

    public int getActualSize() {
        return this.actualSize;
    }
}
