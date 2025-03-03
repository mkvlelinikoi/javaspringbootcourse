package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.model.CarRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ge.nika.springbootdemo.cars.model.CarDTO;

import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.ADMIN;
import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.USER_OR_ADMIN;


@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarsController {

    private final CarsService carsService;

    @GetMapping
    @PreAuthorize(USER_OR_ADMIN)
    Page<CarDTO> getCars(@RequestParam int page, @RequestParam int pageSize){
        return carsService.getCars(page, pageSize);
    }

    @PostMapping
    @PreAuthorize(ADMIN)
    void addCar(@RequestBody @Valid CarRequest request){
        carsService.addCar(request);
    }

    @PutMapping("{id}")
    @PreAuthorize(ADMIN)
    void updateCar(@PathVariable Long id, @RequestBody @Valid CarRequest request){
        carsService.updateCar(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize(ADMIN)
    void deleteCar(@PathVariable Long id){
        carsService.deleteCar(id);
    }

    @GetMapping("{id}")
    @PreAuthorize(ADMIN)
    ResponseEntity<CarDTO> getCar(@PathVariable Long id){
        CarDTO car = carsService.findCar(id);
        if(car != null){
            return ResponseEntity.ok(car);
        }
        return ResponseEntity.notFound().build();
    }
}
