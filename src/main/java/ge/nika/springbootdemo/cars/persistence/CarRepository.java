package ge.nika.springbootdemo.cars.persistence;

import ge.nika.springbootdemo.cars.model.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT NEW ge.nika.springbootdemo.cars.model.CarDTO(c.id, c.model, c.year, c.driveable, c.priceInCents, c.carImage, " +
            "NEW ge.nika.springbootdemo.cars.model.EngineDTO(e.id, e.horsePower, e.capacity)) " +
            "FROM Car c JOIN c.engine e")
    Page<CarDTO> findCars(Pageable pageable);

    @Query("SELECT NEW ge.nika.springbootdemo.cars.model.CarDTO(c.id, c.model, c.year, c.driveable, c.priceInCents, c.carImage, " +
            "NEW ge.nika.springbootdemo.cars.model.EngineDTO(e.id, e.horsePower, e.capacity)) " +
            "FROM Car c JOIN c.owners o JOIN c.engine e WHERE o.id = :userId")
    Page<CarDTO> getOwnedCars(Long userId, Pageable pageable);
}
