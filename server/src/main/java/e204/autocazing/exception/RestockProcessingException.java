package e204.autocazing.exception;

public class RestockProcessingException extends Exception {
    public RestockProcessingException(String message) {
        super(message);
    }

    public RestockProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
