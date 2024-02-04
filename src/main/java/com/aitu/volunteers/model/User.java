package com.aitu.volunteers.model;

import com.aitu.volunteers.model.serializer.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@JsonSerialize(using = UserSerializer.class)
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "sub", unique = true, nullable = false, length = 70)
    private String userSub;

    @Column(name = "barcode", unique = true, nullable = false, length = 40)
    private String barcode;

    @Column(name = "is_registered")
    private boolean isRegistered;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private UserInfo userInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    private UserCertificate certificate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBan> bans = new LinkedList<>();

    @ManyToMany(targetEntity = Team.class, mappedBy = "members", cascade = CascadeType.ALL)
    private List<Team> teams = new LinkedList<>();
}
