package tmrv.dev.blogsystem.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserBlockedException extends ResponseStatusException {
    public UserBlockedException() {
        super(HttpStatus.FORBIDDEN, "User is blocked and cannot create posts");
    }
}

