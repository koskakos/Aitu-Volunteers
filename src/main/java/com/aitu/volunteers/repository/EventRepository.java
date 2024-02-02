package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
