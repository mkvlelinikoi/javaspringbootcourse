package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.user.persistence.Role;
import ge.nika.springbootdemo.cars.user.persistence.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testGetRole(){
        //Given
        Long id = 1L;
        Role role = buildRole();
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        //When
        Role result = roleService.getRole(id);

        //Then
        assertEquals(id, result.getId());
        assertEquals(role.getName(), result.getName());
        verify(roleRepository).findById(id);
    }
//-----
    private Role buildRole(){
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        return role;
    }
}
