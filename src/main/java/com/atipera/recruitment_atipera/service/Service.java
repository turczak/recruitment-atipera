package com.atipera.recruitment_atipera.service;

import com.atipera.recruitment_atipera.dto.BranchDto;
import com.atipera.recruitment_atipera.dto.BranchResponse;
import com.atipera.recruitment_atipera.dto.RepositoryDto;
import com.atipera.recruitment_atipera.dto.RepositoryResponse;
import com.atipera.recruitment_atipera.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service {

    private final RestTemplate restTemplate;

    public List<RepositoryDto> getNonForkRepositories(String username) {
        String url = String.format("https://api.github.com/users/%s/repos", username);
        try {
            ResponseEntity<List<RepositoryResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            List<RepositoryResponse> repositories = Objects.requireNonNull(response.getBody());
            return repositories.stream()
                    .filter(repository -> !repository.fork())
                    .map(repository -> {
                        List<BranchDto> branches = getBranches(username, repository.name());
                        return new RepositoryDto(repository.name(), repository.owner().login(), branches);
                    })
                    .toList();
        } catch (RestClientException exception) {
            throw new UserNotFoundException("User '" + username + "' not found.");
        }
    }

    private List<BranchDto> getBranches(String username, String repositoryName) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/branches",
                username,
                repositoryName);
        ResponseEntity<List<BranchResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<BranchResponse> responseBody = Objects.requireNonNull(response.getBody());
        return responseBody.stream()
                .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                .toList();
    }

}
