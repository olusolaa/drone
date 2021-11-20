package musala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import musala.model.Drone;
import musala.model.Estate;

@Data
@AllArgsConstructor
public class ApiResponse {
    private Object status;
    private String message;
    private Object data;
}
