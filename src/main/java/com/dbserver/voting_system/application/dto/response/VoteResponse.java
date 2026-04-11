package com.dbserver.voting_system.application.dto.response;

import java.time.Instant;

public record VoteResponse(String agendaId, String associateId, String voteValue, Instant votedAt) {
}
