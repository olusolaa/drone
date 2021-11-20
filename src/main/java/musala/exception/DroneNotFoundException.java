package musala.exception;

public class DroneNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DroneNotFoundException(String message) {
        super(message);
    }
}
