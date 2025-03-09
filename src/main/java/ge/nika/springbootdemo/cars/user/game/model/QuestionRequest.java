package ge.nika.springbootdemo.cars.user.game.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank
    private String level;
    @NotBlank
    @Size(max = 100)
    private String question;
    @NotNull
    private Double answer;
}
