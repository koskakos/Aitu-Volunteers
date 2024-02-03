package com.aitu.volunteers.model;

import com.aitu.volunteers.model.serializer.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "user_certificate")
public class UserCertificate {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "certificate", cascade = CascadeType.ALL)
    private User user;

    @Column(name = "certificate_url")
    private String certificateUrl;

    @Column(name = "is_approved")
    private Boolean isApproved;

}
