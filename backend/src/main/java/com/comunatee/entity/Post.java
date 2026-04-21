package com.comunatee.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private int score = 0;
    private int commentCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
}
