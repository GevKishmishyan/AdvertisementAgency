package exception;

public class ExistingModelException extends Exception {

    public ExistingModelException() {
    }

    public ExistingModelException(String message) {
        super(message);
    }

    public ExistingModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingModelException(Throwable cause) {
        super(cause);
    }

    public ExistingModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
