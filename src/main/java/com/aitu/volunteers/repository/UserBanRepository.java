package com.aitu.volunteers.repository;


import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserBanRepository extends JpaRepository<UserBan, Long> {
    void deleteById(Long id);

    boolean existsByUserAndEndDateAfter(User user, LocalDateTime localDateTime);
}
