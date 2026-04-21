# Comunatee (React + Spring Boot)

## Overview

This project is an alternative to Reddit built with:

* **React frontend**
* **Spring Boot backend**
* **PostgreSQL database**

It supports the core features required for an MVP:

* Users
* Communities (like subreddits)
* Posts
* Threaded comments (nested)
* Voting

---

# Core Design Principles

* Read-heavy system → optimize for fast reads
* Use **denormalized fields** (score, comment count)
* Avoid complex recursion in SQL
* Use **materialized path** for comment threading

---

# Project Structure

```
root/
  frontend/
  backend/
```

---

# ⚙️ Backend (Spring Boot)

## 1. Setup

Use Spring Initializr with:

* Spring Web
* Spring Data JPA
* PostgreSQL Driver
* Lombok (optional)

---

## 2. Database Config

```yaml id="2qv8yl"
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reddit_clone
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

---

# Core Entities

## User

```java id="p5lmre"
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String passwordHash;
    private String profilePicUrl;

    private int postRating = 0;
    private int commentRating = 0;
}
```

---

## Community

```java id="s0t8sj"
@Entity
public class Community {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToOne
    private User creator;

    private int subscriberCount = 0;
}
```

---

## Post

```java id="g9k4ne"
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Community community;

    @ManyToOne
    private User author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private int score = 0;
    private int commentCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
}
```

---

## Comment (Materialized Path)

```java id="y0m6d1"
@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User author;

    private Long parentId; // null for top-level

    private String path;   // e.g. "1.5.9"
    private int depth;     // nesting level

    @Column(columnDefinition = "TEXT")
    private String body;

    private int score = 0;

    private boolean isDeleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
```

---

## Vote

```java id="8eqx3k"
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id", "comment_id"}))
public class Vote {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private Long postId;
    private Long commentId;

    private int voteType; // +1 or -1
}
```


# Repositories

```java id="l6v7o3"
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCommunityIdOrderByScoreDesc(Long communityId);
}

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByPath(Long postId);
}
```

---

# Controllers

## Posts

```java id="v7j2r2"
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/community/{id}")
    public List<Post> getPosts(@PathVariable Long id) {
        return postRepository.findByCommunityIdOrderByScoreDesc(id);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }
}
```

---

## Comments

```java id="8k9r2v"
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/post/{postId}")
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentRepository.findByPostIdOrderByPath(postId);
    }
}
```

---

# Run Backend

```bash id="cxv4kt"
cd backend
./mvnw spring-boot:run
```

---

# Frontend (React)

## Setup

```bash id="s3i3du"
cd frontend
npm create vite@latest
npm install
npm install axios react-router-dom
```

---

## API Layer

```javascript id="6z1srt"
import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api"
});

export default API;
```

---

# Final Mental Model

Comments behave like file paths:

```id="0p6e4n"
/1
/1/2
/1/2/3
```

Sorting by path = correct thread order.

---
