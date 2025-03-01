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
    private EngineDTO engine;

}
