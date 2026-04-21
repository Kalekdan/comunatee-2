package com.comunatee.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private Long parentId;

    private String path;
    private int depth;

    @Column(columnDefinition = "TEXT")
    private String body;

    private int score = 0;

    private boolean deleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
