package ge.nika.springbootdemo.cars.user.game;

import ge.nika.springbootdemo.cars.error.*;
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

import java.util.HashMap;
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
    public void addQuestion(QuestionRequest request) {
        if (!request.getLevel().equals("Easy") &&
                !request.getLevel().equals("Medium") &&
                !request.getLevel().equals("Hard")) {
            throw new InvalidGameLevelException("Enter game level either: Easy, Medium, or Hard");
        }

        Question question = new Question();
        question.setLevel(request.getLevel());
        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());

        questionRepository.save(question);
    }

    //answerign to question
    public Map<String, Long> answerToQuestion(Map<String, Double> answer){
        if(currentQuestionId == null){//checking if user used get method first to sset currentQuestionId
            throw new CurrentQuestionNullException("Use get method first and then answer the given question");
        }

        if (!answer.containsKey("answer")) {
            throw new MissingFieldException("Missing Required Field: answer");
        }

        Double userAnswer = answer.get("answer");

        Optional<Question> optionalQuestion = questionRepository.findById(currentQuestionId);
        Question question = optionalQuestion.orElseThrow(() -> new NotFoundException("Question not found"));

        if (Math.abs(userAnswer - question.getAnswer()) > TOLERANCE) {//used tolerance so double values can be evaluated
            throw new InvalidAnswerException("Incorrect answer");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();//getting authorized users username
        AppUser user = userRepository.findByUsername(username)//getting user
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        Long earned = 0L;
        switch (question.getLevel()){
            case "Easy":
                user.setBalanceInCents(user.getBalanceInCents() + 1000);
                earned = 1000L;
                break;
            case "Medium":
                user.setBalanceInCents(user.getBalanceInCents() + 5000);
                earned = 5000L;
                break;
            case "Hard":
                user.setBalanceInCents(user.getBalanceInCents() + 10000);
                earned = 10000L;
                break;
        }
        Map<String, Long> response = new HashMap<>();
        response.put("You earned:", earned);

        userRepository.save(user);

        return response;
    }

   //map car
   private QuestionDTO mapQuestion(Question question){
       return new QuestionDTO(question.getLevel(), question.getQuestion());
   }
}
