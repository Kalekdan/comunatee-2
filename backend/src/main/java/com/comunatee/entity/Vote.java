package com.comunatee.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id", "comment_id"}))
@Data
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long postId;
    private Long commentId;

    private int voteType; // +1 or -1
}
