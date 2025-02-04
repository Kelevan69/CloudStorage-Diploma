package storagecloud9.exception;

public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException(long maxSize) {
        super("File size exceeds limit: " + maxSize + " bytes");
    }
}