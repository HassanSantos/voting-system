package com.dbserver.voting_system.adapters.out.dynamodb.entity;

import java.time.Instant;

public record VotingSessionItem(String agendaId, Instant openedAt, Instant endsAt, String status) {
}
