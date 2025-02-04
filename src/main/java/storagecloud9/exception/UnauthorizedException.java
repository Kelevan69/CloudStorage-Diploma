package storagecloud9.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private final String resource;
    private final String action;

    public UnauthorizedException(String message) {
        super(message);
        this.resource = null;
        this.action = null;
    }

    public UnauthorizedException(String resource, String action) {
        super(String.format("Unauthorized access to %s: %s", resource, action));
        this.resource = resource;
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }
}