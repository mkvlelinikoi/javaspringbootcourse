package ge.nika.springbootdemo.cars;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FuelEfficiencyCalculatorTest {

    private final FuelEfficiencyCalculator fuelEfficiencyCalculator = new FuelEfficiencyCalculator();

    @Test
    void shouldCalculateFuelEfficiencyCorrectly(){
        double horsePower = 400;
        double capacity = 4.4;
        double weightKg = 1800;

        double expectedResult = 0.8;
        double actualResult = fuelEfficiencyCalculator.calculatorFuelEfficiency(horsePower, capacity, weightKg);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldThrowExceptionIfInvalidHorsePower(){
        double horsePower = -1;
        double capacity = 4.4;
        double weightKg = 1800;

        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> fuelEfficiencyCalculator.calculatorFuelEfficiency(horsePower, capacity, weightKg));
    }

    @Test
    void shouldThrowExceptionIfInvalidCapacity(){
        double horsePower = 400;
        double capacity = -1;
        double weightKg = 1800;

        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> fuelEfficiencyCalculator.calculatorFuelEfficiency(horsePower, capacity, weightKg));
    }

    @Test
    void shouldThrowExceptionIfInvalidWeight(){
        double horsePower = 400;
        double capacity = 4.4;
        double weightKg = -12;

        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> fuelEfficiencyCalculator.calculatorFuelEfficiency(horsePower, capacity, weightKg));
    }
}
