package com.aitu.volunteers.service;

import com.aitu.volunteers.model.*;
import com.aitu.volunteers.model.request.EventRequest;
import com.aitu.volunteers.model.request.EventRequirementRequest;
import com.aitu.volunteers.repository.EventDayRepository;
import com.aitu.volunteers.repository.EventRegistrationRepository;
import com.aitu.volunteers.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EventRegistrationRepository eventRegistrationRepository;

    private final EventDayRepository eventDayRepository;

    private final UserService userService;

    private final QrCodeService qrCodeService;

    public EventDay getEventDayById(Long id) {
        return eventDayRepository.findEventDayById(id).orElseThrow();
    }

    public EventRegistration getEventRegistrationByUserAndEventDay(User user, EventDay eventDay) {
        return eventRegistrationRepository.findByUserAndEventDay(user, eventDay).orElseThrow();
    }

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

    public EventRegistration registerUserToEventDay(User user, EventDay eventDay) {
        if(eventRegistrationRepository.existsByUserAndEventDay(user, eventDay)) return null;
        if(!canUserRegisterToEvent(user, eventDay)) return null;
        EventRegistration eventRegistration = EventRegistration.builder()
                .registrationDate(LocalDateTime.now())
                .user(user)
                .eventDay(eventDay)
                .startQr(qrCodeService.createQrCode())
                .endQr(qrCodeService.createQrCode())
                .build();
        return eventRegistrationRepository.save(eventRegistration);
    }

    public EventDay setEventDayActivation(User user, EventDay eventDay, boolean isActive) {
        if(!hasPermissionToEvent(user, eventDay.getEvent())) return null;
        eventDay.setActive(isActive);
        return eventDayRepository.save(eventDay);
    }

    // add the necessary roles
    private boolean hasPermissionToEvent(User user, Event event) {
        return user.getId().equals(event.getLeader().getId());
    }

    @Transactional
    public void unregisterUserToEventDay(User user, EventDay eventDay) {
        eventRegistrationRepository.deleteByUserAndEventDay(user, eventDay);
    }

    @Transactional
    public void cancelAllRegistrationsForUser(User user) {
        eventRegistrationRepository.deleteAllByUser(user);
    }

    public boolean canUserRegisterToEvent(User user, EventDay eventDay) {
        if(userService.hasActiveBan(user)) return false;
        EventRequirement requirement = eventDay.getEvent().getRequirement();
        UserInfo userInfo = user.getUserInfo();
        // add age
        return (requirement.getGender() == null || requirement.getGender() == userInfo.getGender()) &&
                (!requirement.getIsCertificateRequired() || (user.getCertificate() != null && user.getCertificate().getIsApproved())) &&
                (requirement.getKazakh() == null || userInfo.getKazakh() >= requirement.getKazakh()) &&
                (requirement.getRussian() == null || userInfo.getRussian() >= requirement.getRussian()) &&
                (!requirement.getIsCertificateRequired() || requirement.getEnglish() == null || userInfo.getEnglish() >= requirement.getEnglish()) &&
                (eventDay.getParticipantLimit() > eventDay.getRegistrations().size());

    }

    public QrCode getStartQrByUserAndEventDay(User user, EventDay eventDay) {
        return getEventRegistrationByUserAndEventDay(user, eventDay).getStartQr();
    }


    public QrCode getEndQrByUserAndEventDay(User user, EventDay eventDay) {
        return getEventRegistrationByUserAndEventDay(user, eventDay).getEndQr();
    }
}
