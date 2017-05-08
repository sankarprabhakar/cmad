package com.cisco.cmad.blogs.api;

@SuppressWarnings("serial")
public class DuplicateEntityException extends EntityException {

    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(Throwable cause) {
        super(cause);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEntityException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
