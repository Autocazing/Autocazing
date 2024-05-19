package e204.autocazing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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
    //주문시 재료부족 -> 400에러
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "InsufficientStock",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //이미 있는 재료를 장바구니에 추가 -> 409에러
    @ExceptionHandler(IngredientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleIngredientAlreadyExistsException(IngredientAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "IngredientAlreadyExists",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUnitException.class)
    public ResponseEntity<String> handleInvalidUnitException(InvalidUnitException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}


