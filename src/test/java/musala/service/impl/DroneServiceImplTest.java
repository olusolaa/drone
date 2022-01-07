package musala.service.impl;

import musala.dto.ApiResponse;
import musala.dto.RegisterDroneDto;
import musala.model.Drone;
import musala.model.Emodel;
import musala.model.Estate;
import musala.repository.DestinationRepository;
import musala.repository.DroneRepository;
import musala.repository.MedicationRepository;
import musala.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DroneServiceImplTest {

    @Mock
    private DroneRepository droneRepository;
    @Mock
    private MedicationRepository medicalRepository;
    @Mock
    private DestinationRepository destinationRepository;


    private DroneServiceImpl droneService;

    @BeforeEach
    void setUp() {
        droneService = new DroneServiceImpl(droneRepository, medicalRepository, destinationRepository);  }

    @Test
    void registerDrone() {
        RegisterDroneDto droneDto = new RegisterDroneDto("1234", Emodel.Heavyweight, 3, 5);
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setSerialNumber(droneDto.getSerialNumber());
        drone.setModel(droneDto.getModel());
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(droneDto.getWeightLimit());
        drone.setState(Estate.IDLE);
        given(droneRepository.findBySerialNumber(droneDto.getSerialNumber())).willReturn(Optional.empty());
        ApiResponse<?> apiResponse = droneService.registerDrone(droneDto);
        assertEquals(apiResponse.getMessage(), "Drone registration successful");
        System.out.println(apiResponse);
        given(droneRepository.save(any())).willReturn(drone);
        //assertThat(apiResponse.getData()).isEqualTo(drone);
        verify(droneRepository).save(drone);
    }

    @Test
    void chargeDrone() {
    }

    @Test
    void unregisterDrone() {
    }

    @Test
    void loadDrone() {
    }
}