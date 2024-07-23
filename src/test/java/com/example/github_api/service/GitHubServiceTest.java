package com.example.github_api.service;

import com.example.github_api.exception.UserNotFoundException;
import com.example.github_api.model.GitHubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GitHubServiceTest {

    @InjectMocks
    private GitHubService gitHubService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String gitHubApiUrl;

    @Value("${github.api.token}")
    private String gitHubApiToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRepositories_UserNotFound() {
        String username = "nonExistentUser";
        String url = String.format("%s/users/%s/repos", gitHubApiUrl, username);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubRepository[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(UserNotFoundException.class, () -> gitHubService.getRepositories(username));
    }

    @Test
    void testGetRepositories_Succes_WithToken() {
        String username = "existentUser";
        String url = String.format("%s/users/%s/repos", gitHubApiUrl, username);

        GitHubRepository[] gitHubRepositories = new GitHubRepository[1];
        gitHubRepositories[0] = new GitHubRepository();
        gitHubRepositories[0].setName("test-repo");
        gitHubRepositories[0].setFork(false);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubRepository[].class)))
                .thenReturn(new ResponseEntity<>(gitHubRepositories, HttpStatus.OK));

        List<GitHubRepository> result = gitHubService.getRepositories(username);

        assertEquals(1, result.size());
        assertEquals("test-repo", result.get(0).getName());
    }

    @Test
    void testGetRepositories_Succes_WithoutToken() {
        gitHubApiToken = "";

        String username = "existentUser";
        String url = String.format("%s/users/%s/repos", gitHubApiUrl, username);

        GitHubRepository[] repos = new GitHubRepository[1];
        repos[0] = new GitHubRepository();
        repos[0].setName("test-repo");
        repos[0].setFork(false);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubRepository[].class)))
                .thenReturn(new ResponseEntity<>(repos, HttpStatus.OK));

        List<GitHubRepository> result = gitHubService.getRepositories(username);

        assertEquals(1, result.size());
        assertEquals("test-repo", result.get(0).getName());
    }

    @Test
    void testGetBranches_Success_WithToken() {
        String username = "existentUser";
        String repository = "test-repo";
        String url = String.format("%s/repos/%s/%s/branches", gitHubApiUrl, username, repository);

        GitHubRepository.Branch[] branches = new GitHubRepository.Branch[1];
        branches[0] = new GitHubRepository.Branch();
        branches[0].setName("main");
        branches[0].setCommit(new GitHubRepository.Branch.Commit());
        branches[0].getCommit().setSha("123456");

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubRepository.Branch[].class)))
                .thenReturn(new ResponseEntity<>(branches, HttpStatus.OK));

        List<GitHubRepository.Branch> result = gitHubService.getBranches(username, repository);

        assertEquals(1, result.size());
        assertEquals("main", result.get(0).getName());
        assertEquals("123456", result.get(0).getCommit().getSha());
    }

    @Test
    void testGetBranches_Success_WithoutToken() {
        gitHubApiToken = "";

        String username = "existentUser";
        String repository = "test-repo";
        String url = String.format("%s/repos/%s/%s/branches", gitHubApiUrl, username, repository);

        GitHubRepository.Branch[] branches = new GitHubRepository.Branch[1];
        branches[0] = new GitHubRepository.Branch();
        branches[0].setName("main");
        branches[0].setCommit(new GitHubRepository.Branch.Commit());
        branches[0].getCommit().setSha("123456");

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubRepository.Branch[].class)))
                .thenReturn(new ResponseEntity<>(branches, HttpStatus.OK));

        List<GitHubRepository.Branch> result = gitHubService.getBranches(username, repository);

        assertEquals(1, result.size());
        assertEquals("main", result.get(0).getName());
        assertEquals("123456", result.get(0).getCommit().getSha());
    }
}
