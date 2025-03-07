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
    private String priceInDollars;
    private String carImage;
    private EngineDTO engine;

    public CarDTO(Long id, String model, int year, boolean driveable, Long priceInCents, String carImage, EngineDTO engine){
        this.id = id;
        this.model = model;
        this.year = year;
        this.driveable = driveable;
        this.priceInCents = priceInCents;
        this.carImage = carImage;
        this.engine = engine;

        setBalanceInDollars();
    }//overloaded CarDTO constructor so that i can show priceInDollars

    //added "Wait for pricing" for better UI
    public String getPriceInCents(){
        return (priceInCents != null && priceInCents == 0) ? "Waiting for price" : String.valueOf(priceInCents);
    }

    public void setBalanceInDollars(){
        priceInDollars = (double)(priceInCents / 100) + "$";
    }
}
