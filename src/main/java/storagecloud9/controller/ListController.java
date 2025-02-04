package storagecloud9.controller;

import storagecloud9.dto.FileResponse;
import storagecloud9.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cloud/list")
@RequiredArgsConstructor
public class ListController {
    private final FileService fileService;

    @GetMapping
    public List<FileResponse> listFiles(
            @RequestParam(defaultValue = "10") int limit,
            Authentication auth
    ) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }

        return fileService.listFiles(auth.getName(), limit);
    }
}
