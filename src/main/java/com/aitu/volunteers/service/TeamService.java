package com.aitu.volunteers.service;

import com.aitu.volunteers.model.Team;
import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.request.CreateTeamRequest;
import com.aitu.volunteers.repository.TeamRepository;
import com.aitu.volunteers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    public Team getTeamById(Long id) {
        return teamRepository.findTeamById(id).
                orElseThrow(() -> new NoSuchElementException());
    }

    public Team createTeam(CreateTeamRequest teamRequest) {
        Team team = Team.builder()
                .name(teamRequest.getName())
                .description(teamRequest.getDescription())
                .telegramLink(teamRequest.getTelegramLink())
                .leader(userService.getUserById(teamRequest.getLeaderId()))
                .build();
        return teamRepository.save(team);
    }
}
