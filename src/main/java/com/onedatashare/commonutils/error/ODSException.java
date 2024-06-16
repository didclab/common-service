package com.onedatashare.commonutils.error;

public class ODSException extends ODSError{
    public ODSException(String exception, String type) {
        super("Exception occurred");
        this.type = type;
        error = exception;
    }
}
