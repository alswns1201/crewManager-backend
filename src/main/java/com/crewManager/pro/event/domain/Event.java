package com.crewManager.pro.event.domain;

import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    private String title;

//    private String location;
    @Embedded // id 값이 없는 객체 포함
    private Location location;

    private String description;

    private LocalDateTime eventDateTime;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @ManyToOne(fetch = FetchType.LAZY) // 소유자는 여러 이벤트에 있을수 있다.  이벤트는 소유자에 속해있다(반대로 말하면 사용자는 여러 이벤트를 만들수 있다)
    private User owner;




}
