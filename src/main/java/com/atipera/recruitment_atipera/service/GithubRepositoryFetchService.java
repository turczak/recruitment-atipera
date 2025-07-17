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

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class GithubRepositoryFetchService {

    private static final String REPOSITORIES_URL = "https://api.github.com/users/%s/repos";
    private static final String BRANCHES_URL = "https://api.github.com/repos/%s/%s/branches";


    private final RestTemplate restTemplate;

    public List<RepositoryDto> getNonForkRepositories(String username) {
        String url = String.format(REPOSITORIES_URL, username);
        try {
            ResponseEntity<List<RepositoryResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RepositoryResponse>>() {
                    }
            );
            List<RepositoryResponse> repositories = response.getBody();
            if (repositories == null) {
                return Collections.emptyList();
            }
            return repositories.stream()
                    .filter(Predicate.not(RepositoryResponse::fork))
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
                BRANCHES_URL,
                username,
                repositoryName);
        ResponseEntity<List<BranchResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BranchResponse>>() {
                }
        );
        List<BranchResponse> branches = response.getBody();
        if (branches == null) {
            return Collections.emptyList();
        }
        return branches.stream()
                .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                .toList();
    }

}
