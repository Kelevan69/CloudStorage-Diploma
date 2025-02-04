package storagecloud9.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import storagecloud9.dto.FileResponse;
import storagecloud9.service.FileService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("filename") String filename,
                                             Authentication authentication) {
        String email = authentication.getName();
        fileService.uploadFile(filename, file, email);
        log.info("File uploaded: {}", filename);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename, Authentication authentication) {
        String email = authentication.getName();
        byte[] content = fileService.downloadFile(filename, email);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename, Authentication authentication) {
        String email = authentication.getName();
        fileService.deleteFile(filename, email);
        log.info("File deleted: {}", filename);
        return ResponseEntity.ok("File deleted successfully");
    }

    @GetMapping
    public List<FileResponse> listFiles(@RequestParam int limit, Authentication authentication) {
        String email = authentication.getName();
        return fileService.listFiles(email, limit);
    }
}
