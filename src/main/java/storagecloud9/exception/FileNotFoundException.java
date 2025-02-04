package storagecloud9.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {
    private final String filename;
    private final String username;

    public FileNotFoundException(String filename, String username) {
        super(String.format("File '%s' not found for user '%s'", filename, username));
        this.filename = filename;
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public String getUsername() {
        return username;
    }
}