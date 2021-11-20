package musala.exception;

public class MedicationNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public MedicationNotFoundException(String message) {
        super(message);
    }
}
