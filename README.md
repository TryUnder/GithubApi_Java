# GitHub API Application

This application provides an API to fetch GitHub repositories and branches information for a given user. </br>
It was built using Spring Boot and Java 21. </br>
You can easily track changes by following [Issues List](https://github.com/TryUnder/GithubApi_Java/issues) and [Project List](https://github.com/users/TryUnder/projects/2). </br>
Project `GithubApi_Java` implements [CI Auto testing](https://github.com/TryUnder/GithubApi_Java/actions) as well by the use of github actions. </br>
Check [Technical Dockumentation](https://github.com/TryUnder/GithubApi_Java/wiki/Technician-Documentation) for more information.

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
