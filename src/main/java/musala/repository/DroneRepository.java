package musala.repository;

import musala.model.Drone;
import musala.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional <Drone> findBySerialNumber(String serialNumber);
    Set<Drone> findAllByCurrentWeightEquals(int weight);
}
