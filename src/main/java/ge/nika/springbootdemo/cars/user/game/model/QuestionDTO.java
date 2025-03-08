package ge.nika.springbootdemo.cars.user.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDTO {
    private String question;
    private String answer = "Answer Here (enter double values)";

     public QuestionDTO(String question){
         this.question = question;
     } //overloaded constructor to make questiondto in service

    //add proper getter later so that it says where to put an answer and that it should be double value
}
