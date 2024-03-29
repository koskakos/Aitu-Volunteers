package com.aitu.volunteers.controller;

import com.aitu.volunteers.service.ImageService;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;

    @GetMapping("/files/{userSub}/{filename:.+}")
    public ResponseEntity<Resource> servePostImage(@PathVariable String filename, @PathVariable String userSub) {
        Resource file = imageService.loadPostImageAsResource(userSub, filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/api/v1/post/upload-image")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> uploadPostImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageService.storePostImage(file, userService.getAuthorizedUser()));
    }
}
