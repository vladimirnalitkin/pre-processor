package com.van.processor.exeption;

import com.van.processor.common.Nullable;

public abstract class UncategorizedDataAccessException extends NonTransientDataAccessException {
    public UncategorizedDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
