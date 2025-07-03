package com.crewManager.pro.event.domain;

import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAttendee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch =  FetchType.LAZY) // 이벤트는 여러 참석자가 가능하다. / 참석자는 이벤트에 속해 있다.
    private Event event;

    @ManyToOne(fetch =  FetchType.LAZY) // 사용자는 여러 참석자가 될수 있다. / 참석자는 사용자에 속해있다.
    private User user;

}
