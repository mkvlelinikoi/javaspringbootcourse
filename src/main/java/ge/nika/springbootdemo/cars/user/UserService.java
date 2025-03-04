package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.error.NotFoundException;
import ge.nika.springbootdemo.cars.user.model.UserDTO;
import ge.nika.springbootdemo.cars.user.model.UserRequest;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import ge.nika.springbootdemo.cars.user.persistence.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public void createUser(UserRequest request){
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalanceInCents(request.getBalanceInCents());
        user.setRoles(request.getRoleIds().stream()
                .map(roleService::getRole)
                .collect(Collectors.toSet()));

        repository.save(user);
    }

    public AppUser getUser(String username){
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public ResponseEntity<UserDTO> showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); //we got name of current user

        AppUser user = getUser(username);
        UserDTO dto = new UserDTO(user.getUsername(), user.getBalanceInCents());

        return ResponseEntity.ok(dto);
    }
}
