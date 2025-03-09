package ge.nika.springbootdemo.cars.error;

public class InvalidGameLevelException extends RuntimeException {
    public InvalidGameLevelException(String message) {
        super(message);
    }
}
