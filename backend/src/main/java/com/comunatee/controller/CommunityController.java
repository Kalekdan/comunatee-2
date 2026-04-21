package com.comunatee.controller;

import com.comunatee.entity.Community;
import com.comunatee.repository.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {

    @Autowired private CommunityRepository communityRepository;

    @GetMapping
    public List<Community> getAll() {
        return communityRepository.findAll();
    }

    @GetMapping("/{id}")
    public Community getById(@PathVariable Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Community create(@RequestBody Community community) {
        return communityRepository.save(community);
    }
}
