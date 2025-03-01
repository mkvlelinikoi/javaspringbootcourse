package ge.nika.springbootdemo.cars.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EngineDTO {

    private Long id;
    private int horsePower;
    private double capacity;
}
