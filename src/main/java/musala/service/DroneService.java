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
    public ApiResponse registerDrone(RegisterDroneDto droneDto);
    void chargeDrone(Long droneId);
    void unregisterDrone(Long droneId);
    ApiResponse loadDrone(Long droneId, List<Medication> medications, Destination destination);
    void unloadDrone(Long droneId, List<Medication> medications);
    List<Medication> checkDroneLoadedMedications(Long droneId);
    Estate checkDroneStatus(Long droneId);
    Set<Drone> checkDronesAvailableForLoading();
    int chekDroneBatteryStatus(Long droneId);
}
