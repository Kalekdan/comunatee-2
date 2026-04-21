package com.comunatee.service;

import com.comunatee.entity.Comment;
import com.comunatee.entity.Post;
import com.comunatee.entity.User;
import com.comunatee.repository.CommentRepository;
import com.comunatee.repository.PostRepository;
import com.comunatee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    public Comment createTopLevel(Long postId, Long authorId, String body) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(author);
        c.setBody(body);
        c.setParentId(null);
        c.setDepth(0);
        commentRepository.save(c);

        c.setPath(String.valueOf(c.getId()));
        commentRepository.save(c);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return c;
    }

    public Comment createReply(Long parentId, Long authorId, String body) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment c = new Comment();
        c.setPost(parent.getPost());
        c.setAuthor(author);
        c.setBody(body);
        c.setParentId(parent.getId());
        c.setDepth(parent.getDepth() + 1);
        commentRepository.save(c);

        c.setPath(parent.getPath() + "." + c.getId());
        commentRepository.save(c);

        Post post = parent.getPost();
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return c;
    }

    public List<Comment> getByPost(Long postId) {
        return commentRepository.findByPostIdOrderByPath(postId);
    }

    public Comment softDelete(Long commentId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        c.setBody("[deleted]");
        c.setDeleted(true);
        return commentRepository.save(c);
    }
}
