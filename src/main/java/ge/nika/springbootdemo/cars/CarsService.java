package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.error.NotFoundException;
import ge.nika.springbootdemo.cars.model.CarDTO;
import ge.nika.springbootdemo.cars.model.CarRequest;
import ge.nika.springbootdemo.cars.model.EngineDTO;
import ge.nika.springbootdemo.cars.persistence.Car;
import ge.nika.springbootdemo.cars.persistence.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarRepository carRepository;
    private final EngineService engineService;

    public Page<CarDTO> getCars(int page, int pageSize){
        return carRepository.findCars(PageRequest.of(page, pageSize));

    }

    public void addCar(CarRequest requst){
        Car car = new Car();
        car.setModel(requst.getModel());
        car.setYear(requst.getYear());
        car.setDriveable(requst.isDriveable());
        car.setEngine(engineService.findEngine((requst.getEngineId())));

        carRepository.save(car);
    }

    public void updateCar(Long id, CarRequest request){
        Car car = carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setDriveable(request.isDriveable());
        if (car.getEngine().getId() != request.getEngineId()){
            car.setEngine(engineService.findEngine(request.getEngineId()));
        }
        carRepository.save(car);
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }

    public CarDTO findCar(long id){
        Car car = carRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        return mapCar(car);
    }

    private CarDTO mapCar(Car car){
        return new CarDTO(car.getId(), car.getModel(), car.getYear(), car.isDriveable(),
                new EngineDTO(
                        car.getEngine().getId(),
                        car.getEngine().getHorsePower(),
                        car.getEngine().getCapacity()));
    }

    private NotFoundException buildNotFoundException(Long id){
        return new NotFoundException("Car with id " + id + " not found");
    }
}
