package com.example.github_api.controller;

import com.example.github_api.exception.UserNotFoundException;
import com.example.github_api.model.GitHubRepository;
import com.example.github_api.service.GitHubService;
import com.example.github_api.exception.GlobalExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/github")
@RequiredArgsConstructor
public class GitHubController {
    private final GitHubService gitHubService;

    @GetMapping("/users/{username}/repositories")
    public ResponseEntity<?> getUserRepositories(@PathVariable String username) {
        try {
            List<GitHubRepository> repositories = gitHubService.getRepositories(username);
            return ResponseEntity.ok(repositories);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GlobalExceptionHandler());
        }
    }

    @GetMapping("/users/{username}/repositories/{repository}/branches")
    public ResponseEntity<?> getRepositoryBranches(@PathVariable String username, @PathVariable String repository) {
        try {
            List<GitHubRepository.Branch> branches = gitHubService.getBranches(username, repository);
            return ResponseEntity.ok(branches);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GlobalExceptionHandler());
        }
    }
}
