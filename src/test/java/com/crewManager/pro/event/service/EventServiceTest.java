package com.crewManager.pro.event.service;


import com.crewManager.pro.event.domain.Event;
import com.crewManager.pro.event.domain.EventAttendee;
import com.crewManager.pro.event.domain.EventType;
import com.crewManager.pro.event.domain.Location;
import com.crewManager.pro.event.dto.CreateEventRequest;
import com.crewManager.pro.event.repository.EventAttendeeRepository;
import com.crewManager.pro.event.repository.EventRepository;
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

// 단위 테스트
@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;
    @Mock private UserService userService;
    @Mock private EventRepository eventRepository;
    @Mock private EventAttendeeRepository eventAttendeeRepository;

    @Test
    void 이벤트_등록(){
        //given
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setEventType(EventType.PERSONAL);
        createEventRequest.setEventDateTime(LocalDateTime.now().plusDays(1)); // 내일 일정에 대해 test
        createEventRequest.setTitle("내일 일정 테스트");
        createEventRequest.setDescription("user가 일정을 등록한다.");
        createEventRequest.setUserEmail("mjkim1201@naver.com");
        Location location = new Location(
                "스타벅스 강남점",
                "서울특별시 강남구 테헤란로 123",
                37.497942,
                127.027636
        );
        createEventRequest.setLocation(location);

        //가짜 유저 가정
        User user = User.builder()
                .id("12314324234")
                .email("mjkim1201@naver.com")
                .name("김러너")
                .build();

        Event event = Event.builder()
                .id("123123324324234")
                .title(createEventRequest.getTitle())
                .eventType(createEventRequest.getEventType())
                .eventDateTime(createEventRequest.getEventDateTime())
                .owner(user)
                .location(createEventRequest.getLocation())
                .description(createEventRequest.getDescription())
                .build();

        // mock 지정
        Mockito.when(userService.findUserByEmail(createEventRequest.getUserEmail())).thenReturn(user);
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(event);
        Mockito.when(eventAttendeeRepository.existsByEventAndUser(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(eventAttendeeRepository.save(Mockito.any(EventAttendee.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        String result = eventService.createEvent(createEventRequest);

        // then
        assertThat(result).isEqualTo("김러너");


    }


}
