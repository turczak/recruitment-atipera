# GitHub repository fetcher

This is a Spring Boot application that fetches non-forked public repositories for a given GitHub user and returns them with their branches and latest commit SHA.

## Features

- Fetch all non-forked repositories for a GitHub username via GitHub API
- Returns JSON response branches with the latest commit SHA for each repository
- Handles users not found with NOT_FOUND (404)

## Technologies

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Test
- Lombok
- Maven

# Getting started

## Prerequisites

- Java 21
- Maven

## Running locally

1. Clone the repository

```
git clone https://github.com/turczak/recruitment-atipera.git
```

2. Build and run application

Using maven:

```
./mvnw spring-boot:run
```

3. Access the API

The service will start on port 8080 by default.<br>
Example request to fetch repositories for user `turczak`:

```
curl --request GET \
  --url http://localhost:8080/api/turczak
```

# API details

## Get non-fork repositories

```
GET /api/{username}
```

- **Path parameter**:<br>
  `username` - GitHub username to fetch repositories for
- **Success response**:<br>
    - HTTP 200 OK
    - JSON array of repositories, each with:<br>
        - `name`
        - `ownerLogin`
        - `branches`
            - `name`
            - `lastCommitSha`
- **Error response**:

```
  "status": 404,
  "message": "User %username not found."
```

# Tests

Run integration test using:<br>

```
./mvnw test
```

# Notes

Rate limiting may apply while using GitHub API.
