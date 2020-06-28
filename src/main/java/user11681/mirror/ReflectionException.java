package user11681.mirror;

public class ReflectionException extends RuntimeException {
    public ReflectionException() {
        super();
    }

    public ReflectionException(final String message) {
        super(message);
    }

    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(final Throwable cause) {
        super(cause);
    }

    protected ReflectionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
