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
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_sub", unique = true, nullable = false, length = 70)
    private String userSub;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;
}
