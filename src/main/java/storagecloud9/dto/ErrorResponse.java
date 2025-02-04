package storagecloud9.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String message,
        int status,
        LocalDateTime timestamp,
        Map<String, Object> details
) {
    public ErrorResponse(String message, int status) {
        this(message, status, LocalDateTime.now(), null);
    }

    public ErrorResponse(String message, int status, Map<String, Object> details) {
        this(message, status, LocalDateTime.now(), details);
    }

    public ErrorResponse(String message, int status, LocalDateTime timestamp) {
        this(message, status, timestamp, null);
    }

    // Дополнительный конструктор с проверкой на null
    public ErrorResponse {
        if (message == null || timestamp == null) {
            throw new IllegalArgumentException("Message and timestamp cannot be null");
        }
    }
}
