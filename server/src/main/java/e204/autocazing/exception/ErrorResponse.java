package e204.autocazing.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse{
    // Getters and Setters
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

}
