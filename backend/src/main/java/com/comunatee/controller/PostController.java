package com.comunatee.controller;

import com.comunatee.entity.Post;
import com.comunatee.repository.PostRepository;
import com.comunatee.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired private PostRepository postRepository;
    @Autowired private VoteService voteService;

    @GetMapping
    public List<Post> getAll() {
        return postRepository.findAllByOrderByScoreDesc();
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/community/{communityId}")
    public List<Post> getByCommunity(@PathVariable Long communityId) {
        return postRepository.findByCommunityIdOrderByScoreDesc(communityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @PostMapping("/{id}/vote")
    public Post vote(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        int voteType = Integer.parseInt(body.get("voteType").toString());
        return voteService.votePost(userId, id, voteType);
    }
}
