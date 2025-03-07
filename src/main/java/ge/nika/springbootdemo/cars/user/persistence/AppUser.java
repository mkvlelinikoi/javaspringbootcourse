package ge.nika.springbootdemo.cars.user.persistence;

import ge.nika.springbootdemo.cars.persistence.Car;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user", schema = "cars")
@SequenceGenerator(name = "app_user_seq_gen", sequenceName = "app_user_seq", allocationSize = 1)
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(generator = "app_user_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name = "balance_in_cents")
    private Long balanceInCents;

    @Column(name = "number_of_owned_cars")
    private int  numberOfOwnedCars = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "car_owner",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    private List<Car> cars = new ArrayList<>();
}
