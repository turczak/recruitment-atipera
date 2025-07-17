package com.atipera.recruitment_atipera.model.dto;

import java.util.List;

public record RepositoryDto(String name, String ownerLogin, List<BranchDto> branches) {
}
