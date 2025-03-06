package ge.nika.springbootdemo.cars.login;

import ge.nika.springbootdemo.cars.auth.LoginRequest;
import ge.nika.springbootdemo.cars.auth.LoginResponse;
import ge.nika.springbootdemo.cars.auth.LoginService;
import ge.nika.springbootdemo.cars.user.UserService;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    void login(){
        //Given
        AppUser user = buildUser();
        LoginRequest request = new LoginRequest("user", "password");
        String testJWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXM" +
                "iOlsiVVNFUiJdLCJpc3MiOiJjYXJzYXBwLmdlIiwiZXhwIjoxNjAwMDAwMDAwfQ";//test token
        LoginResponse expectedResult = new LoginResponse(testJWT);
        when(userService.getUser(request.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        LoginService loginServiceSpy = spy(loginService);
        /// as it turned out this method wont work because it invokes the real method and hits the catch in try-catch (secretKey = null - probably)*/
        //when(loginServiceSpy.generateLoginResponse(user)).thenReturn(expectedResult); we can use instead
        doReturn(expectedResult).when(loginServiceSpy).generateLoginResponse(user);//doReturn avvoids real generateLoginResponse method

        //changed generateLoginRespone to public for next line

        //when
        LoginResponse response = loginServiceSpy.login(request);

        //Then
        assertEquals(testJWT, response.getAccessToken());
        verify(userService).getUser(request.getUsername());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
    }

    private AppUser buildUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("12345678");
        user.setBalanceInCents(5000L);

        return user;
    }
}
