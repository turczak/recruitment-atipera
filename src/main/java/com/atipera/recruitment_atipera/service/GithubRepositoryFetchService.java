package com.atipera.recruitment_atipera.service;

import com.atipera.recruitment_atipera.exception.UserNotFoundException;
import com.atipera.recruitment_atipera.model.dto.BranchDto;
import com.atipera.recruitment_atipera.model.dto.RepositoryDto;
import com.atipera.recruitment_atipera.model.response.BranchResponse;
import com.atipera.recruitment_atipera.model.response.RepositoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GithubRepositoryFetchService {

    private final RestTemplate restTemplate;

    public List<RepositoryDto> getNonForkRepositories(String username) {
        String url = String.format("https://api.github.com/users/%s/repos", username);
        try {
            ResponseEntity<List<RepositoryResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RepositoryResponse>>() {
                    }
            );
            List<RepositoryResponse> repositories = Objects.requireNonNull(response.getBody());
            return repositories.stream()
                    .filter(repository -> !repository.fork())
                    .map(repository -> {
                        List<BranchDto> branches = getBranches(username, repository.name());
                        return new RepositoryDto(repository.name(), repository.ownerDto().login(), branches);
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
                new ParameterizedTypeReference<List<BranchResponse>>() {
                }
        );
        List<BranchResponse> responseBody = Objects.requireNonNull(response.getBody());
        return responseBody.stream()
                .map(branch -> new BranchDto(branch.name(), branch.commitDto().sha()))
                .toList();
    }

}
