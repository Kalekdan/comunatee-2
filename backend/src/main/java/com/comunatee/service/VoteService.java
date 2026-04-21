package com.comunatee.service;

import com.comunatee.entity.Comment;
import com.comunatee.entity.Post;
import com.comunatee.entity.User;
import com.comunatee.entity.Vote;
import com.comunatee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class VoteService {

    @Autowired private VoteRepository voteRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private UserRepository userRepository;

    public Post votePost(Long userId, Long postId, int voteType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Optional<Vote> existing = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existing.isPresent()) {
            Vote v = existing.get();
            int oldVote = v.getVoteType();
            if (oldVote == voteType) {
                post.setScore(post.getScore() - oldVote);
                voteRepository.delete(v);
            } else {
                post.setScore(post.getScore() - oldVote + voteType);
                v.setVoteType(voteType);
                voteRepository.save(v);
            }
        } else {
            Vote v = new Vote();
            v.setUser(user);
            v.setPostId(postId);
            v.setVoteType(voteType);
            voteRepository.save(v);
            post.setScore(post.getScore() + voteType);
        }

        return postRepository.save(post);
    }

    public Comment voteComment(Long userId, Long commentId, int voteType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        Optional<Vote> existing = voteRepository.findByUserIdAndCommentId(userId, commentId);
        if (existing.isPresent()) {
            Vote v = existing.get();
            int oldVote = v.getVoteType();
            if (oldVote == voteType) {
                comment.setScore(comment.getScore() - oldVote);
                voteRepository.delete(v);
            } else {
                comment.setScore(comment.getScore() - oldVote + voteType);
                v.setVoteType(voteType);
                voteRepository.save(v);
            }
        } else {
            Vote v = new Vote();
            v.setUser(user);
            v.setCommentId(commentId);
            v.setVoteType(voteType);
            voteRepository.save(v);
            comment.setScore(comment.getScore() + voteType);
        }

        return commentRepository.save(comment);
    }
}
