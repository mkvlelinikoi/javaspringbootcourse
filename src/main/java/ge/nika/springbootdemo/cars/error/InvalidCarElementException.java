package ge.nika.springbootdemo.cars.error;

public class InvalidCarElementException extends RuntimeException {
    public InvalidCarElementException(String message) {
        super(message);
    }
}
