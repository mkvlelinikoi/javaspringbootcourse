package ge.nika.springbootdemo.cars.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationError(MethodArgumentNotValidException exception){
        String errorMessage = exception.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("Invalid-Request", errorMessage));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundError(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("not-found", exception.getMessage()));
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorDTO> handleInvalidLogin(InvalidLoginException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid-Login", exception.getMessage()));
    }

    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ErrorDTO> handleMissingField(MissingFieldException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Invalid-Field", exception.getMessage()));
    }

    @ExceptionHandler(NegativePriceException.class)
    public ResponseEntity<ErrorDTO> handleNegativePrice(NegativePriceException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Invalid-Pricing", exception.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleUsernameNotFound(UsernameNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Invalid-Username", exception.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDTO> handleInsufficientBalance(InsufficientBalanceException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("Invalid-Transaction", exception.getMessage()));
    }

    @ExceptionHandler(InvalidCarElementException.class)
    public ResponseEntity<ErrorDTO> handleInvalidCarElement(InvalidCarElementException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Invalid-Car", exception.getMessage()));
    }
}
