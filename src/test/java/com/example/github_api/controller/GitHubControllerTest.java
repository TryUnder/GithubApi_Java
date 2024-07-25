package com.example.github_api.controller;

import com.example.github_api.exception.UserNotFoundException;
import com.example.github_api.model.GitHubRepository;
import com.example.github_api.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GitHubController.class)
public class GitHubControllerTest {

    @MockBean
    private GitHubService gitHubService;

    @InjectMocks
    private GitHubController gitHubController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetUserRepositories_UserNotFound() throws Exception {
        String username = "nonExistentUser";
        when(gitHubService.getRepositories(anyString())).thenThrow(new UserNotFoundException(username));

        mockMvc.perform(get("/api/github/users/{username}/repositories", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(String.format("User %s not found", username)));
    }

    @Test
    void testGetUserRepositories_Success() throws Exception {
        String username = "existentUser";

        GitHubRepository gitHubRepository = new GitHubRepository();
        gitHubRepository.setName("test-repo");
        gitHubRepository.setOwner(new GitHubRepository.Owner());
        gitHubRepository.getOwner().setLogin(username);
        gitHubRepository.setBranches(Collections.emptyList());

        when(gitHubService.getRepositories(anyString())).thenReturn(Collections.singletonList(gitHubRepository));

        mockMvc.perform(get("/api/github/users/{username}/repositories", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("test-repo"))
                .andExpect(jsonPath("$[0].owner.login").value(username));
    }
}
