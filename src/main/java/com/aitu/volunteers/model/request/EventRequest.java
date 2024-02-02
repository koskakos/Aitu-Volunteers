package com.aitu.volunteers.model.request;

import com.aitu.volunteers.model.EventCategory;
import com.aitu.volunteers.model.EventResponsibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String title;
    private String description;
    private String leaderBarcode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventCategory category;
    private Boolean hasTransport;
    private Boolean hasMeal;
    private String dresscode;
    private List<EventResponsibility> responsibilities = new LinkedList<>();

}
