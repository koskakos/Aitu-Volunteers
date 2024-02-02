package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findTeamById(Long id);
}
