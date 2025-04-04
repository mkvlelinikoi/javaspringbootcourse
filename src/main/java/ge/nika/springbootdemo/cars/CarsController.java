package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.model.CarRequest;
import ge.nika.springbootdemo.cars.user.persistence.AppUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ge.nika.springbootdemo.cars.model.CarDTO;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{id}/image")
    @PreAuthorize(USER_OR_ADMIN)
    String getCarImage(@PathVariable Long id){
        return carsService.getCarImage(id);
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

    @PatchMapping("/setPrice/{id}")//update price ONLY
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
        return ResponseEntity.ok(carsService.findCar(id));
    }

    //-------------USER-TRANSACTION-------------
    @GetMapping("/buy/{id}")
    @PreAuthorize(USER_OR_ADMIN)
    void buyCar(@PathVariable Long id){
        carsService.buyCar(id);
    }

    @GetMapping("/owned")
    @PreAuthorize(USER_OR_ADMIN)
    Page<CarDTO> getOwnedCars(@RequestParam int page, @RequestParam int pageSize){
        return carsService.getOwnedCars(page, pageSize);
    }

    @DeleteMapping("/sell/{id}")
    @PreAuthorize(USER_OR_ADMIN)
    void sellCar(@PathVariable Long id){
        carsService.sellCar(id);
    }

    //----------Update Car Image------------
    @PatchMapping("/updateImage/{id}")
    @PreAuthorize(ADMIN)
    void updateCarImage(@PathVariable Long id,@RequestBody Map<String, String> update){
        carsService.updateCarImage(id, update);
    }

    @PostMapping("/{id}/uploadImage")
    @PreAuthorize(ADMIN)//may have some problems //TODO: ADVANCE FUNCTIONALITY
    ResponseEntity<String> uploadCarImage(@PathVariable Long id, @RequestParam(value = "file")MultipartFile file){
        return new ResponseEntity<>(carsService.uploadCarImage(file), HttpStatus.OK);
    }
}
