package ge.nika.springbootdemo.cars;

import org.springframework.stereotype.Component;

@Component
public class FuelEfficiencyCalculator {

    private static final double C1 = 500;
    private static final double C2 = 0.5;
    private static final double C3 = 0.3;
    private static final double C4 = 0.4;

    /**
     * Let's say formula is C1 / (HP^C2 * capacity^C3 * Weight^C4)
     * Higher the output -> more fuel-efficient car
     */
    public double calculatorFuelEfficiency(double horsePower, double capacity, double weightKg){
        if(horsePower < 0 || capacity < 0 || weightKg < 0){
            throw new IllegalArgumentException("All values must be positive");
        }
        return Math.round(C1 / (Math.pow(horsePower, C2) * Math.pow(capacity, C3) * Math.pow(weightKg, C4)) * 100.0) / 100.0;
    }
}
