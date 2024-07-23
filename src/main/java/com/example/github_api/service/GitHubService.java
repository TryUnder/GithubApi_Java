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
public class GitHubService {

    public List<GitHubRepository> getRepositories(String username) {
        return List.of();
    }

    public List<GitHubRepository.Branch> getBranches(@PathVariable String username, @PathVariable String repository) {
        try {
            return List.of();
        } catch (UserNotFoundException e) {
            return List.of();
        }
    }
}
