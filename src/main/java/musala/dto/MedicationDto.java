package musala.dto;

import lombok.Data;
import musala.model.Destination;
import musala.model.Medication;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MedicationDto {

    @NotNull
    List<Medication> medications;
    @NotNull
    Destination destination;
}
