package com.atipera.recruitment_atipera.controller;

import com.atipera.recruitment_atipera.model.dto.RepositoryDto;
import com.atipera.recruitment_atipera.service.GithubRepositoryFetchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class Controller {

    private final GithubRepositoryFetchService service;

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryDto>> getUserRepositories(
            @PathVariable String username) {
        List<RepositoryDto> nonForkRepositories = service.getNonForkRepositories(username);
        return ResponseEntity.ok(nonForkRepositories);
    }

}
