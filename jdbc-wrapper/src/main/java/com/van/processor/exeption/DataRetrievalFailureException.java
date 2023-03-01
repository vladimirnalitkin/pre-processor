package com.van.processor.exeption;

import com.van.processor.common.Nullable;

public class DataRetrievalFailureException extends NonTransientDataAccessException {
    public DataRetrievalFailureException(String msg) {
        super(msg);
    }

    public DataRetrievalFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}

