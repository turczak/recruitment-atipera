package com.atipera.recruitment_atipera.model.response;

import com.atipera.recruitment_atipera.model.dto.OwnerDto;

public record RepositoryResponse(String name, boolean fork, OwnerDto ownerDto) {
}
