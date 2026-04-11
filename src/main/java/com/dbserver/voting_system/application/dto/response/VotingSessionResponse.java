package com.dbserver.voting_system.application.dto.response;

import java.time.Instant;

public record VotingSessionResponse(String agendaId, Instant openedAt, Instant endsAt, String status) {
}
