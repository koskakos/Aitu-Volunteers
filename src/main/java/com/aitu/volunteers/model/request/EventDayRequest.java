package com.aitu.volunteers.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDayRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer participantLimit;
}
