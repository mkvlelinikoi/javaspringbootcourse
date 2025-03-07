package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.error.InsufficientBalanceException;
import ge.nika.springbootdemo.cars.model.CarDTO;
import ge.nika.springbootdemo.cars.model.CarRequest;
import ge.nika.springbootdemo.cars.model.EngineDTO;
import ge.nika.springbootdemo.cars.persistence.Car;
import ge.nika.springbootdemo.cars.persistence.CarRepository;
import ge.nika.springbootdemo.cars.persistence.Engine;
import ge.nika.springbootdemo.cars.user.persistence.AppUser;
import ge.nika.springbootdemo.cars.user.persistence.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private  CarRepository carRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private  EngineService engineService;

    @InjectMocks
    private CarsService carsService;

    //for transactional method tests
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    //------------------------------

    //testing getCars method
    @Test
    void testGetCars(){
        //Given
        PageRequest pageRequest = PageRequest.of(0, 10);
        CarDTO carDTO = new CarDTO(1L, "Vaz", 1975, true, 2000L, "",
                new EngineDTO(1000L, 150, 2.0));
        Page<CarDTO> cars = new PageImpl<>(List.of(carDTO));
        when(carRepository.findCars(pageRequest)).thenReturn(cars);

        //When
        Page<CarDTO> result = carsService.getCars(0, 10);

        //Then
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().getFirst().getId());
        assertEquals("Vaz", result.getContent().getFirst().getModel());
        assertEquals(1975, result.getContent().getFirst().getYear());
        assertEquals("2000", result.getContent().getFirst().getPriceInCents());
        assertEquals(1000L, result.getContent().getFirst().getEngine().getId());
        assertEquals(150, result.getContent().getFirst().getEngine().getHorsePower());
        assertEquals(2.0, result.getContent().getFirst().getEngine().getCapacity());
        verify(carRepository).findCars(pageRequest);//pretty massive
    }

    @Test
    void testAddCar(){
        //Given
        CarRequest request = new CarRequest("Vaz", 1975, true, 2000L, 1000L);
        Engine engine = engineService.findEngine(request.getEngineId());
        when(engineService.findEngine(request.getEngineId())).thenReturn(buildEngine());
        when(carRepository.save(any(Car.class))).thenReturn(buildCar());

        //When
        carsService.addCar(request);

        //Then
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void testUpdateCar(){
        //Given
        CarRequest request = new CarRequest("Vaz", 1975, true, 2000L, 1000L);
        Car car = buildCar();
        when(carRepository.findById(1L)).thenReturn(Optional.of(buildCar()));

        //When
        carsService.updateCar(1L, request);

        //Then
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void testUpdateCarPrice(){
        //Given
        Map<String, Long> update = new HashMap<>();
        update.put("priceInCents", 2000L);
        Car car = buildCar();
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        //When
        carsService.updateCarPrice(car.getId(), update);

        //Then
        verify(carRepository).save(car);
        assertEquals(2000L, car.getPriceInCents());
    }

    @Test
    void deleteCar(){
        //Given
        Long id = 1L;

        //When
        carRepository.deleteById(id);

        //Then
        verify(carRepository).deleteById(id);
    }

    @Test
    void testFindCar(){
        //Given
        Long id = 1L;
        Car car = buildCar();
        CarDTO carDTO = new CarDTO(car.getId(), car.getModel(), car.getYear(), car.isDriveable(), car.getPriceInCents(),
                car.getCarImage(),
                new EngineDTO(car.getEngine().getId(), car.getEngine().getHorsePower(), car.getEngine().getCapacity()));
        when(carRepository.findById(id)).thenReturn(Optional.of(buildCar()));

        //When
        CarDTO result = carsService.findCar(id);

        //Then
        assertEquals(carDTO, result);
        verify(carRepository).findById(id);
    }

    //--BUY CAR--
    @Test
    void testBuyCar(){
        //Given
        String username = "user";
        Car car = buildCar();
        AppUser user = buildUser();
        car.setOwners(new HashSet<>());//setting new set so  its not null
        user.setCars(new ArrayList<>());//setting new List so its not null

        //authenticating
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        //when
        carsService.buyCar(car.getId());

        //Then
        Long expectedBalance = 3000L;
        assertEquals(expectedBalance, user.getBalanceInCents());
        assertTrue(user.getCars().contains(car));//checking if correct
        assertTrue(car.getOwners().contains(user));
        verify(carRepository).save(any(Car.class));
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void testBuyCar_ShouldThrowInsufficientBalanceException(){
        //Given
        String username = "user";
        Car car = buildCar();
        AppUser user = buildUser();
        user.setBalanceInCents(1000L);
        car.setOwners(new HashSet<>());//setting new set so  its not null
        user.setCars(new ArrayList<>());//setting new List so its not null

        //authenticating
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        //When
        assertThrows(InsufficientBalanceException.class, () ->
                carsService.buyCar(car.getId()));

        //Then
        verify(carRepository, never()).save(any(Car.class));
        verify(userRepository, never()).save(any(AppUser.class));
        //using never() we made sure save(any(class)) was never called
    }
    //------ add more test if had time

    //--GET OWNED CARS--
    @Test
    void testGetOwnedCars(){
        String username = "user";
        PageRequest request   = PageRequest.of(0, 10);
        CarDTO carDTO = new CarDTO(1L, "Vaz", 1975, true, 2000L, "",
                new EngineDTO(1000L, 150, 2.0));
        Page<CarDTO> cars = new PageImpl<>(List.of(carDTO));
        AppUser user = buildUser();

        //authenticating
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(carRepository.getOwnedCars(user.getId(), request)).thenReturn(cars);

        //When
        Page<CarDTO> result = carsService.getOwnedCars(0,10);

        //Then
        assertEquals(1, result.getContent().size());
        verify(carRepository).getOwnedCars(user.getId(), request);
    }

    @Test
    void testGetOwnedCars_NoOwnedCars(){
        //Given
        String username = "user";
        PageRequest request = PageRequest.of(0, 10);
        Page<CarDTO> noCarsToOwe = new PageImpl<>(Collections.EMPTY_LIST);
        AppUser user = buildUser();
        user.setCars(new ArrayList<>());

        //authenticating
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(carRepository.getOwnedCars(user.getId(), request)).thenReturn(noCarsToOwe);

        //When
        Page<CarDTO> result = carsService.getOwnedCars(0, 10);

        //Then
        assertEquals(0, result.getContent().size());//should be zero if page is empty
        verify(carRepository).getOwnedCars(user.getId(), request);
    }
    //------add more test cases later

    //--Sell Car--
    @Test
    void testSellCar(){
        //Given
        String username = "user";
        Car car = buildCar();
        AppUser user = buildUser();
        car.setOwners(new HashSet<>());//setting new set so  its not null
        user.setCars(new ArrayList<>());//setting new List so its not null

        //authenticating
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        //When
        carsService.sellCar(car.getId());

        //Then
        Long expectedBalance = 6600L; //80% of cars price
        assertEquals(expectedBalance, user.getBalanceInCents());
        verify(carRepository).save(any(Car.class));
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void testSellCar_WrongSellingPriceCalculation() {
        // Given
        String username = "user";
        Car car = buildCar();
        AppUser user = buildUser();
        car.setOwners(new HashSet<>());
        user.setCars(new ArrayList<>());

        //Authenticating
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        //When
        carsService.sellCar(car.getId());

        //Then
        Long correctBalance = 6600L;// correct 80% price (5000 + (2000 * 0.8)) = 6600
        Long wrongBalance = user.getBalanceInCents() + (long) (car.getPriceInCents() * 0.7); // not correct 70% price
        assertNotEquals(wrongBalance, user.getBalanceInCents(), "Balance calculation should be 80% of car price, not 70%!");
        assertEquals(correctBalance, user.getBalanceInCents());
        verify(carRepository).save(any(Car.class));
        verify(userRepository).save(any(AppUser.class));
    }
    //-----

    private Car buildCar(){
        Car car = new Car();
        car.setId(1L);
        car.setModel("Vaz");
        car.setDriveable(true);
        car.setPriceInCents(2000L);
        car.setEngine(buildEngine());

        return car;
    }

    private AppUser buildUser(){
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("user");
        user.setBalanceInCents(5000L);
        return user;
    }

    private Engine buildEngine() {
        Engine engine = new Engine();
        engine.setId(1000L);
        engine.setCapacity(2.0);
        engine.setHorsePower(150);
        return engine;
    }
}
