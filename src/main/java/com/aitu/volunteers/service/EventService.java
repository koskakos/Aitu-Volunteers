package com.aitu.volunteers.service;

import com.aitu.volunteers.model.Event;
import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.request.EventRequest;
import com.aitu.volunteers.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserService userService;

    public Event createEvent(EventRequest eventRequest) {
        Event event = Event.builder()
                .title(eventRequest.getTitle())
                .description(eventRequest.getDescription())
                .leader(userService.getUserByBarcode(eventRequest.getLeaderBarcode()))
                .startDate(eventRequest.getStartDate())
                .endDate(eventRequest.getEndDate())
                .category(eventRequest.getCategory())
                .hasTransport(eventRequest.getHasTransport())
                .hasMeal(eventRequest.getHasMeal())
                .dresscode(eventRequest.getDresscode())
                .responsibilities(eventRequest.getResponsibilities())
                .build();
        return eventRepository.save(event);
    }

}
