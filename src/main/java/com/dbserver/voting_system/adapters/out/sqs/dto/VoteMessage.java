package com.dbserver.voting_system.adapters.out.sqs.dto;

import java.time.Instant;

public record VoteMessage(String agendaId, String cpf, String voteValue, Instant votedAt) {
}
