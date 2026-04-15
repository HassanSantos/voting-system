package com.dbserver.voting_system.application.dto.request;

import com.dbserver.voting_system.domain.enums.VoteValue;

public record RegisterVoteCommand(String agendaId, String associateId, VoteValue voteValue) {
}
