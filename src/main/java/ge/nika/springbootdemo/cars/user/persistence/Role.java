package ge.nika.springbootdemo.cars.user.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@SequenceGenerator(name = "role_seq_gen", sequenceName = "role_seq", allocationSize = 1)
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(generator = "role_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;
}
