package musala.service;

import musala.dto.ApiResponse;
import musala.model.Destination;
import musala.dto.RegisterDroneDto;
import musala.model.Drone;
import musala.model.Estate;
import musala.model.Medication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface DroneService {
    ApiResponse<?> registerDrone(RegisterDroneDto droneDto);
    ApiResponse<?> chargeDrone(Long droneId);
    ApiResponse<?> unregisterDrone(Long droneId);
    ApiResponse<?> loadDrone(Long droneId, List<Medication> medications, Destination destination);
    ApiResponse<?> unloadDrone(Long droneId);
    ApiResponse<?> removeMedication(Long droneId, List<Medication> medications);
    ApiResponse<?> checkDroneLoadedMedications(Long droneId);
    ApiResponse<?> checkDroneStatus(Long droneId);
    ApiResponse<?> checkDronesAvailableForLoading();
    ApiResponse<?> chekDroneBatteryStatus(Long droneId);
}
