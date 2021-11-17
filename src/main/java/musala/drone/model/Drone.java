package musala.drone.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Drone {
    @Id
    @Column(name = "id", nullable = false, length = 100)
    private String serialNumber;

    private enum model{
        Lightweight,
        Middleweight,
        Cruiserweight,
        Heavyweight
    }
    private int weightLimit;
    private int batteryCapacity;

    private enum state{
        IDLE,
        LOADING,
        LOADED,
        DELIVERING,
        DELIVERED,
        RETURNING
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Drone drone = (Drone) o;
        return serialNumber != null && Objects.equals(serialNumber, drone.serialNumber);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
