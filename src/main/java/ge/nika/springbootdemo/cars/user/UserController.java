package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.user.model.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.ADMIN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize(ADMIN)
public class UserController {

    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody @Valid UserRequest request){
        userService.createUser(request);
    }
}
