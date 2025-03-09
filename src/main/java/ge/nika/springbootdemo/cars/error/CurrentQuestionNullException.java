package ge.nika.springbootdemo.cars.error;

public class CurrentQuestionNullException extends RuntimeException {
    public CurrentQuestionNullException(String message) {
        super(message);
    }
}
