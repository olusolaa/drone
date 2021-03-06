package musala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import musala.model.Emodel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterDroneDto {

    @NotNull
    @Size(max = 100)
    private String serialNumber;

    @NotNull
    private Emodel model;

    @NotNull
    private int weightLimit;

    @NotNull
    private int batteryDrainRate;
}
