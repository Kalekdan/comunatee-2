package com.comunatee.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Community {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    private int subscriberCount = 0;
}
