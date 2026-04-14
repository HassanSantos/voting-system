package com.dbserver.voting_system.adapters.in.web.dto;

import jakarta.validation.constraints.Positive;

public record OpenVotingSessionRequest(
        @Positive(message = "durationMinutes must be greater than zero")
        Long durationMinutes
) {
}
