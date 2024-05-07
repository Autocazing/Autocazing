package e204.autocazing.exception;

public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message, MenuNotFoundException e) {
        super(message);
    }
}
