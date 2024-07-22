# GitHub API Application

This application provides an API to fetch GitHub repositories and branches information for a given user.
The application is built using Spring Boot and Java 21.
You can easily track changes by following [Issues List](https://github.com/TryUnder/GithubApi_Java/issues).

## Prerequisites

- Java 21
- Spring Boot 3
- Testing
  - JUnit
  - Mock tests
- GitHub Personal Access Token (Optional)

## Features

- Fetch non-fork GitHub repositories for a specified user.
- Retrieve branches and last commit SHA for each branch in a specified repository.
- Handle non-existing GitHub users with appropriate 404 response.

## Configuration

### Properties

Create and `application.properies` file in the `src/main/resources` directory with the following content:

```properties
github.api.url=https://api.github.com
github.api.token=${GITHUB_API_TOKEN}  # Optional
