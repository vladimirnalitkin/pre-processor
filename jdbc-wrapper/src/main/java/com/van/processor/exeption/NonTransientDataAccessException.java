package com.van.processor.exeption;

import com.van.processor.common.Nullable;

public abstract class NonTransientDataAccessException extends DataAccessException {
    public NonTransientDataAccessException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
