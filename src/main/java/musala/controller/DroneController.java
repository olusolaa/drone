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

    @PutMapping("/charge_drone/{droneId}")
    public ResponseEntity<?> charging(@PathVariable Long droneId) {
        return new ResponseEntity<>(droneService.chargeDrone(droneId), HttpStatus.OK);
    }

    @DeleteMapping("/unregister/{droneId}")
    public ResponseEntity<?> unregister(@PathVariable Long droneId) {
        return new ResponseEntity<>(droneService.unregisterDrone(droneId), HttpStatus.OK);
    }

    @PostMapping("load_drone/{droneId}")
    public ResponseEntity<?> loadDrone(@PathVariable Long droneId, @RequestBody MedicationDto medicationDto) {
        return new ResponseEntity<>(droneService.loadDrone(droneId, medicationDto.getMedications(),
                medicationDto.getDestination()), HttpStatus.OK);
    }

    @GetMapping("/unload_drone/{droneId}")
    public ResponseEntity<?> unloadDrone(@PathVariable Long droneId) {
        return new ResponseEntity<>(droneService.unloadDrone(droneId), HttpStatus.OK);
    }

    @GetMapping("/check_Drone_Loaded_Medications/{droneId}")
    public ResponseEntity<?> checkDroneLoadedMedications(@PathVariable Long droneId) {
        return new ResponseEntity<>(droneService.checkDroneLoadedMedications(droneId), HttpStatus.OK);
    }

    @GetMapping ("check_Drones_Available_For_Loading")
    public ResponseEntity<?> checkDronesAvailableForLoading() {
        return new ResponseEntity<>(droneService.checkDronesAvailableForLoading(), HttpStatus.OK);
    }

    @PatchMapping("/remove_medications/{droneId}")
    public ResponseEntity<?> removeMedications(@PathVariable Long droneId, @RequestBody List<Medication> medications) {
        System.err.println(medications);
        return new ResponseEntity<>(droneService.removeMedications(droneId, medications), HttpStatus.OK);
    }

    @GetMapping ("/check_drone_status/{droneId}")
    public ResponseEntity<?> checkDroneStatus(@PathVariable Long droneId) {
        return new ResponseEntity<>(droneService.checkDroneStatus(droneId), HttpStatus.OK);
    }

    @GetMapping ("/check_battery_status/{droneId}")
    public ResponseEntity<?> checkBatteryStatus(@PathVariable Long droneId) {
        return new ResponseEntity<>(droneService.chekDroneBatteryStatus(droneId), HttpStatus.OK);
    }

    @GetMapping("/auto-pilot")
    public ResponseEntity<?> autoPilot() {
        return new ResponseEntity<>(droneService.autoPilot(), HttpStatus.OK);
    }
}
