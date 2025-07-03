package com.crewManager.pro.event.service;

import com.crewManager.pro.event.domain.Event;
import com.crewManager.pro.event.domain.EventAttendee;
import com.crewManager.pro.event.dto.CreateEventRequest;
import com.crewManager.pro.event.repository.EventAttendeeRepository;
import com.crewManager.pro.event.repository.EventRepository;
import com.crewManager.pro.exception.BusinessException;
import com.crewManager.pro.exception.ErrorCode;
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.repository.UserRepository;
import com.crewManager.pro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.SplitPaneUI;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventAttendeeRepository eventAttendeeRepository;


    @Transactional
    public String createEvent(CreateEventRequest createEventRequest){
        //1.현재 user 조회
        User user = userService.findUserByEmail(createEventRequest.getUserEmail());

        // 날짜는 존재해야함.
        if(createEventRequest.getEventDateTime() == null || createEventRequest.getEventDateTime().isBefore(LocalDateTime.now())){
            throw  new BusinessException(ErrorCode.INVALID_EVENT_DATE);
        }
        // 타이틀과 위치는 존재해야함.
        if(createEventRequest.getTitle() == null || createEventRequest.getLocation() == null ||
        createEventRequest.getTitle().isBlank() || createEventRequest.getLocation() == null){
            throw  new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        boolean alreadyExists = eventRepository.existsByOwnerAndEventDateTime(user,createEventRequest.getEventDateTime());
        // 이미 존재하는지 여부 판단.
        if(alreadyExists){
            throw new BusinessException(ErrorCode.CREW_USER_DUPLICATED);
        }
        //2. 이벤트 저장.
        Event event = Event.builder()
                .title(createEventRequest.getTitle())
                .description(createEventRequest.getDescription())
                .owner(user)
                .eventDateTime(createEventRequest.getEventDateTime())
                .location(createEventRequest.getLocation())
                .eventType(createEventRequest.getEventType())
                .build();
        Event saved =  eventRepository.save(event);



        EventAttendee eventAttendee = EventAttendee.builder()
                .event(event)
                .user(user)
                .build();
        eventAttendeeRepository.save(eventAttendee);


        return saved.getOwner().getName();
    }


}
