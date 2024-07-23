package com.example.github_api.controller;

import com.example.github_api.exception.UserNotFoundException;
import com.example.github_api.controller.GitHubController;
import com.example.github_api.model.GitHubRepository;
import com.example.github_api.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GitHubControllerTest {

    @InjectMocks
    private GitHubController gitHubController;

    @Mock
    private GitHubService gitHubService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gitHubController).build();
    }

    @Test
    void testGetUserRepositories_UserNotFound() throws Exception {
        String username = "nonExistentUser";
        when(gitHubService.getRepositories(anyString())).thenThrow(new UserNotFoundException(String.format("%s", username)));

        mockMvc.perform(get("api/github/users/nonExistentUser/repositories")
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

        mockMvc.perform(get(String.format("/api/github/users/%s/repositories", username))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("test-repo"))
                .andExpect(jsonPath("$[0].owner.login").value(username));
    }
}
