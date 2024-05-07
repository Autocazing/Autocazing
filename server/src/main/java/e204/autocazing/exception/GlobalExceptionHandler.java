package e204.autocazing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<String> handleOrderProcessingException(OrderProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order processing error: " + e.getMessage());
    }

    @ExceptionHandler(RestockProcessingException.class)
    public ResponseEntity<String> handleRestockProcessingException(RestockProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during restocking: " + e.getMessage());
    }

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<String> handleMenuNotFoundException(MenuNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found error: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
    }
}
