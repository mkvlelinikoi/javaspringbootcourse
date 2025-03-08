package ge.nika.springbootdemo.cars.error;

public class InvalidAnswerException extends RuntimeException {
  public InvalidAnswerException(String message) {
    super(message);
  }
}
