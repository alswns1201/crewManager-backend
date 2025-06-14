package com.crewManager.pro.crew.repository;

import com.crewManager.pro.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew,String> {

    Optional<Crew> findByName(String name);
}
