package com.comunatee.repository;

import com.comunatee.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCommunityIdOrderByScoreDesc(Long communityId);
    List<Post> findAllByOrderByScoreDesc();
}
