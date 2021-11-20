package musala.service.impl;

import lombok.extern.slf4j.Slf4j;
import musala.dto.ApiResponse;
import musala.exception.DroneNotFoundException;
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
    public ApiResponse<?> registerDrone(RegisterDroneDto droneDto) {
        droneRepository.findBySerialNumber(droneDto.getSerialNumber()).ifPresent(drone1 -> {
            throw new RuntimeException("Drone already registered");
        });
        Drone drone = new Drone();
        drone.setSerialNumber(droneDto.getSerialNumber());
        drone.setModel(droneDto.getModel());
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(droneDto.getWeightLimit());
        drone.setBatteryDischargePerDistance(droneDto.getBatteryDrainRate());
        drone.setState(Estate.IDLE);
        return new ApiResponse<>("Drone registration successful", droneRepository.save(drone));
    }

    public ApiResponse<?> chargeDrone(Long droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow( () -> new DroneNotFoundException("Drone not found"));
            drone.setBatteryCapacity(100);
            droneRepository.save(drone);
        return new ApiResponse<>("Drone " + drone.getSerialNumber() + " charged successfully", drone.getBatteryCapacity());
    }

    @Override
    public ApiResponse<?> unregisterDrone(Long droneId) {
        droneRepository.deleteById(droneId);
        return new ApiResponse<>("Drone unregistered successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<?> loadDrone(Long droneId, List<Medication> medications, Destination destination) {
        Drone drone = droneRepository.findById(droneId).orElseThrow( () -> new RuntimeException("Drone not found"));;
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
        return new ApiResponse<>("Drone " + drone.getSerialNumber() + " loaded successfully", drone);
    }

    @Override
    public ApiResponse<?> unloadDrone(Long droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow( () -> new RuntimeException("Drone) not found"));
            if (drone.getDestination().getDistance() != 0) {
                throw new RuntimeException("Drone is not at destination");
            }
            log.info("Drone {} arrived at destination, unloading medications", drone.getSerialNumber());
            drone.setState(Estate.DELIVERING);
            drone.setMedications(new ArrayList<>());
            drone.setCurrentWeight(0);
            log.info("Drone {} unloading completed", drone.getSerialNumber());
            drone.setState(Estate.DELIVERED);
            droneRepository.save(drone);
            return new ApiResponse<>("Drone " + drone.getSerialNumber() + " unloaded successfully", drone.getMedications());
    }

    @Override
    public ApiResponse<?> removeMedication(Long droneId, List<Medication> medications) {
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new RuntimeException("Drone not found"));
            medications.forEach(med -> {
                        medicationRepository.findByCode(med.getCode()).ifPresent(medication -> {
                            drone.getMedications().remove(medication);
                            drone.setCurrentWeight(drone.getCurrentWeight() - medication.getWeight());
                            log.info("{} removed from drone {}", medication.getName(), drone.getSerialNumber());
                        });
                    });
            droneRepository.save(drone);
        return new ApiResponse<>( "Medications removed successfully", drone);
    }


    @Override
    public ApiResponse<?> checkDroneLoadedMedications(Long droneId) {
        List<Medication> medication = droneRepository.findById(droneId).orElseThrow( () -> new DroneNotFoundException("Drone not found")).getMedications();
        return new ApiResponse<>("Medications retrieved successfully", medication);
    }

    @Override
    public ApiResponse<?> checkDroneStatus(Long droneId) {
         Estate state = droneRepository.findById(droneId).orElseThrow( () -> new DroneNotFoundException("Drone not found")).getState();
         return new ApiResponse<>("Drone status retrieved successfully", state);
    }

    public ApiResponse<?> checkDronesAvailableForLoading() {
        return new ApiResponse<>("Drones available for loading", droneRepository.findAllByStateEquals((Estate.IDLE)));
    }

    @Override
    public ApiResponse<?> chekDroneBatteryStatus(Long droneId) {
       Drone drone = droneRepository.findById(droneId).orElseThrow( () -> new DroneNotFoundException("Drone not found"));
            drone.setBatteryCapacity(drone.getBatteryCapacity() - (drone.getDestination().getDistance() - 1) * drone.getBatteryDischargePerDistance());
            log.info("Drone {} battery status: {}", drone.getSerialNumber(), drone.getBatteryCapacity());
          return new ApiResponse<>("Drone battery status retrieved successfully", drone.getBatteryCapacity());
    }
}