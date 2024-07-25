package com.example.github_api.service;

import com.example.github_api.exception.UserNotFoundException;
import com.example.github_api.model.GitHubRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {
    private static Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private final RestTemplate restTemplate;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${github.api.url}")
    private String gitHubApiUrl;

    @Value("${github.api.token}")
    private String gitHubApiToken;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (gitHubApiToken != null && !gitHubApiToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + gitHubApiToken);
        }

        return headers;
    }
    public List<GitHubRepository> getRepositories(String username) {
        String url = String.format("%s/users/%s/repos", gitHubApiUrl, username);
        logger.info("Fetching repositories for user: {}", username);

        try {
            HttpEntity<String> entity = new HttpEntity<String>(createHeaders());
            ResponseEntity<GitHubRepository[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubRepository[].class);

            GitHubRepository[] repositories = response.getBody();
            if (repositories == null) {
                logger.warn("No repositories found for user: {}", username);
                return List.of();
            }

            return Arrays.stream(repositories)
                    .filter(repo -> !repo.isFork())
                    .map(repo -> {
                        List<GitHubRepository.Branch> branches = getBranches(username, repo.getName());
                        repo.setBranches(branches);
                        return repo;
                    })
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("User not found: {}", username);
                throw new UserNotFoundException(username);
            }
            logger.error("Error fetching repositories for user: {}", username, e);
            throw e;
        }
    }

    public List<GitHubRepository.Branch> getBranches(@PathVariable String username, @PathVariable String repository) {
        String url = String.format("%s/repos/%s/%s/branches", gitHubApiUrl, username, repository);
        logger.info("Fetching branches for repository {}/{}", username, repository);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<GitHubRepository.Branch[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubRepository.Branch[].class);

        GitHubRepository.Branch[] branches = response.getBody();
        return branches != null ? Arrays.asList(branches) : List.of();
    }
}
