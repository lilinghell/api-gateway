package com.hell.core.exception;

/**
 * 业务验证Exception
 */
public class ValidationException extends Exception {
    private String errorCode;
    private String errorMsg;
    private String messageKey;
    private Object[] args;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
        this.messageKey = message;
    }

    public ValidationException(String message, Object[] args) {
        super(message, null);
        this.messageKey = message;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
