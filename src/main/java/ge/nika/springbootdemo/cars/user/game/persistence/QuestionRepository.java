package ge.nika.springbootdemo.cars.user.game.persistence;

import ge.nika.springbootdemo.cars.user.game.model.QuestionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT MAX(q.id) FROM Question q")
    Long findMaxId();

    @Query("SELECT MIN(q.id) FROM Question q")
    Long findMinId();
}
