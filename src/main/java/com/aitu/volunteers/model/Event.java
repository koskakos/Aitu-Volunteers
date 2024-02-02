package com.aitu.volunteers.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "leader")
    private User leader;

    @Column(name = "category")
    private EventCategory category;

    @Column(name = "has_transport")
    private Boolean hasTransport;

    @Column(name = "has_meal")
    private Boolean hasMeal;

    @Column(name = "dresscode")
    private String dresscode;

    @Column(name = "responsibility")
    @Enumerated
    @ElementCollection(targetClass = EventResponsibility.class, fetch = FetchType.EAGER)
    private List<EventResponsibility> responsibilities;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private EventRequirement requirement;
}
