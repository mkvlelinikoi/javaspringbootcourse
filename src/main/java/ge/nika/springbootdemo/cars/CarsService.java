package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.error.*;
import ge.nika.springbootdemo.cars.model.CarDTO;
import ge.nika.springbootdemo.cars.model.CarRequest;
import ge.nika.springbootdemo.cars.model.EngineDTO;
import ge.nika.springbootdemo.cars.persistence.Car;
import ge.nika.springbootdemo.cars.persistence.CarRepository;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import ge.nika.springbootdemo.cars.user.persistence.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarRepository carRepository;
    private final EngineService engineService;
    private final AppUserRepository userRepository;

    //* <- tested

    public Page<CarDTO> getCars(int page, int pageSize){
        return carRepository.findCars(PageRequest.of(page, pageSize));
    }//*

    public void addCar(CarRequest request){
        Car car = new Car();
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setDriveable(request.isDriveable());
        car.setPriceInCents(request.getPriceInCents());
        car.setEngine(engineService.findEngine((request.getEngineId())));

        carRepository.save(car);
    }

    public void updateCar(Long id, CarRequest request){
        Car car = carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setDriveable(request.isDriveable());
        car.setPriceInCents(request.getPriceInCents());
        if (car.getEngine().getId() != request.getEngineId()){
            car.setEngine(engineService.findEngine(request.getEngineId()));
        }
        carRepository.save(car);
    }//*

    //method to update Only car price
    public void updateCarPrice(Long id, Map<String, Long> update){
        if(!update.containsKey("priceInCents")){ /**Made a custom exception for missing field*/
            throw new MissingFieldException("Missing required Field: priceInCents");
        }

        Long newPrice = update.get("priceInCents");
        if(newPrice < 0){ /**Made a custom exception for negative price*/
            throw new NegativePriceException("Price cannot be negative");
        }
        Car car = carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));

        car.setPriceInCents(newPrice);
        carRepository.save(car);
    }//*

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }//*

    public CarDTO findCar(Long id){
        Car car = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car with id "+ id + " not found"));
        return mapCar(car);
    }//*

    //TRANSACTIONAL
    public void buyCar(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); //we get name of the user

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        Car car = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car with id " + id + " not found"));

        if(user.getBalanceInCents() < car.getPriceInCents()){
            throw new InsufficientBalanceException("Insufficient balance to buy " + car.getModel());
        }

        user.setBalanceInCents(user.getBalanceInCents() - car.getPriceInCents());
        user.getCars().add(car);
        user.setNumberOfOwnedCars(user.getNumberOfOwnedCars() + 1);
        car.getOwners().add(user);

        userRepository.save(user);
        carRepository.save(car);
    }

    public Page<CarDTO> getOwnedCars(int page, int pageSize){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Long id = userRepository.findByUsername(username).get().getId();//getting name so that i can get id

        return carRepository.getOwnedCars(id, PageRequest.of(page, pageSize));
    }

    public void sellCar(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();//getting name of the current user

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        Car car = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car with id " + id + " not found"));

        if(user.getCars().contains(car)){
            user.getCars().remove(car);
        }else {
            throw new InvalidCarElementException("User doesn't own a car with id " + id);
        }
        user.setNumberOfOwnedCars(user.getNumberOfOwnedCars() - 1);
        car.getOwners().remove(user);
        user.setBalanceInCents(user.getBalanceInCents() + (long) (car.getPriceInCents() * 0.8));//returning back 80%

        userRepository.save(user);
        carRepository.save(car);
    }
    //-------------

    private CarDTO mapCar(Car car){
        return new CarDTO(car.getId(), car.getModel(), car.getYear(), car.isDriveable(), car.getPriceInCents(),
                new EngineDTO(
                        car.getEngine().getId(),
                        car.getEngine().getHorsePower(),
                        car.getEngine().getCapacity()));
    }

    private NotFoundException buildNotFoundException(Long id){
        return new NotFoundException("Car with id " + id + " not found");
    }
}
