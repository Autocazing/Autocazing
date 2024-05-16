package e204.autocazing.exception;

public class InsufficientStockException extends RuntimeException{
    private int errorCode;

    public InsufficientStockException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
