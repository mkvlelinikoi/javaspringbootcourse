package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.user.game.GameService;
import ge.nika.springbootdemo.cars.user.game.model.QuestionDTO;
import ge.nika.springbootdemo.cars.user.game.model.QuestionRequest;
import ge.nika.springbootdemo.cars.user.game.persistence.Question;
import ge.nika.springbootdemo.cars.user.model.UserDTO;
import ge.nika.springbootdemo.cars.user.model.UserRequest;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.ADMIN;
import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GameService gameService;

    @GetMapping("/info")//user information page
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<UserDTO> getUser(){
        return userService.showUserInfo();
    }

    @PostMapping
    @PreAuthorize(ADMIN)
    public void createUser(@RequestBody @Valid UserRequest request){
        userService.createUser(request);
    }

    //--------EARNING MONEY---------
    @GetMapping("/earnMoney")
    @PreAuthorize(USER_OR_ADMIN)
    public ResponseEntity<QuestionDTO> getRandomQuestion(){
        return gameService.getQuestion();
    }

    @PostMapping("/earnMoney/add")//adding question
    @PreAuthorize(ADMIN)
    public void addQuestion(@RequestBody QuestionRequest request){
        gameService.addQuestion(request);
    }

    @PatchMapping("/earnMoney")
    @PreAuthorize(USER_OR_ADMIN)
    public void answerToQuestion(@RequestBody Map<String, Double> answer){
        gameService.answerToQuestion(answer);
    }
}
