package musala.drone.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
public class BaseModel {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
}
