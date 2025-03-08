package ge.nika.springbootdemo.cars.user.game.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "question")
@SequenceGenerator(name = "question_seq_gen", sequenceName = "question_seq", allocationSize = 1)
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(generator = "question_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private Double answer;
}
