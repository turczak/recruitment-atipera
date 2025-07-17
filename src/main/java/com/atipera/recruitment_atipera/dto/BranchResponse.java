package com.atipera.recruitment_atipera.dto;

public record BranchResponse(String name, Commit commit) {
    public record Commit(String sha) {
    }
}
