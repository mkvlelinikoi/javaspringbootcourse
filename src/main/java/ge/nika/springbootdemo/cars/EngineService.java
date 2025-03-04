package ge.nika.springbootdemo.cars;

import ge.nika.springbootdemo.cars.error.NotFoundException;
import ge.nika.springbootdemo.cars.model.EngineDTO;
import ge.nika.springbootdemo.cars.model.EngineRequest;
import ge.nika.springbootdemo.cars.persistence.Engine;
import ge.nika.springbootdemo.cars.persistence.EngineRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EngineService {
    private final EngineRepository engineRepository;

    public Page<EngineDTO> getEngines(int page, int pageSize, double capacity) {
        return engineRepository.findEngines(capacity, PageRequest.of(page, pageSize));
    }
    public Page<EngineDTO> getEngines(int page, int pageSize) {
        return engineRepository.findEngines(PageRequest.of(page, pageSize));
    }


    public EngineDTO getEngine(long id){
        Engine engine=engineRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
        return mapEngine(engine);
    }

    public void createEngine(EngineRequest engineRequest) {
        Engine engine=new Engine();
        engine.setCapacity(engineRequest.getCapacity());
        engine.setHorsePower(engineRequest.getHorsePower());
        engineRepository.save(engine);
    }

    public EngineDTO updateEngine(Long id, EngineRequest request){
        Engine engine = engineRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));

        engine.setHorsePower(request.getHorsePower());
        engine.setCapacity(request.getCapacity());
        engineRepository.save(engine);

        return mapEngine(engine);
    }

    public void deleteEngine(Long id){
        engineRepository.deleteById(id);
    }

    public Engine findEngine(Long id){
        return engineRepository.findById(id).orElseThrow(() -> buildNotFoundException(id));
    }



    private EngineDTO mapEngine(Engine engine){
        return new EngineDTO(
                engine.getId(),
                engine.getHorsePower(),
                engine.getCapacity());
    }

    private NotFoundException buildNotFoundException(Long id){
        return new NotFoundException("Engine with id " + id + " not found");
    }


}
