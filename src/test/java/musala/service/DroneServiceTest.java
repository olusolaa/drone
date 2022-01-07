package musala.service;

import musala.dto.ApiResponse;
import musala.dto.RegisterDroneDto;
import musala.model.Drone;
import musala.model.Emodel;
import musala.model.Estate;
import musala.model.Medication;
import musala.repository.DestinationRepository;
import musala.repository.DroneRepository;
import musala.repository.MedicationRepository;
import musala.service.impl.DroneServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

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
        given(droneRepository.save(any())).willReturn(drone);
        ApiResponse<Drone> apiResponse = droneService.registerDrone(droneDto);
        assertEquals(apiResponse.getMessage(), "Drone registration successful");
        assertThat(apiResponse.getData()).isEqualTo(drone);
        verify(droneRepository).save(drone);
    }
}