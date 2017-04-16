package Exceptions;

/**
 * Created by Mohammed on 4/16/2017.
 */


public class ValueOutOfBoundException extends RuntimeException {
    public ValueOutOfBoundException() {
        super("Value needs to be between 0 and 1");
    }

}
