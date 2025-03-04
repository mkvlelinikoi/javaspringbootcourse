package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.user.model.UserDTO;
import ge.nika.springbootdemo.cars.user.model.UserRequest;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.ADMIN;
import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}
