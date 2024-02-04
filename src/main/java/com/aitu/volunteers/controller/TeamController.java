package com.aitu.volunteers.controller;

import com.aitu.volunteers.model.Team;
import com.aitu.volunteers.model.request.CreateTeamRequest;
import com.aitu.volunteers.service.TeamService;
import com.aitu.volunteers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> teamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping()
    public Team createTeam(@RequestBody CreateTeamRequest teamRequest) {
        return teamService.createTeam(teamRequest);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addMember(@RequestParam Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(userService.addTeamToUser(userService.getUserById(userId), teamService.getTeamById(id)));
    }
}
