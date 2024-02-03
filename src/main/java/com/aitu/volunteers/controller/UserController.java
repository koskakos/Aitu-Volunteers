package com.aitu.volunteers.controller;

import com.aitu.volunteers.service.StorageService;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final StorageService storageService;

    @PostMapping("/certificate")
    public ResponseEntity<?> uploadCertificate(@RequestParam("certificate") MultipartFile certificate) {
        return ResponseEntity.ok(userService.uploadCertificate(userService.getAuthorizedUser(), certificate));
    }

    @DeleteMapping("/certificate")
    public ResponseEntity<?> deleteCertificate() {
        return ResponseEntity.ok(userService.deleteCertificate(userService.getAuthorizedUser()));
    }

    @GetMapping("/certificate")
    public ResponseEntity<Resource> getAuthorizedUserCertificate() {
        Resource file = storageService.loadUserCertificateAsResource(userService.getAuthorizedUser());

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/certificate/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Resource> getUserCertificate(@PathVariable Long id) {
        Resource file = storageService.loadUserCertificateAsResource(userService.getUserById(id));

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PutMapping("/certificate/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> approveCertificate(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleApproveCertificate(userService.getUserById(id)));
    }
}
