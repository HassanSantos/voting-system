package com.dbserver.voting_system.adapters.out.dynamodb.entity;

import java.time.Instant;

public record VoteItem(String agendaId, String associateId, String voteValue, Instant votedAt) {
}
