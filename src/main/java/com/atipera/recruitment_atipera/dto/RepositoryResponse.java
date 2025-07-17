package com.atipera.recruitment_atipera.dto;

public record RepositoryResponse(String name, boolean fork, Owner owner) {
    public record Owner(String login) {
    }
}
