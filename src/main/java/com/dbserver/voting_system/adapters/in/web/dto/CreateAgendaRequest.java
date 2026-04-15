package com.dbserver.voting_system.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAgendaRequest(
        @NotBlank(message = "title is required")
        String title,
        String description
) {
}
