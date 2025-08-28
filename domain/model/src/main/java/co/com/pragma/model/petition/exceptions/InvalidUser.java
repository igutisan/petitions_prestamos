package co.com.pragma.model.petition.exceptions;

public class InvalidUser extends RuntimeException {
    public InvalidUser(String message) {
        super(message);
    }
}
