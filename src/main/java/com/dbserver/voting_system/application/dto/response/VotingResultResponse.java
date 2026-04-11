package com.dbserver.voting_system.application.dto.response;

public record VotingResultResponse(String agendaId, long yesVotes, long noVotes, long totalVotes, String outcome) {
}
