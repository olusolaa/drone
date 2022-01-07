package musala.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Drone{

//    public Drone(Long id, String serialNumber, Emodel model) {
//        this.id = id;
//        this.serialNumber = serialNumber;
//        this.model = model;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, updatable = false, length = 100)
    private String serialNumber;
    private Emodel model;
    private int weightLimit;
    private int currentWeight;
    private int batteryCapacity;
    private Estate state;
    private Long distance;
    @OneToOne
    private Destination destination;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "drone_medication",
            joinColumns = @JoinColumn(name = "drone_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id"))
    @ToString.Exclude
    private List<Medication> medications = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Drone drone = (Drone) o;
        return getSerialNumber() != null && Objects.equals(getSerialNumber(), drone.getSerialNumber());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
