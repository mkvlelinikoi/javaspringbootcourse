package ge.nika.springbootdemo.cars.user.game;

import ge.nika.springbootdemo.cars.error.InvalidAnswerException;
import ge.nika.springbootdemo.cars.error.MissingFieldException;
import ge.nika.springbootdemo.cars.error.NotFoundException;
import ge.nika.springbootdemo.cars.error.UsernameNotFoundException;
import ge.nika.springbootdemo.cars.user.game.model.QuestionDTO;
import ge.nika.springbootdemo.cars.user.game.model.QuestionRequest;
import ge.nika.springbootdemo.cars.user.game.persistence.Question;
import ge.nika.springbootdemo.cars.user.game.persistence.QuestionRepository;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import ge.nika.springbootdemo.cars.user.persistence.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GameService {

    private final QuestionRepository questionRepository;
    private final AppUserRepository userRepository;

    private static Long currentQuestionId;

    final Double TOLERANCE = 0.0001;

    //getting random question
    public ResponseEntity<QuestionDTO> getQuestion(){

        Long randomId = ThreadLocalRandom.current().nextLong(
                questionRepository.findMinId(), questionRepository.findMaxId() + 1);//started at two because in the database id strats this way
        currentQuestionId = randomId;
        QuestionDTO question = mapQuestion(questionRepository.findById(randomId).orElseThrow(
                () -> new NotFoundException("Question not found")
        ));
        return ResponseEntity.ok(question);
    }

    //adding a question
    public void addQuestion(QuestionRequest request){
        Question question = new Question();
        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());

        questionRepository.save(question);
    }

    //answerign to question
    public void answerToQuestion(Map<String, Double> answer){
        if (!answer.containsKey("answer")) {
            throw new MissingFieldException("Missing Required Field: answer");
        }

        Double userAnswer = answer.get("answer");

        Optional<Question> optionalQuestion = questionRepository.findById(currentQuestionId);
        Question question = optionalQuestion.orElseThrow(() -> new NotFoundException("Question not found"));

        System.out.println(question.getAnswer());

        if (Math.abs(userAnswer - question.getAnswer()) > TOLERANCE) {//used tolerance so double values can be evaluated
            throw new InvalidAnswerException("Incorrect answer");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();//getting authorized users username
        AppUser user = userRepository.findByUsername(username)//getting user
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        user.setBalanceInCents(user.getBalanceInCents() + 1000);
        userRepository.save(user);
    }

   //map car
   private QuestionDTO mapQuestion(Question question){
       return new QuestionDTO(question.getQuestion());
   }
}
