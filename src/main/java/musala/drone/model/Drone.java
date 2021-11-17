package musala.drone.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Drone extends BaseModel{

    @Size(max = 100)
    private UUID serialNumber;
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
        return getId() != null && Objects.equals(getId(), drone.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
