package ge.nika.springbootdemo.cars.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegativePriceException extends RuntimeException{
    public NegativePriceException(String message){
        super(message);
    }
}
