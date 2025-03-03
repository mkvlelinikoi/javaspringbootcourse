package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.model.CarRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ge.nika.springbootdemo.cars.model.CarDTO;

import java.util.Map;

import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.ADMIN;
import static ge.nika.springbootdemo.cars.security.AuthorizationConstants.USER_OR_ADMIN;


@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarsController {

    private final CarsService carsService;

    @GetMapping
    @PreAuthorize(USER_OR_ADMIN) //get all cars (anybody can access)
    Page<CarDTO> getCars(@RequestParam int page, @RequestParam int pageSize){
        return carsService.getCars(page, pageSize);
    }

    @PostMapping
    @PreAuthorize(ADMIN) //add a new car (only admin can access)
    void addCar(@RequestBody @Valid CarRequest request){
        carsService.addCar(request);
    }

    @PutMapping("{id}")
    @PreAuthorize(ADMIN) //update the whole car (only admin can access)
    void updateCar(@PathVariable Long id, @RequestBody @Valid CarRequest request){
        carsService.updateCar(id, request);
    }

    /** this endpoint was created to update price, but can be used to update the whole car*/
    @PutMapping("/setPrice/{id}")
    @PreAuthorize(ADMIN)
    void updateCarPrice(@PathVariable Long id, @RequestBody @Valid CarRequest request){
        carsService.updateCar(id, request);
    }

    @PatchMapping("/setPrice/{id}")
    @PreAuthorize(ADMIN)
    void updateCarPrice(@PathVariable Long id, @RequestBody Map<String, Long> update){
        carsService.updateCarPrice(id, update);
    }

    @DeleteMapping("{id}")
    @PreAuthorize(ADMIN) //delete a car (only admin can access)
    void deleteCar(@PathVariable Long id){
        carsService.deleteCar(id);
    }

    @GetMapping("{id}")
    @PreAuthorize(ADMIN) //get car by id (only admin can access)
    ResponseEntity<CarDTO> getCar(@PathVariable Long id){
        CarDTO car = carsService.findCar(id);
        if(car != null){
            return ResponseEntity.ok(car);
        }
        return ResponseEntity.notFound().build();
    }
}
