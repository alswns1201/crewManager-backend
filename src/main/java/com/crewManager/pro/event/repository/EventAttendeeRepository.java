package com.crewManager.pro.event.repository;

import com.crewManager.pro.event.domain.Event;
import com.crewManager.pro.event.domain.EventAttendee;
import com.crewManager.pro.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee,String> {

}
