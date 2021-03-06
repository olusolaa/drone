package musala.service;

import musala.dto.ApiResponse;
import musala.model.Destination;
import musala.dto.RegisterDroneDto;
import musala.model.Drone;
import musala.model.Medication;
import musala.service.impl.DroneServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface DroneService {
    ApiResponse<Drone> registerDrone(RegisterDroneDto droneDto);
    ApiResponse<?> chargeDrone(Long droneId);
    ApiResponse<?> unregisterDrone(Long droneId);
    ApiResponse<?> loadDrone(Long droneId, List<Medication> medications, Destination destination);
    ApiResponse<?> unloadDrone(Long droneId);
    ApiResponse<?> removeMedications(Long droneId, List<Medication> medications);
    ApiResponse<?> checkDroneLoadedMedications(Long droneId);
    ApiResponse<?> checkDroneStatus(Long droneId);
    ApiResponse<?> checkDronesAvailableForLoading();
    ApiResponse<?> chekDroneBatteryStatus(Long droneId);
    ApiResponse<?> autoPilot();
    boolean isAutoPilot();
}
