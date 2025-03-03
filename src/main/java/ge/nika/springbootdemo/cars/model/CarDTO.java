package ge.nika.springbootdemo.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private String model;
    private int year;
    private boolean driveable;
    private Long priceInCents;
    private EngineDTO engine;

    //added "Wait for pricing" for better UI
    public String getPriceInCents(){
        return (priceInCents != null && priceInCents == 0) ? "Waiting for price" : String.valueOf(priceInCents);
    }
}
