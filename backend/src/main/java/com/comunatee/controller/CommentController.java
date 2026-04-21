package com.comunatee.controller;

import com.comunatee.entity.Comment;
import com.comunatee.service.CommentService;
import com.comunatee.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired private CommentService commentService;
    @Autowired private VoteService voteService;

    @GetMapping("/post/{postId}")
    public List<Comment> getByPost(@PathVariable Long postId) {
        return commentService.getByPost(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody Map<String, Object> body) {
        Long postId = body.containsKey("postId") ? Long.valueOf(body.get("postId").toString()) : null;
        Long parentId = body.containsKey("parentId") && body.get("parentId") != null
                ? Long.valueOf(body.get("parentId").toString()) : null;
        Long authorId = Long.valueOf(body.get("authorId").toString());
        String text = body.get("body").toString();

        if (parentId != null) {
            return commentService.createReply(parentId, authorId, text);
        } else {
            return commentService.createTopLevel(postId, authorId, text);
        }
    }

    @DeleteMapping("/{id}")
    public Comment delete(@PathVariable Long id) {
        return commentService.softDelete(id);
    }

    @PostMapping("/{id}/vote")
    public Comment vote(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        int voteType = Integer.parseInt(body.get("voteType").toString());
        return voteService.voteComment(userId, id, voteType);
    }
}
