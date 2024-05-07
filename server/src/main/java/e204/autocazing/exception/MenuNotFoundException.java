package e204.autocazing.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(String message) {
        super(message);
    }

    public MenuNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}


