package com.atipera.recruitment_atipera;

import com.atipera.recruitment_atipera.model.dto.RepositoryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecruitmentAtiperaApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenExistingUser_whenGetNonForkRepositories_thenReturnCorrectResult() {
        // given
        String username = "turczak";
        String url = "/api/" + username;
        JsonNode expectedJson = readJsonResource();
        // when
        ResponseEntity<List<RepositoryDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RepositoryDto>>() {
                }
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<RepositoryDto> repositories = response.getBody();
        assertThat(repositories).isNotNull().isNotEmpty();
        assertValidStructure(repositories, username);
        assertThat(toJson(repositories)).isEqualTo(expectedJson);
    }

    @SneakyThrows
    private JsonNode readJsonResource() {
        return objectMapper.readTree(
                getClass().getResourceAsStream("/expected.json")
        );
    }

    @SneakyThrows
    private JsonNode toJson(List<RepositoryDto> repositories) {
        return objectMapper.readTree(
                objectMapper.writeValueAsString(repositories)
        );
    }

    private void assertValidStructure(List<RepositoryDto> repositories, String username) {
        repositories.forEach(repo -> {
            assertThat(repo.name()).isNotBlank();
            assertThat(repo.ownerLogin()).isNotBlank().isEqualTo(username);
            assertThat(repo.branches()).isNotNull().isNotEmpty();
            repo.branches().forEach(branch -> {
                assertThat(branch.name()).isNotBlank();
                assertThat(branch.lastCommitSha()).isNotBlank();
            });
        });
    }

}
