package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.EventDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventDayRepository extends JpaRepository<EventDay, Long> {
    Optional<EventDay> findEventDayById(Long id);
}
