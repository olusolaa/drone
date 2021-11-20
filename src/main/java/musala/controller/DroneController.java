package musala.controller;

import musala.dto.MedicationDto;
import musala.dto.RegisterDroneDto;
import musala.model.Drone;
import musala.model.Medication;
import musala.service.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController()
public class DroneController {
    DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDroneDto registerDroneDto) {
        return new ResponseEntity<>(droneService.registerDrone(registerDroneDto), HttpStatus.OK);
    }

    @PutMapping("/charging")
    public ResponseEntity<?> charging(Long droneId) {
        droneService.chargeDrone(droneId);
        return new ResponseEntity<Drone>(HttpStatus.OK);
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregister(Long droneId) {
        droneService.unregisterDrone(droneId);
        return new ResponseEntity<Drone>(HttpStatus.OK);
    }

    @PostMapping("load_drone/{id}")
    public ResponseEntity<?> loadDrone(@PathVariable Long id, @RequestBody MedicationDto medicationDto) {
        return new ResponseEntity<>(droneService.loadDrone(id, medicationDto.getMedications(),
                medicationDto.getDestination()), HttpStatus.OK);
    }

    @GetMapping("/unload_drone")
    public ResponseEntity<?> unloadDrone(Long droneId, @RequestBody List<Medication>medications) {
        droneService.unloadDrone(droneId, medications);
        return new ResponseEntity<Drone>(HttpStatus.OK);
    }

    @GetMapping("/check_Drone_Loaded_Medications/{droneId}")
    public ResponseEntity<?> checkDroneLoadedMedications(@PathVariable Long droneId) {
        List<Medication> medications = droneService.checkDroneLoadedMedications(droneId);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @GetMapping ("check_Drones_Available_For_Loading")
    public ResponseEntity<?> checkDronesAvailableForLoading() {
        Set<Drone> drones = droneService.checkDronesAvailableForLoading();
        return new ResponseEntity<>(drones, HttpStatus.OK);
    }
}
