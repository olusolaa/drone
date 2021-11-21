package musala;


import lombok.extern.slf4j.Slf4j;
import musala.model.Drone;
import musala.model.Estate;
import musala.repository.DestinationRepository;
import musala.repository.DroneRepository;
import musala.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Slf4j
@Configuration
@EnableScheduling
public class scheduledConfiguration {


    private final DroneService droneService;
    private final DroneRepository droneRepository;
    private final DestinationRepository destinationRepository;
    private List<Drone> loadedDrones = new ArrayList<>();

    @Autowired
    public scheduledConfiguration(DroneService droneService, DroneRepository droneRepository, DestinationRepository destinationRepository) {
        this.droneService = droneService;
        this.droneRepository = droneRepository;
        this.destinationRepository = destinationRepository;
    }


    @Scheduled(fixedDelay = 50000)
    public void executeTask2() {
        if (droneService.isAutoPilot()){
            droneRepository.saveAll(loadedDrones);
            log.info("Retrieve all loaded drones: " + new Date());
            loadedDrones = droneRepository.findByStateEquals(Estate.LOADED);
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void executeTask3() {
        if (droneService.isAutoPilot()) {
            log.info("Retrieve drone distance to destination : " + new Date());
            for ( int i = 0; i < loadedDrones.size(); i++){
                Drone drone = loadedDrones.get(i);
                drone.setDistance(drone.getDistance() - 1);
                log.info("Drone {} is {} km to {} : {}", drone.getSerialNumber(), drone.getDistance(), drone.getDestination().getCity(), new Date());
                loadedDrones.set(i, drone);
                if (drone.getDistance() == 0) {
                    droneService.unloadDrone(drone.getId());
                    loadedDrones.remove(drone);
                }
            }
        }
    }

    @Scheduled(fixedDelay = 20000)
    public void executeTask4() {
        if (droneService.isAutoPilot()) {
            log.info("Retrieving drone battery information: " + new Date());
            for (Drone loadedDrone : loadedDrones) {
                droneService.chekDroneBatteryStatus(loadedDrone.getId());
            }
        }
    }
}
