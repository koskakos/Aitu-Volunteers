package com.aitu.volunteers.service;

import com.aitu.volunteers.model.Event;
import com.aitu.volunteers.model.EventDay;
import com.aitu.volunteers.model.EventRequirement;
import com.aitu.volunteers.model.User;
import com.aitu.volunteers.model.request.EventRequest;
import com.aitu.volunteers.model.request.EventRequirementRequest;
import com.aitu.volunteers.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserService userService;

    public Event createEvent(EventRequest eventRequest) {
        EventRequirementRequest requirementRequest = eventRequest.getRequirement();
        Event event = Event.builder()
                .title(eventRequest.getTitle())
                .description(eventRequest.getDescription())
                .leader(userService.getUserByBarcode(eventRequest.getLeaderBarcode()))
                .category(eventRequest.getCategory())
                .hasTransport(eventRequest.getHasTransport())
                .hasMeal(eventRequest.getHasMeal())
                .dresscode(eventRequest.getDresscode())
                .responsibilities(eventRequest.getResponsibilities())
                .telegramLink(eventRequest.getTelegramLink())
                .requirement(EventRequirement.builder()
                        .gender(requirementRequest.getGender())
                        .age(requirementRequest.getAge())
                        .kazakh(requirementRequest.getKazakh())
                        .russian(requirementRequest.getRussian())
                        .english(requirementRequest.getEnglish())
                        .isCertificateRequired(requirementRequest.getIsCertificateRequired())
                        .build())
                .build();
        event.setDays(eventRequest.getDays().stream().map((r) -> (
                EventDay.builder()
                        .event(event)
                        .startDate(r.getStartDate())
                        .endDate(r.getEndDate())
                        .isActive(false)
                        .participantLimit(r.getParticipantLimit())
                        .build()
        )).toList());
        return eventRepository.save(event);
    }

}
