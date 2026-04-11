package com.dbserver.voting_system.adapters.out.dynamodb.entity;

public record VotingResultItem(String agendaId, long yesVotes, long noVotes, long totalVotes, String outcome) {
}
