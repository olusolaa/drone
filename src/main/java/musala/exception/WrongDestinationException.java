package musala.exception;

public class WrongDestinationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public WrongDestinationException(String message) {
        super(message);
    }
}
