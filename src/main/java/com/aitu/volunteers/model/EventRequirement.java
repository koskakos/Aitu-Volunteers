package com.aitu.volunteers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_requirement")
public class EventRequirement {

    @JsonIgnore
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "kazakh")
    private Byte kazakh;

    @Column(name = "russian")
    private Byte russian;

    @Column(name = "english")
    private Byte english;

    @Column(name = "is_certificate_required")
    private Boolean isCertificateRequired;
}
