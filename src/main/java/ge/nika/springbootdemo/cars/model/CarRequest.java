package ge.nika.springbootdemo.cars.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarRequest {
    @NotBlank
    @Size(max = 20)
    private String model;
    @Min(1940)
    private int year;
    private boolean driveable;
    @PositiveOrZero
    private Long priceInCents;
    @Positive
    private Long engineId;
    private String imageLink;
}
