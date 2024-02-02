package com.aitu.volunteers.model;

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
@Table(name = "post")
public class Post {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private User author;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private PostInfo postInfo;
}
