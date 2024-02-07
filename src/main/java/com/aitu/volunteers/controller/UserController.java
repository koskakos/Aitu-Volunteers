package com.aitu.volunteers.controller;

import com.aitu.volunteers.model.request.UserBanRequest;
import com.aitu.volunteers.service.EventService;
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
    private final EventService eventService;

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

    @PostMapping("/ban/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> banUser(@PathVariable Long id, @RequestBody UserBanRequest userBanRequest) {
        return ResponseEntity.ok(userService.banUser(userService.getUserById(id), userBanRequest));
    }

    @DeleteMapping("/ban/{banId}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> deleteBanUser(@PathVariable Long banId) {
        userService.deleteUserBan(banId);
        return ResponseEntity.ok("");
    }

    @GetMapping("/ban/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> allUserBan(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAllUserBan(userService.getUserById(id)));
    }

    @GetMapping("/{userId}/has-active-ban")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> isUserHasActiveBan(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.hasActiveBan(userService.getUserById(userId)));
    }

    @PostMapping("/{id}/cancel-all-registrations")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> cancelAllRegistrations(@PathVariable Long id) {
        eventService.cancelAllRegistrationsForUser(userService.getUserById(id));
        return ResponseEntity.ok("all registrations are canceled");
    }
}
