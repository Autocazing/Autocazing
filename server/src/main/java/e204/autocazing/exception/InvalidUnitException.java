package e204.autocazing.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class InvalidUnitException extends RuntimeException {
    public InvalidUnitException(String message) {
        super(message);
    }
}

