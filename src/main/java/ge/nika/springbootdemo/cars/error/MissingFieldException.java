package ge.nika.springbootdemo.cars.error;

public class MissingFieldException extends RuntimeException{

    public MissingFieldException(String message){
        super(message);
    }
}
