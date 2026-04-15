package com.dbserver.voting_system.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterVoteRequest(
        @NotBlank(message = "cpf is required")
        String cpf,
        @NotBlank(message = "vote is required")
        String vote
) {
}
