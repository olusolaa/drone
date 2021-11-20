package musala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import musala.model.Drone;
import musala.model.Estate;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
}
