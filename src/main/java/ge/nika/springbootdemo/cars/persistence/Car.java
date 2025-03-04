package ge.nika.springbootdemo.cars.persistence;

import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car")
@SequenceGenerator(name = "car_seq_gen", sequenceName = "car_seq", allocationSize = 1)
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(generator = "car_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "is_driveable")
    private boolean driveable;

    @Column(name = "price_in_cents")
    private Long priceInCents;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToMany(mappedBy = "cars",fetch = FetchType.LAZY)
    private Set<AppUser> owners = new HashSet<>();
}
