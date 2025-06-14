package com.crewManager.pro.crew.repository;

import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import com.crewManager.pro.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CrewMemberRepository extends JpaRepository<CrewMember,String> {

    Boolean existsByUserAndCrew(User user , Crew crew);

}
