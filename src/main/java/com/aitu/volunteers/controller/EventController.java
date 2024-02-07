package com.aitu.volunteers.controller;

import com.aitu.volunteers.model.EventDay;
import com.aitu.volunteers.model.request.EventRequest;
import com.aitu.volunteers.service.EventService;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<?> saveEvent(@RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(eventService.createEvent(eventRequest));
    }

    @PostMapping("/register/{eventDayId}")
    public ResponseEntity<?> registerForEventDay(@PathVariable Long eventDayId) {
        return ResponseEntity.ok(eventService.registerUserToEventDay(userService.getAuthorizedUser(), eventService.getEventDayById(eventDayId)));
    }

    @DeleteMapping("/register/{eventDayId}")
    public ResponseEntity<?> deleteRegistrationForEventDay(@PathVariable Long eventDayId) {
        eventService.unregisterUserToEventDay(userService.getAuthorizedUser(), eventService.getEventDayById(eventDayId));
        return ResponseEntity.ok("unregistered");
    }

    @PostMapping("/activate-day/{eventDayId}")
    public ResponseEntity<?> setEventDayActivation(@PathVariable Long eventDayId, @RequestParam("isActive") Boolean isActive) {
        return ResponseEntity.ok(eventService.setEventDayActivation(userService.getAuthorizedUser(),
                eventService.getEventDayById(eventDayId), isActive));
    }

    @GetMapping("/get-start-qr/{eventDayId}")
    public ResponseEntity<?> getStartQr(@PathVariable Long eventDayId) {
        EventDay eventDay = eventService.getEventDayById(eventDayId);
        if(!eventDay.isActive()) return ResponseEntity.status(404).body("Event is not active");
        return ResponseEntity.ok(eventService.getStartQrByUserAndEventDay(userService.getAuthorizedUser(), eventDay));
    }

    @GetMapping("/get-end-qr/{eventDayId}")
    public ResponseEntity<?> getEndQr(@PathVariable Long eventDayId) {
        EventDay eventDay = eventService.getEventDayById(eventDayId);
        if(!eventDay.isActive()) return ResponseEntity.status(404).body("Event is not active");
        return ResponseEntity.ok(eventService.getEndQrByUserAndEventDay(userService.getAuthorizedUser(), eventDay));
    }
}
