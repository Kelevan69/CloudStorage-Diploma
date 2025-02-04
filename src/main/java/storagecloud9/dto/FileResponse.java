package storagecloud9.dto;

import storagecloud9.entity.File;

import java.time.LocalDateTime;

public record FileResponse(
        String filename,
        long size,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static FileResponse fromEntity(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        return new FileResponse(
                file.getFilename(),
                file.getSize(),
                file.getCreatedAt(),
                file.getUpdatedAt()
        );
    }
}
