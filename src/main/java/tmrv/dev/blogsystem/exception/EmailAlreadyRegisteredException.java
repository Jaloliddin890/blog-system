package tmrv.dev.blogsystem.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("Email " + email + " is already registered.");
    }
}

