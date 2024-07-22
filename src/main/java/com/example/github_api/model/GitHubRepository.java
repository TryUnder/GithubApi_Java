package com.example.github_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({ "name", "owner", "branches" })
public class GitHubRepository {
    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private Owner owner;

    @JsonIgnore
    private boolean fork;

    @Data
    public static class Owner {
        @JsonProperty("login")
        private String login;
    }

    @Data
    public static class Branch {
        @JsonProperty("name")
        private String name;

        @JsonProperty("commit")
        private Commit commit;

        @Data
        public static class Commit {
            @JsonProperty("sha")
            private String sha;
        }
    }

    private List<Branch> branches;

    public String getName() { return name; }
    public boolean isFork() { return fork; }
    public void setFork(boolean fork) { this.fork = fork; }
    public List<Branch> getBranches() { return branches; }
    public void setBranches(List<Branch> branches) { this.branches = branches; }
}