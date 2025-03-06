package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.user.model.UserDTO;
import ge.nika.springbootdemo.cars.user.model.UserRequest;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import ge.nika.springbootdemo.cars.user.persistence.AppUserRepository;
import ge.nika.springbootdemo.cars.user.persistence.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser(){
        // Given
        Set<Long> roles = Set.of(1L);
        UserRequest request = new UserRequest("user", "password", 5000L, roles);
        Role role = buildRole();

        when(roleService.getRole(1L)).thenReturn(role);
        when(passwordEncoder.encode("password")).thenReturn("12345678");
        when(userRepository.save(any(AppUser.class))).thenReturn(buildUser());

        // When
        userService.createUser(request);

        // Then
        verify(roleService).getRole(1L);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void testGetUser(){
        //Given
        String username = "user";
        AppUser user = buildUser();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //When
        AppUser foundUser = userService.getUser(username);

        //Then
        assertEquals(username, foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getBalanceInCents(), foundUser.getBalanceInCents());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testShowUserInfo(){
        //Given
        String username = "user";
        AppUser user = buildUser();


        //authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext); //to provide authentication
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //When
        ResponseEntity<UserDTO> response = userService.showUserInfo();

        //Then
        UserDTO result = response.getBody();
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getBalanceInCents(), result.getBalanceInCents());
        verify(userRepository).findByUsername(username);

    }

    //----------
    private AppUser buildUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("12345678");
        user.setBalanceInCents(5000L);
        user.setRoles(Set.of(buildRole()));

        return user;
    }

    private Role buildRole(){
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        return role;
    }
}
