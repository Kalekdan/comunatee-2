package com.comunatee.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String passwordHash;
    private String profilePicUrl;

    private int postRating = 0;
    private int commentRating = 0;
}
