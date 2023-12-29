package com.aitu.volunteers.model;

import com.aitu.volunteers.model.serializer.TeamSerializer;
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
@Table(name = "team")
@JsonSerialize(using = TeamSerializer.class)
public class Team {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private User leader;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    private List<User> members = new LinkedList<>();

}
