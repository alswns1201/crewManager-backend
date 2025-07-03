package com.crewManager.pro.event.repository;

import com.crewManager.pro.event.domain.Event;
import com.crewManager.pro.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event,String> {

    boolean existsByOwnerAndEventDateTime(User user, LocalDateTime eventDateTime);
}
