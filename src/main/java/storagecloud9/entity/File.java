package storagecloud9.entity;

import org.springframework.http.ContentDisposition;

import java.time.LocalDateTime;

public class File {
    private String filename;
    private long size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Конструкторы
    public File(String filename, long size, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.filename = filename;
        this.size = size;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ContentDisposition.Builder builder() {
    }

    // Геттеры
    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Сеттеры (если нужны)
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
