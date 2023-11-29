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
@Table(name = "users_info")
public class UserInfo {

    @JsonIgnore
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_firstname", nullable = false, length = 15)
    private String firstname;

    @Column(name = "user_lastname", nullable = false, length = 20)
    private String lastname;

    @Column(name = "user_gender", nullable = false)
    private Gender gender;

    @Column(name = "user_IIN", unique = true, length = 12)
    private String IIN;

    @Column(name = "user_phone", nullable = false, length = 15)
    private String phone;
}
