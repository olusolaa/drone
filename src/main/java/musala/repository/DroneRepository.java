package musala.repository;

import musala.model.Drone;
import musala.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional <Drone> findBySerialNumber(String serialNumber);
    List<Drone> findAllByCurrentWeightEquals(int weight);
    List<Drone> findByStateEquals(Estate state);
}
