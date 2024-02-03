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
@Table(name = "user_info")
public class UserInfo {

    @JsonIgnore
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstname", length = 15)
    private String firstname;

    @Column(name = "lastname", length = 20)
    private String lastname;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "kazakh")
    private Byte kazakh;

    @Column(name = "russian")
    private Byte russian;

    @Column(name = "english")
    private Byte english;

    @Column(name = "IIN", unique = true, length = 12)
    private String IIN;

    @Column(name = "phone", length = 15)
    private String phone;
}
