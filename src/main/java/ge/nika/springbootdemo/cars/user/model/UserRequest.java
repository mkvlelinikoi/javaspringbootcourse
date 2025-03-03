package ge.nika.springbootdemo.cars.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;
    @PositiveOrZero
    private Long balanceInCents;

    @NotEmpty
    private Set<Long> roleIds;
}
