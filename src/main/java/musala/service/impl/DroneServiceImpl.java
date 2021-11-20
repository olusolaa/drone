package musala.service.impl;

import lombok.extern.slf4j.Slf4j;
import musala.dto.ApiResponse;
import musala.model.Destination;
import musala.dto.RegisterDroneDto;
import musala.model.Drone;
import musala.model.Estate;
import musala.model.Medication;
import musala.repository.DestinationRepository;
import musala.repository.DroneRepository;
import musala.repository.MedicationRepository;
import musala.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DestinationRepository destinationRepository;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository, DestinationRepository destinationRepository1) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.destinationRepository = destinationRepository1;
    }

    @Override
    public ApiResponse registerDrone(RegisterDroneDto droneDto) {
        Drone drone = new Drone();
        drone.setSerialNumber(droneDto.getSerialNumber());
        drone.setModel(droneDto.getModel());
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(droneDto.getWeightLimit());
        drone.setBatteryDischargePerDistance(droneDto.getBatteryDrainRate());
        drone.setState(Estate.IDLE);
        return new ApiResponse(Estate.IDLE, "Drone registration succesfull", droneRepository.save(drone));
    }

    public void chargeDrone(Long droneId) {
        droneRepository.findById(droneId).ifPresent(drone -> {
            drone.setBatteryCapacity(100);
            droneRepository.save(drone);
        });
    }

    @Override
    public void unregisterDrone(Long droneId) {
        droneRepository.deleteById(droneId);
    }

    @Override
    public ApiResponse loadDrone(Long droneId, List<Medication> medications, Destination destination) {
        Optional<Drone> droneDb = droneRepository.findById(droneId);
            if (droneDb.isEmpty()) {
                throw new RuntimeException("Drone is not available");
            }
            Drone drone = droneDb.get();
            if (drone.getDestination() != null && !Objects.equals(drone.getDestination().getCity(), destination.getCity())){
                throw new RuntimeException("Drone " + drone.getSerialNumber() + " is already loaded with medication heading towards " + drone.getDestination().getCity());
            }
            if (drone.getBatteryCapacity() < 25) {
                throw new RuntimeException("Battery too low");
            }
            if (drone.getBatteryCapacity() <  2 * destination.getDistance() * drone.getBatteryDischargePerDistance()) {
                throw new RuntimeException("Destination distance too far");
            }
            log.info("Drone {} loading medications", drone.getSerialNumber());

        Destination finalDestination = destination;
        destination = destinationRepository.findByCity(destination.getCity()).orElseGet(() -> destinationRepository.save(finalDestination));
            drone.setDestination(destination);
            drone.setState(Estate.LOADING);
            medications.forEach(med -> {
                Medication finalMed = med;
                if (drone.getWeightLimit() < drone.getCurrentWeight() + med.getWeight()) {
                    throw new RuntimeException("Drone weight limit exceeded");
                }
                med = medicationRepository.findByCode(med.getCode()).orElseGet(() -> medicationRepository.save(finalMed));
                drone.getMedications().add(med);
                drone.setCurrentWeight(drone.getCurrentWeight() + med.getWeight());
                log.info("Drone {} loaded medications {} successfully", drone.getSerialNumber(), med.getName());
            });
            log.info("Drone {} loading completed", drone.getSerialNumber());
            drone.setState(Estate.LOADED);
            droneRepository.save(drone);
        return new ApiResponse(Estate.LOADED, "Drone {} " + drone.getSerialNumber() + " loaded successfully", drone.getMedications());
    }

    @Override
    public void unloadDrone(Long droneId, List<Medication> medications) {
        droneRepository.findById(droneId).ifPresent(drone -> {
            if (drone.getDestination().getDistance() != 0) {
                throw new RuntimeException("Drone is not at destination");
            }
            log.info("Drone {} arrived at destination, unloading medications", drone.getSerialNumber());
            drone.setState(Estate.DELIVERING);
            drone.getMedications().removeAll(medications);
            drone.setCurrentWeight(drone.getCurrentWeight() - medications.stream().mapToInt(Medication::getWeight).sum());
            if (drone.getCurrentWeight() == 0) {
                log.info("Drone {} unloading completed", drone.getSerialNumber());
                drone.setState(Estate.DELIVERED);
            }
            droneRepository.save(drone);
        });
    }


    @Override
    public List<Medication> checkDroneLoadedMedications(Long droneId) {
        return droneRepository.findById(droneId).map(Drone::getMedications).orElse(null);
    }

    @Override
    public Estate checkDroneStatus(Long droneId) {
        return droneRepository.findById(droneId).map(Drone::getState).orElse(null);
    }

    public Set<Drone> checkDronesAvailableForLoading() {
        return droneRepository.findAllByStateEquals(Estate.IDLE);
    }

    @Override
    public int chekDroneBatteryStatus(Long droneId) {
       Drone drone = droneRepository.getById(droneId);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - (drone.getDestination().getDistance() - 1) * drone.getBatteryDischargePerDistance());
            log.info("Drone {} battery status: {}", drone.getSerialNumber(), drone.getBatteryCapacity());
           return droneRepository.save(drone).getBatteryCapacity();
    }
}