package ge.nika.springbootdemo.cars.user;

import ge.nika.springbootdemo.cars.error.NotFoundException;
import ge.nika.springbootdemo.cars.user.persistence.Role;
import ge.nika.springbootdemo.cars.user.persistence.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(Long id){
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
    }
}
