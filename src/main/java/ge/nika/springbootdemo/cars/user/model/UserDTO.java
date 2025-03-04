package ge.nika.springbootdemo.cars.user.model;

import ge.nika.springbootdemo.cars.model.CarDTO;
import ge.nika.springbootdemo.cars.persistence.Car;
import ge.nika.springbootdemo.cars.user.persistence.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;
    private Long balanceInCents;
}
