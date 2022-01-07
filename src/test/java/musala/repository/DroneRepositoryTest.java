package musala.repository;

import musala.model.Drone;
import musala.model.Emodel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DroneRepositoryTest {


    private final DroneRepository droneRepository;

    @Autowired
    DroneRepositoryTest(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Test
    void findBySerialNumber() {
        String serialNumber = "1234";
        Drone drone = new Drone(1L, serialNumber, Emodel.Cruiserweight);
        droneRepository.save(drone);
        Optional <Drone> drone2 = droneRepository.findBySerialNumber(serialNumber);
        assert drone2.get().getSerialNumber().equals(serialNumber);
        assertThat(drone2.get()).isEqualTo(drone);
    }
}