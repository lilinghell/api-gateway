package ms.frame.core.exception;

/**
 * 业务验证Exception
 */
public class ValidationException extends Exception {
    private String errorCode;
    private String errorMsg;
    private Object[] args;

    public ValidationException() {
    }

    public ValidationException(String code) {
        super(code);
        this.errorCode = code;
    }

    public ValidationException(String code, Object[] args) {
        super(code, null);
        this.errorCode = code;
        this.args = args;
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
