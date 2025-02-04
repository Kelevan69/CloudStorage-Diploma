package storagecloud9.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import storagecloud9.dto.FileResponse;
import storagecloud9.entity.File;
import storagecloud9.entity.User;
import storagecloud9.exception.*;
import storagecloud9.repository.FileRepository;
import storagecloud9.repository.UserRepository;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    @Transactional
    public void uploadFile(String filename, MultipartFile file, String email) {
        User user = getUserByEmail(email);
        validateFile(file);

        if (fileRepository.existsByFilenameAndUser(filename, user)) {
            throw new FileAlreadyExistsException(filename);
        }

        try {
            File newFile = File.builder()
                    .filename(filename)
                    .content(file.getBytes())
                    .size(file.getSize())
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();

            fileRepository.save(newFile);
            log.info("User {} uploaded file: {}", email, filename);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Transactional
    public byte[] downloadFile(String filename, String email) {
        User user = getUserByEmail(email);

        return fileRepository.findByFilenameAndUser(filename, user)
                .map(File::getContent)
                .orElseThrow(() -> new FileNotFoundException(filename));
    }

    @Transactional
    public void deleteFile(String filename, String email) {
        User user = getUserByEmail(email);

        File file = fileRepository.findByFilenameAndUser(filename, user)
                .orElseThrow(() -> new FileNotFoundException(filename));

        fileRepository.delete(file);
        log.info("User {} deleted file: {}", email, filename);
    }

    public List<FileResponse> listFiles(String email, int limit) {
        User user = getUserByEmail(email);

        return fileRepository.findTopNByUser(user.getId(), limit)
                .stream()
                .map(file -> new FileResponse(
                        file.getFilename(),
                        file.getSize(),
                        file.getCreatedAt(),
                        file.getUpdatedAt()
                ))
                .toList();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException(MAX_FILE_SIZE);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
