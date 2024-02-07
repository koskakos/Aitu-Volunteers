package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.EventDay;
import com.aitu.volunteers.model.EventRegistration;
import com.aitu.volunteers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    boolean existsByUserAndEventDay(User user, EventDay eventDay);

    void deleteByUserAndEventDay(User user, EventDay eventDay);
}
