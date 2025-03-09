package ge.nika.springbootdemo.cars.user.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDTO {
    private String level;
    private String question;
    private String answer = "Answer Here (enter double values)[NOTE: Hard = +10000, Medium = +5000, Easy = +1000]";

     public QuestionDTO(String level, String question){
         this.level = level;
         this.question = question;
     } //overloaded constructor to make questiondto in service
}
