package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.model.EngineDTO;
import ge.nika.springbootdemo.cars.model.EngineRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.ADMIN;
import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/engines")
@RequiredArgsConstructor
public class EngineController {

    private final EngineService engineService;

    @GetMapping
    @PreAuthorize(USER_OR_ADMIN)
    Page<EngineDTO> getEngines(@RequestParam int page, @RequestParam int pageSize, @RequestParam double capacity) {
        return engineService.getEngines(page, pageSize, capacity);
    }
    @GetMapping("/all")
    @PreAuthorize(ADMIN)//additional endpoint for admin to get all engines
    Page<EngineDTO> getEngines(@RequestParam int page, @RequestParam int pageSize) {
        return engineService.getEngines(page, pageSize);
    }

    @GetMapping("{id}")
    @PreAuthorize(ADMIN)
    EngineDTO getEngine(@PathVariable Long id){
        return engineService.getEngine(id);
    }

    @PostMapping
    @PreAuthorize(ADMIN)
    ResponseEntity<Void> createEngine(@RequestBody @Valid EngineRequest request) {
        engineService.createEngine(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    @PreAuthorize(ADMIN)
    EngineDTO updateEngine(@PathVariable Long id, @RequestBody @Valid EngineRequest request){
        return engineService.updateEngine(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize(ADMIN)
    ResponseEntity<Void> deleteEngine(@PathVariable Long id){
        engineService.deleteEngine(id);
        return ResponseEntity.noContent().build();
    }
}
